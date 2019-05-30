package de.ur.mi.reactiontest;

import de.ur.mi.reactiontest.Enums.PanelType;
import de.ur.mi.reactiontest.Helper.TestStatistic;
import de.ur.mi.reactiontest.Interfaces.OnReactionCapturedListener;
import de.ur.mi.reactiontest.Panel.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReactionTestGUI extends JFrame implements ChangeListener, ActionListener, KeyListener {

    //region Form Panels
    private JPanel panelFrame;

    //region Content of outer Panel
    private JTabbedPane panelTabs;
    //region Content of TabbedPane
    private JPanel panelVisual;
    private JPanel panelAudio;
    private JPanel panelChoice;
    private JPanel panelStim;
    private JPanel panelResults;
    //endregion
    //endregion

    //region Content of outer panelM
    private JButton buttonStart;
    private JTextArea textAreaDescription;
    private JLabel labelTestResult;
    private JTextArea resultText;
    private JLabel stimDescription;
    //endregion

    //endregion
    private int frameWidth, frameHeight;
    private int baseTextSize;

    private PanelProperties[] panels;
    private PanelProperties currentPanel;

    public ReactionTestGUI() {
        initGUI();
        initDimensions();

        setListeners();
        setupTestList();

        this.stateChanged(null);
    }

    private void setupTestList() {
        PanelType[] panelTypes = PanelType.values();
        panels = new PanelProperties[panelTypes.length];

        for (int i = 0; i < panels.length; i++) {
            switch (panelTypes[i]) {
                case VISUAL:
                    panels[i] = new VisualPanel(panelVisual);
                    break;
                case AUDIO:
                    panels[i] = new AudioPanel(panelAudio);
                    break;
                case CHOICE:
                    panels[i] = new ChoicePanel(panelChoice);
                    break;
                case STIM:
                    panels[i] = new StimPanel(panelStim, stimDescription);
                    break;
                case RESULTS:
                    panels[i] = new ResultPanel(panelResults, resultText);
                    break;
            }
        }

        currentPanel = panels[panelTabs.getSelectedIndex()];
    }

    private void initGUI() {
        add(panelFrame);
        setTitle("Reaction Test Library by Fabian Schebera | github.com/realdegrees");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panelTabs.setSelectedIndex(panelTabs.getTabCount() - 1);
    }

    private void initDimensions() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frameWidth = screen.width / 3;
        frameHeight = screen.height / 3;

        baseTextSize = frameWidth / 50;

        setBounds(frameWidth / 2, frameHeight / 2, frameWidth, frameHeight);
        Font font = new Font("Courier", Font.PLAIN, baseTextSize);
        for (Component c : panelFrame.getComponents()) {
            c.setFont(font);
        }
        resultText.setFont(font); //Not included in the above array for some reason
        stimDescription.setFont(font); //Not included in the above array for some reason
    }

    private void setListeners() {
        panelTabs.addChangeListener(this);
        buttonStart.addActionListener(this);
        panelTabs.addKeyListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        currentPanel.stop();
        currentPanel = panels[panelTabs.getSelectedIndex()];
        setSideDescription(currentPanel.getPanelType());
        if (currentPanel.getPanelType() == PanelType.RESULTS) {
            setButtonDescription("Export Results");
            setSideDescription(PanelType.RESULTS);
            labelTestResult.setVisible(false);
            ((ResultPanel) currentPanel).listResults(getAvailableResults());
        } else {
            if (currentPanel.getTestStatistic().hasResult()) {
                setResultLabel(currentPanel.getTestStatistic());
            } else {
                labelTestResult.setVisible(false);
            }
            setButtonDescription("Start Test");
        }

    }

    private ArrayList<TestStatistic> getAvailableResults() {
        ArrayList<TestStatistic> results = new ArrayList<>();
        for (PanelProperties panel : panels) {
            results.add(panel.getTestStatistic());
        }
        return results;
    }

    private void setButtonDescription(String content) {
        buttonStart.setText(content);
    }

    private void setSideDescription(PanelType panelType) {
        ResourceBundle resources = ResourceBundle.getBundle("strings");
        switch (panelType) {
            case VISUAL:
                textAreaDescription.setText(resources.getString("visualDescription"));
                break;
            case AUDIO:
                textAreaDescription.setText(resources.getString("audioDescription"));
                break;
            case STIM:
                textAreaDescription.setText(resources.getString("stimuliDescription"));
                break;
            case CHOICE:
                textAreaDescription.setText(resources.getString("choiceDescription"));
                break;
            case RESULTS:
                textAreaDescription.setText(resources.getString("resultDescription"));
                break;
        }
    }

    private void setResultLabel(TestStatistic testStatistic) {
        if (testStatistic == null) {
            labelTestResult.setText("");
        } else {
            labelTestResult.setText("Average: " + testStatistic.getAverageTime() + " | Best: " + testStatistic.getBestTime() + " | Worst: " + testStatistic.getWorstTime());
        }
        labelTestResult.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PanelProperties panel = panels[panelTabs.getSelectedIndex()];
        if (panel.getPanelType() == PanelType.RESULTS) {
            currentPanel.start();
        } else {
            if (!panel.isRunning()) {
                startTest();
            } else {
                stopTest();
            }
        }
    }

    private void startTest() {
        setButtonDescription("Test Running | Abort Test");

        setResultLabel(null);

        //panelTabs.setEnabled(false);

        currentPanel.start();
    }

    private void stopTest() {
        setButtonDescription("Start Test");
        setResultLabel(currentPanel.getTestStatistic().hasResult() ? currentPanel.getTestStatistic() : null);
        currentPanel.stop();

        //panelTabs.setEnabled(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("button pressed");

        panels[panelTabs.getSelectedIndex()].onUserInput(e, new OnReactionCapturedListener() {
            @Override
            public void onReactionCaptured(long reactionTimeInMillis, boolean isFinalResult) {
                if (isFinalResult) {
                    setResultLabel(currentPanel.getTestStatistic());
                    stopTest();
                } else if (reactionTimeInMillis == -1) {
                    labelTestResult.setText("Too early");
                } else if (reactionTimeInMillis == -2) {
                    labelTestResult.setText("Wrong input");
                } else
                    labelTestResult.setText("Reaction Time: " + reactionTimeInMillis);

            }
        });

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
