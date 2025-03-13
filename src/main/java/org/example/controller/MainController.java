package org.example.controller;

import org.example.model.OpenedFile;
import org.example.model.interfaces.*;
import org.example.view.MainView;
import org.example.view.ViewConstants;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.regex.*;
import java.util.List;


import java.io.*;

import static javax.swing.SwingUtilities.getRootPane;
import static org.example.view.ViewConstants.SHOW_ERROR_DIALOG;

public class MainController {

  private final MainView view;
  private final JFileChooser fileChooser = new JFileChooser();
  private final UndoManager undoManager = new UndoManager();

  private boolean isUnsavedChanges = false;
  private String oldSearch = "";
  private int searchOccurrenceIndex = 0;
  private OpenedFile file = new OpenedFile("Untitled", null, null);

  private final List<int[]> searchOccurrences = new ArrayList<>();

  public MainController(MainView view) {
    this.view = view;

    handleListeners();
    undoRedoListeners();
    textAreaChanges();
    setTextAreaCaretListener();
    addEscKeyBinding();
  }

  private void handleListeners() {

    view.addWindowListener((SimpleWindowListener) _ -> closeApp());

    view.textArea().addKeyListener((SimpleKeyListener) _ -> textAreaChanges());
    view.textArea().addCaretListener(this::handleMenuEditCutCopy);

    view.menuFileNew().addActionListener(_ -> newTextFile());
    view.menuFileSave().addActionListener(_ -> saveTextFile(false));
    view.menuFileSaveAs().addActionListener(_ -> saveTextFile(true));
    view.menuFileOpen().addActionListener(_ -> openTextFile());
    view.menuFileExit().addActionListener(_ -> closeApp());

    view.menuEditSelAll().addActionListener(_ -> view.textArea().selectAll());
    view.menuEditPaste().addActionListener(_ -> pasteText());
    view.menuEditCopy().addActionListener(_ -> copyText());
    view.menuEditCut().addActionListener(_ -> cutText());
    view.menuEditDelete().addActionListener(_ -> deleteText());

    view.menuViewWrapText().addActionListener(_ -> {
      boolean isSelected = view.menuViewWrapText().isSelected();
      view.textArea().setLineWrap(isSelected);
      view.textArea().setWrapStyleWord(isSelected);
    });

    view.menuHelpAbout().addActionListener(_ -> ViewConstants.OPEN_ABOUT());

    view.menuEditSearch().addActionListener(_ -> toggleSearchVisibility());
    view.btnCloseSearch().addActionListener(_ -> setSearchVisibility(false));
    view.txtSearch().addActionListener(e -> executeSearch());
  }

  private void executeSearch() {
    String search = view.txtSearch().getText();
    String text = view.textArea().getText();

    if (text.isEmpty() && search.isEmpty()) {
      System.out.println("Search Easter Egg!");
      view.textArea().setText("abcdefabcdefabcdef");
      view.txtSearch().setText("abcdef");
    }

    if (search.isEmpty()) return;

    if (!view.cbCaseSensitive().isSelected()) {
      search = search.toLowerCase();
      text = text.toLowerCase();
    }

    Pattern pattern = Pattern.compile(search);
    Matcher matcher = pattern.matcher(text);

    // Reinicia as ocorrências quando a busca é alterada
    boolean isSearchChanged = !search.equals(oldSearch);
    oldSearch = search;

    if (isSearchChanged) {
      searchOccurrences.clear();
      while (matcher.find()) searchOccurrences.add(new int[]{matcher.start(), matcher.end()});
    }

    if (searchOccurrences.isEmpty()) {
      String msg = "Não foi possível encontrar: \"" + search + "\"";
      JOptionPane.showMessageDialog(view, msg, ViewConstants.APP_TITLE, JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    boolean isLastSearch = searchOccurrenceIndex >= searchOccurrences.size() - 1;


    // Se a pesquisa tiver mudado ou for a última seleção
    if (isSearchChanged || isLastSearch) searchOccurrenceIndex = 0;

    // Caso não tenha mudado o search e não seja a última seleção
    if (!isSearchChanged && !isLastSearch) searchOccurrenceIndex++;

    view.setOccurrences(searchOccurrenceIndex + 1, searchOccurrences.size());

    // Pega a ocorrência no índice atual e faz a seleção no JTextArea
    int[] item = searchOccurrences.get(searchOccurrenceIndex);
    int start = item[0], end = item[1];
    view.textArea().select(start, end);
  }

  private void addEscKeyBinding() {
    // Define a ação a ser executada quando a tecla for pressionada
    Action escAction = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        setSearchVisibility(false);
      }
    };

    // Associa a tecla ESC a ação dentro do JFrame
    getRootPane(view).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "ESC");
    getRootPane(view).getActionMap().put("ESC", escAction);
  }

  private void setTextAreaCaretListener() {
    Highlighter highlighter = view.textArea().getHighlighter();

    Color highlightColor = Color.BLUE;
    Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(highlightColor);

    view.textArea().addCaretListener(e -> {
      highlighter.removeAllHighlights();

      int start = e.getMark(), end = e.getDot();
      if (start == end) return;

      try {
        if (view.textArea().hasFocus()) return;
        highlighter.addHighlight(start, end, painter);
      } catch (BadLocationException er) {
        ViewConstants.SHOW_ERROR_DIALOG(view, er);
      }
    });
  }

  private void toggleSearchVisibility() {
    boolean isFocusInSearch = ViewConstants.isPanelOrChildFocused(view.searchPanel());
    boolean isVisible = view.isAncestorOf(view.searchPanel());

    if (!isFocusInSearch && isVisible) view.txtSearch().requestFocus();
    else setSearchVisibility(!isVisible);
  }

  private void setSearchVisibility(boolean visibility) {
    if (visibility) {
      view.add(view.searchPanel(), BorderLayout.NORTH);
      view.txtSearch().requestFocus();
    } else view.remove(view.searchPanel());
    view.revalidate();
    view.repaint();
  }

  private void undoRedoListeners() {
    view.textArea().getDocument().addUndoableEditListener(e -> {
      undoManager.addEdit(e.getEdit());
      updateUndoRedoButtons();
    });

    view.textArea().getInputMap().put(KeyStroke.getKeyStroke("control Z"), "undo");
    view.textArea().getInputMap().put(KeyStroke.getKeyStroke("control Y"), "redo");

    view.menuEditUndo().addActionListener(_ -> undoAction());
    view.menuEditRedo().addActionListener(_ -> redoAction());

    view.textArea().getActionMap().put("undo", (SimpleAction) _ -> undoAction());
    view.textArea().getActionMap().put("redo", (SimpleAction) _ -> redoAction());

    updateUndoRedoButtons();
  }

  private void undoAction() {
    if (!undoManager.canUndo()) return;
    try {
      undoManager.undo();
    } catch (CannotUndoException e) {
      SHOW_ERROR_DIALOG(view, e);
    }
    updateUndoRedoButtons();
  }

  private void redoAction() {
    if (!undoManager.canRedo()) return;
    try {
      undoManager.redo();
    } catch (CannotRedoException e) {
      SHOW_ERROR_DIALOG(view, e);
    }
    updateUndoRedoButtons();
  }

  private void updateUndoRedoButtons() {
    view.menuEditUndo().setEnabled(undoManager.canUndo());
    view.menuEditRedo().setEnabled(undoManager.canRedo());
  }

  private void handleMenuEditCutCopy(CaretEvent e) {
    int start = e.getDot(), end = e.getMark();

    boolean enabled = start != end;

    view.menuEditCut().setEnabled(enabled);
    view.menuEditCopy().setEnabled(enabled);
    view.menuEditDelete().setEnabled(enabled);
  }

  private void pasteText() {
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Clipboard clipboard = toolkit.getSystemClipboard();

    try {
      // Verifica se o clipboard contém um tipo de dado string
      if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
        String text = (String) clipboard.getData(DataFlavor.stringFlavor);

        JTextArea txa = view.textArea();

        // Se houver texto selecionado, substitui pela área selecionada
        if (txa.getSelectedText() != null) {
          txa.replaceSelection(text);
        } else {
          // Caso não haja seleção, insere no local do cursor
          int caretPosition = txa.getCaretPosition();
          txa.insert(text, caretPosition);
        }
      }

    } catch (UnsupportedFlavorException | IOException e) {
      String msg = "Erro ao colar o texto da área de transferência - " + e.getMessage();
      SHOW_ERROR_DIALOG(view, new Exception(msg));
    }
  }

  private void copyText() {
    String text = view.textArea().getSelectedText();
    if (text == null) return;
    StringSelection stringSelection = new StringSelection(text);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(stringSelection, null);
  }

  private void cutText() {
    copyText();
    view.textArea().replaceSelection("");
  }

  private void deleteText() {
    JTextArea txa = view.textArea();
    if (txa.getSelectedText() != null) txa.replaceSelection("");
  }

  private int unsavedChangesToSave() {
    int resp = JOptionPane.NO_OPTION;

    if (isUnsavedChanges) {
      String title = "Save File";
      String msg = "Deseja salvar as alterações de " + file.name() + "?";
      resp = JOptionPane.showConfirmDialog(view, msg, title, JOptionPane.YES_NO_CANCEL_OPTION);
      if (resp == JOptionPane.YES_OPTION) saveTextFile(false);
    }

    return resp;
  }

  private void newTextFile() {
    if (unsavedChangesToSave() == JOptionPane.CANCEL_OPTION) return;

    file = new OpenedFile("Untitled", null, null);
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
      assert cf : "CRITIAL ERROR: Ao criar o arquivo, foi verificado que ele já existe.";
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

  private void textAreaChanges() {
    String txt = view.textArea().getText();
    isUnsavedChanges = file.isUnsaved(txt);
    view.setTitle(file.name() + (isUnsavedChanges ? "*" : "") + " - " + ViewConstants.APP_TITLE);

    if (txt.isEmpty()) {
      view.menuEditCut().setEnabled(false);
      view.menuEditCopy().setEnabled(false);
      view.menuEditDelete().setEnabled(false);
    }
  }

  private void setSavedStatus() {
    isUnsavedChanges = false;
    view.setTitle(file.name() + " - " + ViewConstants.APP_TITLE);
  }

  private void closeApp() {
    if (unsavedChangesToSave() == JOptionPane.CANCEL_OPTION) return;
    view.dispose();
  }
}
