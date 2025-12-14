package com.itsmesdn.pdfmerger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import java.io.File;
import java.io.IOException;

public class SamplePdfGenerator {
  public static void main(String[] args) throws IOException {
    String resourcesPath = "src/test/resources";
    new File(resourcesPath).mkdirs();
    createSamplePdf(new File(resourcesPath, "sample1.pdf"), "Sample PDF 1 - Page 1");
    createSamplePdf(new File(resourcesPath, "sample2.pdf"), "Sample PDF 2 - Page 1");
    System.out.println("Sample PDFs created successfully in " + resourcesPath);
  }

  public static File createSamplePdf(File file, String content) {
    try (PDDocument doc = new PDDocument()) {
      PDPage page = new PDPage();
      doc.addPage(page);
      try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);
        contentStream.newLineAtOffset(50, 700);
        contentStream.showText(content);
        contentStream.endText();
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
      doc.save(file);

    } catch (IOException e1) {
      e1.printStackTrace();
      System.exit(1);
    }
    return file;
  }
}
