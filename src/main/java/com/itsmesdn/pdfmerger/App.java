package com.itsmesdn.pdfmerger;

import javax.swing.UIManager;
import javax.swing.SwingUtilities;

/**
 * Main entry point for the PDF Merger application. Sets up the application's look and feel and
 * launches the main window.
 */
public class App {

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    SwingUtilities.invokeLater(() -> launchApplication());
  }

  /**
   * Launches the application by initializing the main window and setting up event handlers.
   */
  private static void launchApplication() {
    // Initialize model and services
    AppModel model = new AppModel();
    PdfMergerService mergerService = new PdfMergerService();

    // Create main window
    MainWindow window = new MainWindow(model);

    // Setup event handlers
    AppEventHandler eventHandler = new AppEventHandler(window, model, mergerService);
    eventHandler.initializeListeners();

    // Display window
    window.setVisible(true);
  }
}

