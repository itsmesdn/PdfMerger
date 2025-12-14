package com.itsmesdn.pdfmerger;

import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.io.File;

/**
 * UI component builder class. Responsible for creating and configuring all UI components.
 */
public class UiComponentFactory {

  /**
   * Creates a styled file chooser for opening PDFs.
   *
   * @param initialDirectory Initial directory to open
   * @return Configured JFileChooser for PDF selection
   */
  public static JFileChooser createPdfOpenChooser(File initialDirectory) {
    JFileChooser chooser = new JFileChooser(initialDirectory);
    chooser.setMultiSelectionEnabled(true);
    chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
    return chooser;
  }

  /**
   * Creates a styled file chooser for saving PDFs.
   *
   * @param initialDirectory Initial directory to save
   * @return Configured JFileChooser for PDF save
   */
  public static JFileChooser createPdfSaveChooser(File initialDirectory) {
    JFileChooser chooser = new JFileChooser(initialDirectory);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
    return chooser;
  }

  /**
   * Creates a button with standard styling.
   *
   * @param text Button text
   * @return Configured JButton
   */
  public static JButton createButton(String text) {
    return new JButton(text);
  }

  /**
   * Creates a styled label for display.
   *
   * @param text Label text
   * @return Configured JLabel
   */
  public static JLabel createLabel(String text) {
    return new JLabel(text);
  }

  /**
   * Creates a read-only text field for file paths.
   *
   * @return Configured JTextField
   */
  public static JTextField createReadOnlyTextField() {
    JTextField field = new JTextField();
    field.setEditable(false);
    return field;
  }
}
