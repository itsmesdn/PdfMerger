# PDF Merger

A simple Java application that merges multiple PDF files into a single output PDF file using Apache PDFBox.

## Features

- **GUI Interface**: User-friendly Swing-based interface for selecting and managing PDF files
- **Add Files**: Select one or multiple PDF files to merge
- **Remove Files**: Remove unwanted files from the merge list
- **Output Selection**: Choose the destination path for the merged PDF
- **Error Handling**: Clear error messages for invalid operations

## Prerequisites

- Java 21 or higher
- Maven 3.6+

## Building the Project

Clone the repository and navigate to the project directory:

    cd PdfMerger

Build the project using Maven:
    
    mvn clean install

Running the Application
After building, run the application using:

    mvn exec:java -Dexec.mainClass="com.itsmesdn.pdfmerger.App"

Alternatively, you can run the JAR file directly (after building):

    java -cp target/pdfmerger-0.0.1-SNAPSHOT.jar:target/dependency/* com.itsmesdn.pdfmerger.App

How to Use
1. Launch the Application: Run the application using one of the methods above
2. **Add PDFs**: Click the "Add PDF" button to select one or more PDF files from your system
3. **Review Files**: The selected files will appear in the "Input PDF Files" list
4. **Remove Files** (if needed): Select a file in the list and click "Remove Selected"
5. **Select Output**: Click "Select Output" to choose where the merged PDF will be saved
6. **Merge**: Click "Merge PDFs" to combine all selected files into the output PDF
7. **Success**: A confirmation dialog will appear when the merge is complete

### Project Structure
```
src/
  main/
    java/
      com/itsmesdn/pdfmerger/
        App.java                 # Main application with GUI
  test/
    java/
      com/itsmesdn/pdfmerger/
        PdfmergerApplicationTests.java  # Unit tests
```
### Dependencies
- **Apache PDFBox 3.0.1**: For PDF manipulation and merging
- **JUnit Jupiter 5.10.0**: For unit testing

### Testing
Run the unit tests with:
```
mvn test
```

The test suite verifies that:

- PDFs can be successfully merged
- The output file is created and contains content
- The merged PDF has the expected number of pages

### Troubleshooting
- **"Please select input PDFs."**: Add at least one PDF file before merging
- **"Please select output file."**: Specify an output file path before merging
- **"Error merging PDFs"**: Check that all input files are valid PDFs and you have write permissions for the output directory

### License
This project is provided as-is for educational purposes.