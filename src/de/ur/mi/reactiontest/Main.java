package de.ur.mi.reactiontest;

import javax.swing.*;

public class Main extends JFrame {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        SwingUtilities.invokeLater(() -> {
            ReactionTestGUI reactionTestGUI = new ReactionTestGUI();
            reactionTestGUI.setVisible(true);
        });
    }
}
