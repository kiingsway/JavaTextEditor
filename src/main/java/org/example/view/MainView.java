package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainView extends JFrame {

  private final JMenuBar menuBar = new JMenuBar();

  private final JMenuItem menuFileNew = new JMenuItem("New");
  private final JMenuItem menuFileOpen = new JMenuItem("Open");
  private final JMenuItem menuFileSave = new JMenuItem("Save");
  private final JMenuItem menuFileSaveAs = new JMenuItem("Save As");
  private final JMenuItem menuFileExit = new JMenuItem("Exit");

  private final JMenuItem menuEditUndo = new JMenuItem("Undo");
  private final JMenuItem menuEditRedo = new JMenuItem("Redo");
  private final JMenuItem menuEditCut = new JMenuItem("Cut");
  private final JMenuItem menuEditCopy = new JMenuItem("Copy");
  private final JMenuItem menuEditPaste = new JMenuItem("Paste");
  private final JMenuItem menuEditSelAll = new JMenuItem("Select All");

  private final JCheckBoxMenuItem menuViewWrapText = new JCheckBoxMenuItem("Wrap Text");

  private final JMenuItem menuHelpAbout = new JMenuItem("About");

  private final JTextArea textArea = new JTextArea();

  public MainView() {
    setTitle("Untitled - Java Text Editor");
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);

    setJMenuBar(menuBar);

    renderFileMenuBar();
    renderEditMenuBar();
    renderViewMenuBar();
    renderHelpMenuBar();

    renderContent();

    setVisible(true);
  }

  private void renderContent() {
    JScrollPane scrollPane = new JScrollPane(textArea);

    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
  }

  private void renderFileMenuBar() {
    JMenu menu = new JMenu("File");

    menuFileNew.setAccelerator(KeyStroke.getKeyStroke("control N"));
    menuFileOpen.setAccelerator(KeyStroke.getKeyStroke("control O"));
    menuFileSave.setAccelerator(KeyStroke.getKeyStroke("control S"));
    menuFileSaveAs.setAccelerator(KeyStroke.getKeyStroke("control shift S"));

    menu.add(menuFileNew);
    menu.add(menuFileOpen);
    menu.add(menuFileSave);
    menu.add(menuFileSaveAs);
    menu.addSeparator();
    menu.add(menuFileExit);

    menuBar.add(menu);
  }

  private void renderEditMenuBar() {
    JMenu menu = new JMenu("Edit");

    menuEditUndo.setEnabled(false);
    menuEditRedo.setEnabled(false);
    menuEditCut.setEnabled(false);
    menuEditCopy.setEnabled(false);
    menuEditPaste.setEnabled(false);
    menuEditSelAll.setEnabled(false);

//    menu.add(menuEditUndo);
//    menu.add(menuEditRedo);
//    menu.addSeparator();
//    menu.add(menuEditCut);
//    menu.add(menuEditCopy);
//    menu.add(menuEditPaste);
//    menu.add(menuEditSelAll);

    //menuBar.add(menu);
  }

  private void renderViewMenuBar() {
    JMenu menu = new JMenu("View");

    menuViewWrapText.setSelected(false);
    menu.add(menuViewWrapText);

    menuBar.add(menu);
  }

  private void renderHelpMenuBar() {
    JMenu menu = new JMenu("Help");

    menuHelpAbout.setEnabled(false);

    menu.add(menuHelpAbout);

    //menuBar.add(menu);
  }

  public JMenuItem menuFileNew() {
    return menuFileNew;
  }

  public JMenuItem menuFileSave() {
    return menuFileSave;
  }

  public JMenuItem menuFileOpen() {
    return menuFileOpen;
  }

  public JMenuItem menuFileExit() {
    return menuFileExit;
  }

  public JCheckBoxMenuItem menuViewWrapText() {
    return menuViewWrapText;
  }

  public JTextArea textArea() {
    return textArea;
  }

  public Map<String, JMenuItem> getMenuItems() {
    Map<String, JMenuItem> menuItems = new HashMap<>();

    menuItems.put("FileNew", menuFileNew);
    menuItems.put("FileOpen", menuFileOpen);
    menuItems.put("FileSave", menuFileSave);
    menuItems.put("FileSaveAs", menuFileSaveAs);
    menuItems.put("FileExit", menuFileExit);

    menuItems.put("ViewWrapText", menuViewWrapText);

    menuItems.put("EditUndo", menuEditUndo);
    menuItems.put("EditRedo", menuEditRedo);
    menuItems.put("EditCut", menuEditCut);
    menuItems.put("EditCopy", menuEditCopy);
    menuItems.put("EditPaste", menuEditPaste);
    menuItems.put("EditSelectAll", menuEditSelAll);

    menuItems.put("HelpAbout", menuHelpAbout);

    return menuItems;
  }

  public JMenuItem menuFileSaveAs() {
    return menuFileSaveAs;
  }
}
