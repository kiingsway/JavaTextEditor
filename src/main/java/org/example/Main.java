package org.example;

import org.example.controller.MainController;
import org.example.view.MainView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView view = new MainView();
            new MainController(view);
            view.setVisible(true);
        });
    }
}