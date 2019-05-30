package de.ur.mi.reactiontest.Panel;

import de.ur.mi.reactiontest.Enums.PanelType;
import de.ur.mi.reactiontest.Helper.TestStatistic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ResultPanel extends PanelProperties {

    private JTextArea resultText;
    private ArrayList<TestStatistic> currentResults;

    private boolean exported = false;

    public ResultPanel(JPanel jPanel, JTextArea resultText) {
        super(PanelType.RESULTS, jPanel);
        this.resultText = resultText;
    }

    @Override
    public void start() {
        if(currentResults != null && listHasValidResults(currentResults)){
            String id = String.valueOf(System.currentTimeMillis() / 1000);
            try (PrintWriter o = new PrintWriter("reaction_test_result_" + id + ".txt")){
                resultText.append("\n\n------ Successfully exported data to: reaction_test_result_" + id + ".txt ------");
                o.println(resultText.getText());
                currentResults = null;
                exported = true;
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }else if(!exported){
            resultText.append("\n--- Nothing to export! Complete one or multiple tests to find your data here! ---");
        }else{
            resultText.append("\n--- You already exported this! ---");
        }
    }

    @Override
    protected boolean checkInputValidity(KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void stop() {
        reset();
    }

    public void listResults(ArrayList<TestStatistic> results) {
        exported = false;
        currentResults = results;
        resultText.setVisible(true);
        if (results == null || !listHasValidResults(results)) {
            resultText.setText("After you have completed one or multiple tests your results will show here!");
            return;
        } else {
            resultText.setText("");
        }
        for (TestStatistic statistic : results) {
            if (statistic.hasResult()) {
                String testName = statistic.getTestType().toString();
                String averageTime = "Average Time: " + statistic.getAverageTime() + "ms\n";
                String bestTime = "Best Time: " + statistic.getBestTime() + "ms\n";
                String worstTime = "Worst Time: " + statistic.getWorstTime() + "ms\n";
                resultText.append("\n--- " + testName + " ---\n");
                resultText.append(averageTime);
                resultText.append(bestTime);
                resultText.append(worstTime);
            }
        }
    }

    private boolean listHasValidResults(ArrayList<TestStatistic> results){
        for(TestStatistic statistic : results){
            if(statistic.hasResult()){
                return true;
            }
        }
        return false;
    }
}
