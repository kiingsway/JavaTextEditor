package org.example.view.tabPolicy;

import java.awt.*;

public class MainFocusTraversalPolicy extends FocusTraversalPolicy {
  private final Component txtSearch;
  private final Component cbCaseSensitive;
  private final Component btnCloseSearch;
  private final Component textArea;

  public MainFocusTraversalPolicy(Component txtSearch, Component cbCaseSensitive, Component btnCloseSearch, Component textArea) {
    this.txtSearch = txtSearch;
    this.cbCaseSensitive = cbCaseSensitive;
    this.btnCloseSearch = btnCloseSearch;
    this.textArea = textArea;
  }

  @Override
  public Component getComponentAfter(Container container, Component component) {
    if (component == txtSearch) {
      return cbCaseSensitive;
    } else if (component == cbCaseSensitive) {
      return btnCloseSearch;
    } else if (component == btnCloseSearch) {
      return txtSearch;
    }
    return null;
  }

  @Override
  public Component getComponentBefore(Container container, Component component) {
    if (component == txtSearch) {
      return btnCloseSearch;
    } else if (component == cbCaseSensitive) {
      return txtSearch;
    } else if (component == btnCloseSearch) {
      return cbCaseSensitive;
    }
    return null;
  }

  @Override
  public Component getDefaultComponent(Container container) {
    return txtSearch;
  }

  @Override
  public Component getLastComponent(Container container) {
    return textArea;
  }

  @Override
  public Component getFirstComponent(Container container) {
    return txtSearch;
  }
}
