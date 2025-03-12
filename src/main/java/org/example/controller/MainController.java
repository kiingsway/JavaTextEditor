package org.example.controller;

import org.example.model.OpenedFile;
import org.example.view.MainView;
import org.example.view.ViewConstants;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;

import java.io.*;

import static org.example.view.ViewConstants.SHOW_ERROR_DIALOG;

public class MainController {

  private final MainView view;
  private final JFileChooser fileChooser = new JFileChooser();

  private boolean isUnsavedChanges = false;
  private OpenedFile file = new OpenedFile("Untitled", null, null);

  public MainController(MainView view) {
    this.view = view;

    handleListeners();
  }

  private void handleListeners() {

    view.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        closeApp();
      }
    });

    view.menuFileNew().addActionListener(_ -> newTextFile());
    view.menuFileSave().addActionListener(_ -> saveTextFile(false));
    view.menuFileSaveAs().addActionListener(_ -> saveTextFile(true));
    view.menuFileOpen().addActionListener(_ -> openTextFile());
    view.menuFileExit().addActionListener(_ -> closeApp());

    view.textArea().addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        compareSavedChanges();
      }
    });

    view.menuViewWrapText().addActionListener(_ -> {
      boolean isSelected = view.menuViewWrapText().isSelected();
      view.textArea().setLineWrap(isSelected);
      view.textArea().setWrapStyleWord(isSelected);
    });
  }

  private int unsavedChangesToSave() {
    if (isUnsavedChanges) {
      String title = "Save File";
      String msg = "Deseja salvar seu arquivo?";
      int resp = JOptionPane.showConfirmDialog(view, msg, title, JOptionPane.YES_NO_CANCEL_OPTION);
      if (resp == JOptionPane.YES_OPTION) saveTextFile(false);
      return resp;
    }

    return JOptionPane.NO_OPTION;
  }

  private void newTextFile() {
    if (unsavedChangesToSave() == JOptionPane.CANCEL_OPTION) return;

    file = new OpenedFile("Untitled", null, null);
    view.setTitle(file.name() + " - " + ViewConstants.APP_TITLE);
    view.textArea().setText("");
    setSavedStatus();
  }

  private void openTextFile() {
    if (unsavedChangesToSave() == JOptionPane.CANCEL_OPTION) return;

    fileChooser.setDialogTitle("Open Text File");
    fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (.txt)", "txt"));

    int userSelection = fileChooser.showOpenDialog(view);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
      try {
        File selectedFile = fileChooser.getSelectedFile();
        String name = selectedFile.getName();
        String path = selectedFile.getAbsolutePath();
        String oText = OpenedFile.getStringFromFileContent(selectedFile);

        file = new OpenedFile(name, path, oText);
        view.textArea().setText(oText);
        setSavedStatus();

      } catch (Exception e) {
        SHOW_ERROR_DIALOG(view, e);
      }
    }
  }

  private void saveTextFile(boolean saveAs) {
    fileChooser.setDialogTitle("Save file");
    fileChooser.setSelectedFile(new File(file.name()));

    if (file.isNewFile() || saveAs) {
      int userSelection = fileChooser.showSaveDialog(view);

      if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();

        if (!fileToSave.getName().endsWith(".txt")) {
          // Garante que o arquivo tenha a extensão .txt
          fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
        }

        if (fileToSave.exists()) {
          String title = ViewConstants.APP_TITLE;
          String msg = "O arquivo já existe. Deseja sobrescrevê-lo?";

          int response = JOptionPane.showConfirmDialog(view, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
          if (response == JOptionPane.NO_OPTION) return;
        } else {
          createFileContent(fileToSave);
          return;
        }

        saveFileContent(fileToSave);
        file = new OpenedFile(fileToSave.getName(), fileToSave.getAbsolutePath(), view.textArea().getText());
        setSavedStatus();
      }
    } else {
      saveFileContent(new File(file.path()));
      file.setOriginalText(view.textArea().getText());
      setSavedStatus();
    }
  }

  private void createFileContent(File fileToSave) {
    try {
      boolean cf = fileToSave.createNewFile();
      assert cf;
    } catch (Exception e) {
      SHOW_ERROR_DIALOG(view, e);
    }
  }

  private void saveFileContent(File fileToSave) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
      writer.write(view.textArea().getText());
    } catch (IOException e) {
      SHOW_ERROR_DIALOG(view, e);
    }
  }

  private void compareSavedChanges() {
    isUnsavedChanges = file.isUnsaved(view.textArea().getText());

    view.setTitle(file.name() + (isUnsavedChanges ? "*" : "") + " - " + ViewConstants.APP_TITLE);
  }

  private void setSavedStatus() {
    isUnsavedChanges = false;
    view.setTitle(file.name() + " - " + ViewConstants.APP_TITLE);
  }

  private void closeApp() {
    if (!isUnsavedChanges) view.dispose();

    else {
      String msg = "Deseja salvar as alterações de " + file.name() + "?";
      String title = ViewConstants.APP_TITLE;
      int response = JOptionPane.showConfirmDialog(view, msg, title, JOptionPane.YES_NO_CANCEL_OPTION);

      if (response == JOptionPane.CANCEL_OPTION) return;
      else if (response == JOptionPane.YES_OPTION) saveTextFile(false);

      view.dispose();
    }
  }
}
