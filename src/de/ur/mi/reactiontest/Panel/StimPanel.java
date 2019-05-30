package de.ur.mi.reactiontest.Panel;

import de.ur.mi.reactiontest.Enums.PanelType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.TimerTask;

public class StimPanel extends PanelProperties {

    private Color expectedColor;
    private Color currentColor;
    private static final Color[] colorPool = {Color.GREEN, Color.RED, Color.BLUE, Color.MAGENTA};
    private JLabel shortDescription;

    public StimPanel(JPanel jPanel, JLabel shortDescription) {
        super(PanelType.STIM, jPanel);
        this.shortDescription = shortDescription;
        shortDescription.setText("Press when you see the color shown right now");
    }

    @Override
    public void start() {
        initScheduler();
        if(!isRunning){
            isRunning = true;
            expectedColor = colorPool[random.nextInt(colorPool.length)];
            jPanel.setBackground(expectedColor);

            shortDescription.setVisible(true);
            scheduler.schedule(new TimerTask() {
                @Override
                public void run() {
                    jPanel.setBackground(Color.WHITE);
                    shortDescription.setVisible(false);
                    runStimTest();
                }
            }, 5000);
        }else{
            runStimTest();
        }
    }

    @Override
    protected boolean checkInputValidity(KeyEvent keyEvent) {
        return currentColor == expectedColor;
    }

    @Override
    public void stop() {
        reset();
        expectedColor = null;
        jPanel.setBackground(Color.WHITE);
        shortDescription.setVisible(false);
    }

    private void runStimTest(){
        if (!isRunning) {
            return;
        }
        scheduler.schedule(new TimerTask() {
            @Override
            public void run() {
                Color newColor = getPseudoRandomColor();
                if(newColor != expectedColor){
                    //re-roll once for higher chance at expected color
                    newColor = getPseudoRandomColor();
                }
                currentColor = newColor;
                jPanel.setBackground(currentColor);
                timer.startTimer();
                if(currentColor != expectedColor){
                    start();
                }
            }
        }, getRandomDelay() / 2);
    }

    private Color getPseudoRandomColor(){
        Color newColor = colorPool[random.nextInt(colorPool.length)];
        while(newColor == currentColor){
            newColor = colorPool[random.nextInt(colorPool.length)];
        }
        return newColor;
    }
}
