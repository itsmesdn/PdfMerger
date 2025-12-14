package com.itsmesdn.pdfmerger;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Service class responsible for PDF merging operations. Handles all PDF-related business logic.
 */
public class PdfMergerService {

  /**
   * Merges multiple PDF files into a single output file.
   *
   * @param inputFiles List of PDF files to merge
   * @param outputFile Output PDF file
   * @throws IOException if an error occurs during merging
   */
  public void mergePDFs(List<File> inputFiles, File outputFile) throws IOException {
    if (inputFiles == null || inputFiles.isEmpty()) {
      throw new IllegalArgumentException("Input files list cannot be empty");
    }
    if (outputFile == null) {
      throw new IllegalArgumentException("Output file cannot be null");
    }

    PDFMergerUtility merger = new PDFMergerUtility();
    merger.setDestinationFileName(outputFile.getAbsolutePath());

    for (File file : inputFiles) {
      if (!file.exists()) {
        throw new IOException("Input file not found: " + file.getAbsolutePath());
      }
      merger.addSource(file);
    }

    merger.mergeDocuments(IOUtils.createMemoryOnlyStreamCache());
  }

  /**
   * Validates if a file is a valid PDF file.
   *
   * @param file File to validate
   * @return true if file is a valid PDF, false otherwise
   */
  public boolean isValidPdf(File file) {
    return file.exists() && file.getName().toLowerCase().endsWith(".pdf");
  }

  /**
   * Ensures the output filename ends with .pdf extension.
   *
   * @param filename Filename to validate
   * @return Filename with .pdf extension
   */
  public String ensurePdfExtension(String filename) {
    if (!filename.toLowerCase().endsWith(".pdf")) {
      return filename + ".pdf";
    }
    return filename;
  }
}
