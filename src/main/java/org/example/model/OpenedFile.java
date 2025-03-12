package org.example.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class OpenedFile {

  String name, path, originalText;

  public OpenedFile(String name, String path, String originalText) {
    this.name = name == null ? "" : name;
    this.path = path == null ? "" : path;
    this.originalText = originalText == null ? "" : originalText;
  }

  public boolean isNewFile() {
    return path == null || path.isEmpty();
  }

  public static String getStringFromFileContent(File selectedFile) throws IOException {
    StringBuilder fileContent = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
      String line;
      boolean firstLine = true;
      while ((line = reader.readLine()) != null) {
        if (!firstLine) {
          fileContent.append("\n");
        }
        fileContent.append(line);
        firstLine = false;
      }
    }

    return fileContent.toString();
  }

  public boolean isUnsaved(String text) {
    return !originalText.equals(text);
  }

  public void setOriginalText(String originalText) {
    this.originalText = originalText;
  }

  public String name() {
    return name;
  }

  @Override
  public String toString() {
    return String.format("name: %s, path: %s, originalText: %s", name, path, originalText);
  }

  public String path() {
    return path;
  }
}
