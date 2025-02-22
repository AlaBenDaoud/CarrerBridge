package org.example.auth.controllers.Congee;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PdfViewerController {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ImageView pdfImageView;

    public void loadPdf(String pdfPath) {
        try {
            System.out.println("Loading PDF: " + pdfPath); // Debug statement

            // Load the PDF file
            File file = new File(pdfPath);
            if (!file.exists()) {
                System.out.println("PDF file does not exist: " + pdfPath); // Debug statement
                return;
            }

            PDDocument document = PDDocument.load(file);
            System.out.println("PDF loaded successfully: " + pdfPath); // Debug statement

            // Render the first page of the PDF
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage bufferedImage = renderer.renderImage(0); // Render the first page

            // Convert BufferedImage to JavaFX Image
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            pdfImageView.setImage(image);

            // Close the document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}