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

    JButton addButton = new JButton("Add PDF");
    JButton removeButton = new JButton("Remove Selected");
    JButton outputButton = new JButton("Select Output");
    JButton mergeButton = new JButton("Merge PDFs");

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

    frame.add(buttonPanel, BorderLayout.NORTH);
    frame.add(scrollPane, BorderLayout.CENTER);
    frame.add(outputPanel, BorderLayout.SOUTH);

    // Action listeners
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
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
        }
      }
    });

    removeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int selectedIndex = fileList.getSelectedIndex();
        if (selectedIndex != -1) {
          inputFiles.remove(selectedIndex);
          fileListModel.remove(selectedIndex);
        }
      }
    });

    outputButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser
            .setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
        int result = chooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
          outputField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
      }
    });

    mergeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (inputFiles.isEmpty()) {
          JOptionPane.showMessageDialog(frame, "Please select input PDFs.");
          return;
        }
        String outputPath = outputField.getText();
        if (outputPath.isEmpty()) {
          JOptionPane.showMessageDialog(frame, "Please select output file.");
          return;
        }
        try {
          mergePDFs(inputFiles, new File(outputPath));
          JOptionPane.showMessageDialog(frame, "PDFs merged successfully!");
        } catch (IOException ex) {
          JOptionPane.showMessageDialog(frame, "Error merging PDFs: " + ex.getMessage());
        }
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
