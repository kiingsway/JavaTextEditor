package org.example.view;

import javax.swing.*;
import java.awt.*;

public class AboutView extends JFrame {

  private final GridBagConstraints gbc = new GridBagConstraints();

  public AboutView() {
    setTitle("About");
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setSize(ViewConstants.APP_WIDTH, ViewConstants.APP_HEIGHT);
    setLocationRelativeTo(null);
    setLayout(new GridBagLayout());

    gbc.gridx = 0;
    gbc.gridy = 0;
    renderInfo();
  }

  private void renderInfo() {
    JLabel lblAuthor = new JLabel("Author: King Sway");
    JLabel lblVersion = new JLabel("Version 1.0");

    add(lblAuthor, gbc);
    gbc.gridy++;
    add(lblVersion, gbc);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(ViewConstants::OPEN_ABOUT);
  }
}
