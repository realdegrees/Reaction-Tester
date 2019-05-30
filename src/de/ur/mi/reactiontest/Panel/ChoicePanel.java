package de.ur.mi.reactiontest.Panel;

import de.ur.mi.reactiontest.Enums.PanelType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.TimerTask;

public class ChoicePanel extends PanelProperties {

    private int expectedButton;

    public ChoicePanel(JPanel jPanel) {
        super(PanelType.CHOICE, jPanel);
    }

    @Override
    public void start() {
        isRunning = true;
        initScheduler();
        runChoiceTest();
    }

    @Override
    protected boolean checkInputValidity(KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == expectedButton;
    }

    @Override
    public void stop() {
        for (Component c : jPanel.getComponents()) {
            c.setBackground(Color.WHITE);
        }
        expectedButton = -1;
        reset();
    }

    private void runChoiceTest() {
        if (!isRunning) {
            return;
        }

        int randomIndex = getRandomIndex();
        expectedButton = getRandomButton(randomIndex);

        Component activeComponent = componentFromChar((char) expectedButton);

        for (Component c : jPanel.getComponents()) {
            c.setBackground(Color.RED);
        }

        scheduler.schedule(new TimerTask() {
            @Override
            public void run() {
                activeComponent.setBackground(Color.GREEN);
                timer.startTimer();
            }
        }, getRandomDelay());
    }

    private int getRandomIndex() {
        return random.nextInt(4);
    }

    private int getRandomButton(int i) {
        switch (i) {
            case 0:
                return KeyEvent.VK_C;
            case 1:
                return KeyEvent.VK_V;
            case 2:
                return KeyEvent.VK_N;
            case 3:
                return KeyEvent.VK_M;
        }
        return -1;
    }

    private Component componentFromChar(char ch) {
        Component[] components = jPanel.getComponents();
        for (Component c : components) {
            if (c.getName() != null && c.getName().equals(Character.toString(ch))) {
                return c;
            }
        }
        return null;
    }
}
