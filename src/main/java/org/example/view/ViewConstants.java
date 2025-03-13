package org.example.view;

import javax.swing.*;
import java.awt.*;

public class ViewConstants {

  public static final String APP_TITLE = "Java Text Editor";

  public static final int APP_WIDTH = 800;
  public static final int APP_HEIGHT = 600;

  public static void OPEN_ABOUT() {
    AboutView v = new AboutView();
    v.setVisible(true);
  }

  public static void SHOW_ERROR_DIALOG(Component view, Exception e) {
    String errorType = e.getClass().getSimpleName();
    JOptionPane.showMessageDialog(view, e.getMessage(), "ERROR - " + errorType, JOptionPane.ERROR_MESSAGE);
  }

  public static boolean isPanelOrChildFocused(JPanel panel) {
    if (panel.isFocusOwner()) return true;
    for (Component c : panel.getComponents()) if (c.isFocusOwner()) return true;
    return false;
  }

}
