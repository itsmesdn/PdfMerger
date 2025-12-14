package com.itsmesdn.pdfmerger;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
  private JFrame frame;
  private DefaultListModel<String> fileListModel;
  private JList<String> fileList;
  private JTextField outputField;
  private JLabel statusLabel;
  private JButton addButton;
  private JButton removeButton;
  private JButton outputButton;
  private JButton mergeButton;
  private List<File> inputFiles;

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    SwingUtilities.invokeLater(() -> new App().createAndShowGUI());
  }

  private void createAndShowGUI() {
    frame = new JFrame("PDF Merger");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500, 400);
    frame.setLocationRelativeTo(null);
    frame.setLayout(new BorderLayout(10, 10));

    inputFiles = new ArrayList<>();

    // Panel for buttons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    addButton = new JButton("Add PDF");
    removeButton = new JButton("Remove Selected");
    outputButton = new JButton("Select Output");
    mergeButton = new JButton("Merge PDFs");

    buttonPanel.add(addButton);
    buttonPanel.add(removeButton);
    buttonPanel.add(outputButton);
    buttonPanel.add(mergeButton);

    // List for selected files
    fileListModel = new DefaultListModel<>();
    fileList = new JList<>(fileListModel);
    fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(fileList);
    scrollPane.setBorder(BorderFactory.createTitledBorder("Input PDF Files"));

    // Output field
    JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
    outputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    outputPanel.add(new JLabel("Output File:"), BorderLayout.WEST);
    outputField = new JTextField();
    outputField.setEditable(false);
    outputPanel.add(outputField, BorderLayout.CENTER);

    // Status panel
    JPanel statusPanel = new JPanel(new BorderLayout());
    statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
    statusLabel = new JLabel("Ready");
    statusPanel.add(statusLabel, BorderLayout.WEST);

    frame.add(buttonPanel, BorderLayout.NORTH);
    frame.add(scrollPane, BorderLayout.CENTER);
    frame.add(outputPanel, BorderLayout.SOUTH);
    frame.add(statusPanel, BorderLayout.PAGE_END);

    // Action listeners
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        chooser.setMultiSelectionEnabled(true);
        chooser
            .setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
        int result = chooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
          File[] files = chooser.getSelectedFiles();
          for (File file : files) {
            inputFiles.add(file);
            fileListModel.addElement(file.getName());
          }
          statusLabel.setText("✓ Added " + files.length + " file(s)");
        }
      }
    });

    removeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int selectedIndex = fileList.getSelectedIndex();
        if (selectedIndex != -1) {
          String removedFile = fileListModel.get(selectedIndex);
          inputFiles.remove(selectedIndex);
          fileListModel.remove(selectedIndex);
          statusLabel.setText("✓ Removed: " + removedFile);
        } else {
          statusLabel.setText("⚠ Select a file to remove");
        }
      }
    });

    outputButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser
            .setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
        int result = chooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
          String selectedFileName = chooser.getSelectedFile().getAbsolutePath();
          if (!selectedFileName.endsWith(".pdf")) {
            selectedFileName += ".pdf";
          }
          outputField.setText(selectedFileName);
          statusLabel.setText("✓ Output file selected");
        }
      }
    });

    mergeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (inputFiles.isEmpty()) {
          statusLabel.setText("✗ Error: No input files selected");
          JOptionPane.showMessageDialog(frame, "Please select input PDFs.", "No Files",
              JOptionPane.WARNING_MESSAGE);
          return;
        }
        String outputPath = outputField.getText();
        if (outputPath.isEmpty()) {
          statusLabel.setText("✗ Error: No output file selected");
          JOptionPane.showMessageDialog(frame, "Please select output file.", "No Output",
              JOptionPane.WARNING_MESSAGE);
          return;
        }

        // Disable all buttons during merge
        addButton.setEnabled(false);
        removeButton.setEnabled(false);
        outputButton.setEnabled(false);
        mergeButton.setEnabled(false);
        statusLabel.setText("⏳ Merging PDFs...");

        // Execute merge in background thread
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
          @Override
          protected Void doInBackground() throws Exception {
            mergePDFs(inputFiles, new File(outputPath));
            return null;
          }

          @Override
          protected void done() {
            try {
              get();
              statusLabel.setText("✓ PDFs merged successfully!");
              JOptionPane.showMessageDialog(frame, "PDFs merged successfully!", "Success",
                  JOptionPane.INFORMATION_MESSAGE);

              // Auto-clear fields after successful merge
              inputFiles.clear();
              fileListModel.clear();
              outputField.setText("");

            } catch (Exception ex) {
              statusLabel.setText("✗ Error: " + ex.getMessage());
              JOptionPane.showMessageDialog(frame, "Error merging PDFs: " + ex.getMessage(),
                  "Merge Error", JOptionPane.ERROR_MESSAGE);
            } finally {
              // Re-enable buttons after merge completes
              addButton.setEnabled(true);
              removeButton.setEnabled(true);
              outputButton.setEnabled(true);
              mergeButton.setEnabled(true);
            }
          }
        };

        worker.execute();
      }
    });

    frame.setVisible(true);
  }

  public void mergePDFs(List<File> inputFiles, File outputFile) throws IOException {
    PDFMergerUtility merger = new PDFMergerUtility();
    merger.setDestinationFileName(outputFile.getAbsolutePath());
    for (File file : inputFiles) {
      merger.addSource(file);
    }
    merger.mergeDocuments(IOUtils.createMemoryOnlyStreamCache());
  }
}
