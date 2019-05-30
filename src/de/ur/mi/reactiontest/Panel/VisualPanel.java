package de.ur.mi.reactiontest.Panel;

import de.ur.mi.reactiontest.Enums.PanelType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.TimerTask;

public class VisualPanel extends PanelProperties {
    public VisualPanel(JPanel jPanel) {
        super(PanelType.VISUAL, jPanel);
    }

    @Override
    public void start() {
        isRunning = true;
        initScheduler();
        runVisualTest();
    }

    @Override
    protected boolean checkInputValidity(KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == KeyEvent.VK_ENTER || keyEvent.getKeyCode() == KeyEvent.VK_SPACE;
    }

    @Override
    public void stop() {
        jPanel.setBackground(Color.WHITE);
        reset();
    }

    private void runVisualTest() {
        if (!isRunning) {
            return;
        }
        jPanel.setBackground(Color.GREEN);
        scheduler.schedule(new TimerTask() {
            @Override
            public void run() {
                jPanel.setBackground(Color.RED);
                timer.startTimer();
            }
        }, getRandomDelay());
    }
}
