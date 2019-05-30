package de.ur.mi.reactiontest.Panel;

import de.ur.mi.reactiontest.Config.Config;
import de.ur.mi.reactiontest.Enums.PanelType;
import de.ur.mi.reactiontest.Helper.TestStatistic;
import de.ur.mi.reactiontest.Interfaces.OnReactionCapturedListener;
import de.ur.mi.reactiontest.Helper.Timer;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public abstract class PanelProperties {

    protected Timer timer = new Timer();
    protected java.util.Timer scheduler;
    protected Random random = new Random();

    private TestStatistic currentTestStatistic;
    private TestStatistic lastTestStatistic;

    private PanelType panelType;
    protected boolean isRunning = false;
    protected JPanel jPanel;

    protected PanelProperties(PanelType panelType, JPanel jPanel) {
        this.panelType = panelType;
        this.jPanel = jPanel;
        this.currentTestStatistic = new TestStatistic(panelType);
        lastTestStatistic = currentTestStatistic.copyOf();
        initScheduler();
    }

    public PanelType getPanelType() {
        return panelType;
    }

    public TestStatistic getTestStatistic() {
        return lastTestStatistic;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public abstract void start();

    public void onUserInput(KeyEvent keyEvent, OnReactionCapturedListener onReactionCapturedListener) {
        if (!isRunning) {
            return;
        }
        if (!timer.isRunning()) {
            onReactionCapturedListener.onReactionCaptured(-1, false);
            stopScheduledTasks();
            timer.stopTimer();
            start();
            return;
        } else if (!checkInputValidity(keyEvent)) {
            onReactionCapturedListener.onReactionCaptured(-2, false);
            stopScheduledTasks();
            timer.stopTimer();
            start();
            return;
        }
        long currentTime = timer.getTime();
        currentTestStatistic.setNextTime(currentTime);
        timer.stopTimer();
        onReactionCapturedListener.onReactionCaptured(currentTime, false);
        if (currentTestStatistic.getCurrentPass() < Config.PASSES_PER_TEST) {
            start();
        } else {
            isRunning = false;
            lastTestStatistic = currentTestStatistic.copyOf();
            onReactionCapturedListener.onReactionCaptured(currentTestStatistic.getAverageTime(), true);
        }
    }

    protected abstract boolean checkInputValidity(KeyEvent keyEvent);

    protected void stopScheduledTasks() {
        scheduler.cancel();
        scheduler.purge();
    }

    protected void initScheduler() {
        scheduler = new java.util.Timer();
    }


    protected void reset() {
        currentTestStatistic.reset();
        timer.stopTimer();
        isRunning = false;
        stopScheduledTasks();
    }

    public abstract void stop();

    protected long getRandomDelay() {
        return Config.DELAY_MIN_MILLIS + random.nextInt(Config.DELAY_MAX_MILLIS - Config.DELAY_MIN_MILLIS);
    }


}
