package com.omniview;

//import com.formdev.flatlaf.FlatDarkLaf;
import com.omniview.ui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
    // commented out because it was harder to convert it to jar
    /*  try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize theme. Using fallback.");
        } */
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}