package com.itsmesdn.pdfmerger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 * Application state and data management class. Manages the application's data model and state.
 */
public class AppModel {
  private List<File> inputFiles;
  private DefaultListModel<String> fileListModel;
  private String outputPath;

  public AppModel() {
    this.inputFiles = new ArrayList<>();
    this.fileListModel = new DefaultListModel<>();
    this.outputPath = "";
  }

  /**
   * Adds files to the input list.
   *
   * @param files Files to add
   */
  public void addInputFiles(File[] files) {
    for (File file : files) {
      inputFiles.add(file);
      fileListModel.addElement(file.getName());
    }
  }

  /**
   * Removes a file at the specified index.
   *
   * @param index Index of file to remove
   * @return Removed filename, or null if no file at index
   */
  public String removeInputFile(int index) {
    if (index < 0 || index >= inputFiles.size()) {
      return null;
    }
    String removedName = fileListModel.get(index);
    inputFiles.remove(index);
    fileListModel.remove(index);
    return removedName;
  }

  /**
   * Clears all input files.
   */
  public void clearInputFiles() {
    inputFiles.clear();
    fileListModel.clear();
  }

  /**
   * Sets the output file path.
   *
   * @param path Output file path
   */
  public void setOutputPath(String path) {
    this.outputPath = path;
  }

  /**
   * Gets the output file path.
   *
   * @return Output file path
   */
  public String getOutputPath() {
    return outputPath;
  }

  /**
   * Clears the output path.
   */
  public void clearOutputPath() {
    this.outputPath = "";
  }

  /**
   * Gets the list of input files.
   *
   * @return List of input files
   */
  public List<File> getInputFiles() {
    return inputFiles;
  }

  /**
   * Gets the file list model for UI binding.
   *
   * @return DefaultListModel containing file names
   */
  public DefaultListModel<String> getFileListModel() {
    return fileListModel;
  }

  /**
   * Checks if input files list is empty.
   *
   * @return true if no input files, false otherwise
   */
  public boolean isEmpty() {
    return inputFiles.isEmpty();
  }

  /**
   * Clears all application state.
   */
  public void clear() {
    clearInputFiles();
    clearOutputPath();
  }
}
