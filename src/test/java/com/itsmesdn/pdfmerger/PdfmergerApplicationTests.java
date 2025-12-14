package com.itsmesdn.pdfmerger;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PdfmergerApplicationTests {

	@TempDir
	Path tempDir;

	@Test
	void testMergePdfs() throws Exception {
		// Create temporary input PDF files
		File input1 = createSamplePdf(tempDir.resolve("input1.pdf"));
		File input2 = createSamplePdf(tempDir.resolve("input2.pdf"));
		File output = tempDir.resolve("output.pdf").toFile();

		List<File> inputs = List.of(input1, input2);

		// Test the merge
		App app = new App();
		app.mergePDFs(inputs, output);

		// Assert output file exists and has content
		assertTrue(output.exists());
		assertTrue(Files.size(output.toPath()) > 0);

		// Optionally, verify the merged PDF has the expected number of pages
		try (PDDocument doc = Loader.loadPDF(output)) {
			assertEquals(2, doc.getNumberOfPages());
		}
	}

	private File createSamplePdf(Path path) throws IOException {
		try (PDDocument doc = new PDDocument()) {
			PDPage page = new PDPage();
			doc.addPage(page);
			try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
				contentStream.beginText();
				contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
				contentStream.newLineAtOffset(100, 700);
				contentStream.showText("Sample PDF content");
				contentStream.endText();
			}
			doc.save(path.toFile());
		}
		return path.toFile();
	}
}
