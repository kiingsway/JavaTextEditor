package org.example.view;

import javax.swing.*;
import java.awt.*;

public class ViewConstants {
  public static final String APP_TITLE = "Java Text Editor";

  public static void SHOW_ERROR_DIALOG(Component view, Exception e) {
    String errorType = e.getClass().getSimpleName();
    JOptionPane.showMessageDialog(view, e.getMessage(), "ERROR - " + errorType, JOptionPane.ERROR_MESSAGE);
  }
}
