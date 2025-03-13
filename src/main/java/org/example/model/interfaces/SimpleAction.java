package org.example.model.interfaces;

import javax.swing.*;
import java.awt.event.ActionEvent;

@FunctionalInterface
public interface SimpleAction extends Action {
  void onAction(ActionEvent e);

  @Override
  default void actionPerformed(ActionEvent e) {
    onAction(e);
  }

  // Métodos padrão para evitar implementações desnecessárias
  @Override
  default Object getValue(String key) { return null; }

  @Override
  default void putValue(String key, Object value) {}

  @Override
  default void setEnabled(boolean b) {}

  @Override
  default boolean isEnabled() { return true; }

  @Override
  default void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {}

  @Override
  default void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {}
}
