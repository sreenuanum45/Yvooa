package Utility;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class OCRUtility {
    private ITesseract tesseract;

    // Constructor to initialize Tesseract instance
    public OCRUtility() {
        tesseract = new Tesseract();

        // Set the path to the Tesseract executable if it's not in the system path
        tesseract.setDatapath("path/to/tesseract"); // Path to tessdata folder
    }

    // Method to extract text from an image file
    public String extractTextFromImage(String imagePath) {
        File imageFile = new File(imagePath);
        try {
            // Perform OCR on the image and return the extracted text
            return tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            // Handle errors (e.g., incorrect path, Tesseract not installed correctly)
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
