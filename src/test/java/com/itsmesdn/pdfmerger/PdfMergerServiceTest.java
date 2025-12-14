package com.itsmesdn.pdfmerger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PdfMergerServiceTest {

	@TempDir
	Path tempDir;

	@Test
	void testMergePdfs() throws Exception {
		// Create temporary input PDF files
		File input1 = SamplePdfGenerator.createSamplePdf(tempDir.resolve("input1.pdf").toFile(),
				"Sample PDF 1 - Page 1");
		File input2 = SamplePdfGenerator.createSamplePdf(tempDir.resolve("input2.pdf").toFile(),
				"Sample PDF 2 - Page 1");
		File output = tempDir.resolve("output.pdf").toFile();

		List<File> inputs = List.of(input1, input2);

		// Test the merge
		PdfMergerService mergerService = new PdfMergerService();
		mergerService.mergePDFs(inputs, output);

		// Assert output file exists and has content
		assertTrue(output.exists());
		assertTrue(Files.size(output.toPath()) > 0);

		// Optionally, verify the merged PDF has the expected number of pages
		try (PDDocument doc = Loader.loadPDF(output)) {
			assertEquals(2, doc.getNumberOfPages());
		}
	}
}
