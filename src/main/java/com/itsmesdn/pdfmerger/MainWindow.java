package com.itsmesdn.pdfmerger;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * Main application window class. Responsible for creating and managing the main UI window.
 */
public class MainWindow extends JFrame {
  private JButton addButton;
  private JButton removeButton;
  private JButton outputButton;
  private JButton mergeButton;
  private JList<String> fileList;
  private JTextField outputField;
  private JLabel statusLabel;
  private AppModel model;

  public MainWindow(AppModel model) {
    this.model = model;
    initializeWindow();
  }

  /**
   * Initializes the main window with all UI components.
   */
  private void initializeWindow() {
    setTitle("PDF Merger");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(500, 400);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout(10, 10));

    // Create all panels
    JPanel buttonPanel = createButtonPanel();
    JPanel centerPanel = createCenterPanel();
    JPanel outputPanel = createOutputPanel();
    JPanel statusPanel = createStatusPanel();

    // Add panels to frame
    add(buttonPanel, BorderLayout.NORTH);
    add(centerPanel, BorderLayout.CENTER);
    add(outputPanel, BorderLayout.SOUTH);
    add(statusPanel, BorderLayout.PAGE_END);
  }

  /**
   * Creates the button panel with action buttons.
   */
  private JPanel createButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    addButton = UiComponentFactory.createButton("Add PDF");
    removeButton = UiComponentFactory.createButton("Remove Selected");
    outputButton = UiComponentFactory.createButton("Select Output");
    mergeButton = UiComponentFactory.createButton("Merge PDFs");

    panel.add(addButton);
    panel.add(removeButton);
    panel.add(outputButton);
    panel.add(mergeButton);

    return panel;
  }

  /**
   * Creates the center panel with file list.
   */
  private JPanel createCenterPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    fileList = new JList<>(model.getFileListModel());
    fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    JScrollPane scrollPane = new JScrollPane(fileList);
    scrollPane.setBorder(BorderFactory.createTitledBorder("Input PDF Files"));

    panel.add(scrollPane, BorderLayout.CENTER);
    return panel;
  }

  /**
   * Creates the output file panel.
   */
  private JPanel createOutputPanel() {
    JPanel panel = new JPanel(new BorderLayout(5, 5));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JLabel label = UiComponentFactory.createLabel("Output File:");
    outputField = UiComponentFactory.createReadOnlyTextField();

    panel.add(label, BorderLayout.WEST);
    panel.add(outputField, BorderLayout.CENTER);

    return panel;
  }

  /**
   * Creates the status panel.
   */
  private JPanel createStatusPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

    statusLabel = UiComponentFactory.createLabel("Ready");
    panel.add(statusLabel, BorderLayout.WEST);

    return panel;
  }

  // Getters for UI components
  public JButton getAddButton() {
    return addButton;
  }

  public JButton getRemoveButton() {
    return removeButton;
  }

  public JButton getOutputButton() {
    return outputButton;
  }

  public JButton getMergeButton() {
    return mergeButton;
  }

  public JList<String> getFileList() {
    return fileList;
  }

  public JTextField getOutputField() {
    return outputField;
  }

  public JLabel getStatusLabel() {
    return statusLabel;
  }

  /**
   * Sets the enabled state of all action buttons.
   */
  public void setButtonsEnabled(boolean enabled) {
    addButton.setEnabled(enabled);
    removeButton.setEnabled(enabled);
    outputButton.setEnabled(enabled);
    mergeButton.setEnabled(enabled);
  }
}
