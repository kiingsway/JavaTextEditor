package org.example.view;

import org.example.view.tabPolicy.MainFocusTraversalPolicy;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class MainView extends JFrame {

  private final JTextArea textArea = new JTextArea();

  // ----------------------------------- Search -----------------------------------
  private final JPanel searchPanel = new JPanel();
  private final JTextField txtSearch = new JTextField();
  private final JCheckBox cbCaseSensitive = new JCheckBox("Case Sensitive");
  private final JLabel lblOccurrences = new JLabel("0/0");
  private final JButton btnCloseSearch = new JButton("X");

  // -----------------------------------  Menu -----------------------------------
  private final JMenuBar menuBar = new JMenuBar();

  // -------------------------------- File Menu --------------------------------
  private final JMenuItem menuFileNew = createMenuItem("New", "control N");
  private final JMenuItem menuFileOpen = createMenuItem("Open", "control O");
  private final JMenuItem menuFileSave = createMenuItem("Save", "control S");
  private final JMenuItem menuFileSaveAs = createMenuItem("Save As", "control shift S");
  private final JMenuItem menuFileExit = new JMenuItem("Exit");

  // -------------------------------- Edit Menu --------------------------------
  private final JMenuItem menuEditUndo = createMenuItem("Undo", "control Z");
  private final JMenuItem menuEditRedo = createMenuItem("Redo", "control Y");
  private final JMenuItem menuEditCut = createMenuItem("Cut", "control X");
  private final JMenuItem menuEditCopy = createMenuItem("Copy", "control C");
  private final JMenuItem menuEditPaste = createMenuItem("Paste", "control V");
  private final JMenuItem menuEditSelAll = createMenuItem("Select All", "control A");
  private final JMenuItem menuEditDelete = createMenuItem("Delete", "DELETE");
  private final JMenuItem menuEditSearch = createMenuItem("Search", "control F");

  // -------------------------------- View Menu --------------------------------
  private final JCheckBoxMenuItem menuViewWrapText = new JCheckBoxMenuItem("Wrap Text");

  // -------------------------------- Help Menu --------------------------------
  private final JMenuItem menuHelpAbout = new JMenuItem("About");

  public MainView() {
    setTitle("Untitled - Java Text Editor");
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setSize(ViewConstants.APP_WIDTH, ViewConstants.APP_HEIGHT);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    setJMenuBar(menuBar);
    renderFileMenuBar();
    renderEditMenuBar();
    renderViewMenuBar();
    renderHelpMenuBar();
    renderContent();
  }

  private void renderFileMenuBar() {
    JMenu menu = new JMenu("File");

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

    menuEditCut.setEnabled(false);
    menuEditCopy.setEnabled(false);
    menuEditDelete.setEnabled(false);

    menu.add(menuEditUndo);
    menu.add(menuEditRedo);
    menu.addSeparator();
    menu.add(menuEditCut);
    menu.add(menuEditCopy);
    menu.add(menuEditPaste);
    menu.add(menuEditSelAll);
    menu.add(menuEditDelete);
    menu.addSeparator();
    menu.add(menuEditSearch);

    menuBar.add(menu);
  }

  private void renderViewMenuBar() {
    JMenu menu = new JMenu("View");
    menuViewWrapText.setSelected(false);
    menuViewWrapText.setAccelerator(KeyStroke.getKeyStroke("alt Z"));
    menu.add(menuViewWrapText);
    menuBar.add(menu);
  }

  private void renderHelpMenuBar() {
    JMenu menu = new JMenu("Help");
    menu.add(menuHelpAbout);
    menuBar.add(menu);
  }

  private void renderContent() {
    searchPanel.setLayout(new BorderLayout());

    JPanel eastPanel = new JPanel(new BorderLayout());
    JLabel lblSearch = new JLabel("Search:");

    cbCaseSensitive.setSelected(false);

    eastPanel.add(lblOccurrences, BorderLayout.WEST);
    eastPanel.add(cbCaseSensitive, BorderLayout.CENTER);
    eastPanel.add(btnCloseSearch, BorderLayout.EAST);

    searchPanel.add(lblSearch, BorderLayout.WEST);
    searchPanel.add(txtSearch, BorderLayout.CENTER);
    searchPanel.add(eastPanel, BorderLayout.EAST);

    setFocusTraversalPolicy(new MainFocusTraversalPolicy(txtSearch, cbCaseSensitive, btnCloseSearch, textArea));

    add(new JScrollPane(textArea), BorderLayout.CENTER);
  }

  public JTextArea textArea() {
    return textArea;
  }

  // ----------------------------------- Search -----------------------------------
  public JPanel searchPanel() {
    return searchPanel;
  }

  public JTextField txtSearch() {
    return txtSearch;
  }

  public JCheckBox cbCaseSensitive() {
    return cbCaseSensitive;
  }

  public void setOccurrences(int number, int total) {
    String text = number + "/" + total;
    lblOccurrences.setText(text);
  }

  public JButton btnCloseSearch() {
    return btnCloseSearch;
  }

  // -------------------------------- File Menu --------------------------------
  public JMenuItem menuFileNew() {
    return menuFileNew;
  }

  public JMenuItem menuFileOpen() {
    return menuFileOpen;
  }

  public JMenuItem menuFileSave() {
    return menuFileSave;
  }

  public JMenuItem menuFileSaveAs() {
    return menuFileSaveAs;
  }

  public JMenuItem menuFileExit() {
    return menuFileExit;
  }

  // -------------------------------- Edit Menu --------------------------------
  public JMenuItem menuEditUndo() {
    return menuEditUndo;
  }

  public JMenuItem menuEditRedo() {
    return menuEditRedo;
  }

  public JMenuItem menuEditCut() {
    return menuEditCut;
  }

  public JMenuItem menuEditCopy() {
    return menuEditCopy;
  }

  public JMenuItem menuEditPaste() {
    return menuEditPaste;
  }

  public JMenuItem menuEditSelAll() {
    return menuEditSelAll;
  }

  public JMenuItem menuEditDelete() {
    return menuEditDelete;
  }

  public JMenuItem menuEditSearch() {
    return menuEditSearch;
  }

  // -------------------------------- View Menu --------------------------------
  public JCheckBoxMenuItem menuViewWrapText() {
    return menuViewWrapText;
  }

  // -------------------------------- Help Menu --------------------------------
  public JMenuItem menuHelpAbout() {
    return menuHelpAbout;
  }

  // -------------------------------- Helpers --------------------------------
  private JMenuItem createMenuItem(String text, String accelerator) {
    JMenuItem menuItem = new JMenuItem(text);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(accelerator));
    return menuItem;
  }
}
