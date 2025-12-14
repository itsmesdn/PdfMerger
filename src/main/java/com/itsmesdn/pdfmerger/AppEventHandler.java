package com.itsmesdn.pdfmerger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Event handler class for application actions. Manages all button clicks and user interactions.
 */
public class AppEventHandler {
  private MainWindow window;
  private AppModel model;
  private PdfMergerService mergerService;

  public AppEventHandler(MainWindow window, AppModel model, PdfMergerService mergerService) {
    this.window = window;
    this.model = model;
    this.mergerService = mergerService;
  }

  /**
   * Initializes all event listeners.
   */
  public void initializeListeners() {
    setupAddButtonListener();
    setupRemoveButtonListener();
    setupOutputButtonListener();
    setupMergeButtonListener();
  }

  /**
   * Sets up the add PDF button listener.
   */
  private void setupAddButtonListener() {
    window.getAddButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        File initialDir = new File(System.getProperty("user.dir"));
        JFileChooser chooser = UiComponentFactory.createPdfOpenChooser(initialDir);
        int result = chooser.showOpenDialog(window);

        if (result == JFileChooser.APPROVE_OPTION) {
          File[] files = chooser.getSelectedFiles();
          model.addInputFiles(files);
          window.getStatusLabel().setText("✓ Added " + files.length + " file(s)");
        }
      }
    });
  }

  /**
   * Sets up the remove button listener.
   */
  private void setupRemoveButtonListener() {
    window.getRemoveButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int selectedIndex = window.getFileList().getSelectedIndex();
        if (selectedIndex != -1) {
          String removedFile = model.removeInputFile(selectedIndex);
          window.getStatusLabel().setText("✓ Removed: " + removedFile);
        } else {
          window.getStatusLabel().setText("⚠ Select a file to remove");
        }
      }
    });
  }

  /**
   * Sets up the output button listener.
   */
  private void setupOutputButtonListener() {
    window.getOutputButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        File initialDir = new File(System.getProperty("user.dir"));
        JFileChooser chooser = UiComponentFactory.createPdfSaveChooser(initialDir);
        int result = chooser.showSaveDialog(window);

        if (result == JFileChooser.APPROVE_OPTION) {
          String selectedPath = chooser.getSelectedFile().getAbsolutePath();
          selectedPath = mergerService.ensurePdfExtension(selectedPath);
          model.setOutputPath(selectedPath);
          window.getOutputField().setText(selectedPath);
          window.getStatusLabel().setText("✓ Output file selected");
        }
      }
    });
  }

  /**
   * Sets up the merge button listener.
   */
  private void setupMergeButtonListener() {
    window.getMergeButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (model.isEmpty()) {
          window.getStatusLabel().setText("✗ Error: No input files selected");
          JOptionPane.showMessageDialog(window, "Please select input PDFs.", "No Files",
              JOptionPane.WARNING_MESSAGE);
          return;
        }

        if (model.getOutputPath().isEmpty()) {
          window.getStatusLabel().setText("✗ Error: No output file selected");
          JOptionPane.showMessageDialog(window, "Please select output file.", "No Output",
              JOptionPane.WARNING_MESSAGE);
          return;
        }

        performMerge();
      }
    });
  }

  /**
   * Performs the PDF merge operation in a background thread.
   */
  private void performMerge() {
    window.setButtonsEnabled(false);
    window.getStatusLabel().setText("⏳ Merging PDFs...");

    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        mergerService.mergePDFs(model.getInputFiles(), new File(model.getOutputPath()));
        return null;
      }

      @Override
      protected void done() {
        try {
          get();
          window.getStatusLabel().setText("✓ PDFs merged successfully!");
          JOptionPane.showMessageDialog(window, "PDFs merged successfully!", "Success",
              JOptionPane.INFORMATION_MESSAGE);

          // Clear all fields after successful merge
          model.clear();
          window.getOutputField().setText("");

        } catch (Exception ex) {
          window.getStatusLabel().setText("✗ Error: " + ex.getMessage());
          JOptionPane.showMessageDialog(window, "Error merging PDFs: " + ex.getMessage(),
              "Merge Error", JOptionPane.ERROR_MESSAGE);
        } finally {
          window.setButtonsEnabled(true);
        }
      }
    };

    worker.execute();
  }
}
