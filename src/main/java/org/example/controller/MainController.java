package org.example.controller;

import org.example.view.MainView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainController {
  private final MainView view;

  public MainController(MainView view) {
    this.view = view;

    handleListeners();
  }

  private void handleListeners() {
    view.menuFileExit().addActionListener(_ -> closeApp());

    view.menuViewWrapText().setSelected(false);
    view.menuViewWrapText().addActionListener(_ -> {
      view.textArea().setLineWrap(view.menuViewWrapText().isSelected());
      view.textArea().setWrapStyleWord(view.menuViewWrapText().isSelected());
    });

    view.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        closeApp();
      }
    });
  }

  private void closeApp() {
    String msg = "Tem certeza que deseja fechar?";
    String title = "Close Java Text Editor";
    int response = JOptionPane.showConfirmDialog(view, msg, title, JOptionPane.YES_NO_OPTION);

    if (response == JOptionPane.YES_OPTION) view.dispose();
  }
}
