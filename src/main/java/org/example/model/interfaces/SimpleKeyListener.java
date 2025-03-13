package org.example.model.interfaces;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@FunctionalInterface
public interface SimpleKeyListener extends KeyListener {
  void onKeyReleased(KeyEvent ignoredE);

  @Override
  default void keyTyped(KeyEvent e) {
  }

  @Override
  default void keyPressed(KeyEvent e) {
  }

  @Override
  default void keyReleased(KeyEvent e) {
    onKeyReleased(e);
  }
}
