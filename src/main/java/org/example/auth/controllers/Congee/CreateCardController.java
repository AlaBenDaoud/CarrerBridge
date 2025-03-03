package org.example.auth.controllers.Congee;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class CreateCardController {

    @FXML
    private TextField idField, fullNameField, positionField, telephoneField;
    @FXML
    private DatePicker startDatePicker, endDatePicker;
    @FXML
    private ImageView imageView, previewImageView;
    @FXML
    private Label previewId, previewFullName, previewPosition, previewTelephone, previewDates;
    @FXML
    private StackPane cardPreview;
    @FXML
    private Canvas barcodeCanvas;

    private File selectedImageFile;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private final Random random = new Random();

    @FXML
    public void initialize() {
        // Set today's date as default for startDatePicker
        startDatePicker.setValue(LocalDate.now());
        // Set date one year from now as default for endDatePicker
        endDatePicker.setValue(LocalDate.now().plusYears(1));

        // Initialize preview with default values
        updatePreview();
    }

    @FXML
    private void handleUploadPicture(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Employee Photo");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedImageFile = file;
            javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());

            // Apply the image to both form and preview
            imageView.setImage(image);
            previewImageView.setImage(image);
        }
    }

    @FXML
    private void updatePreview() {
        // Get current values from form fields
        String id = idField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String position = positionField.getText().trim();
        String telephone = telephoneField.getText().trim();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        // Update preview labels with form values or defaults
        previewId.setText("ID: " + (id.isEmpty() ? "0000" : id));
        previewFullName.setText(fullName.isEmpty() ? "Full Name" : fullName);
        previewPosition.setText(position.isEmpty() ? "Position" : position);
        previewTelephone.setText("Tel: " + (telephone.isEmpty() ? "000-000-0000" : telephone));

        // Format dates if they exist
        if (startDate != null && endDate != null) {
            String formattedStartDate = startDate.format(dateFormatter);
            String formattedEndDate = endDate.format(dateFormatter);
            previewDates.setText("Valid: " + formattedStartDate + " - " + formattedEndDate);
        } else {
            previewDates.setText("Valid: Start - End");
        }

        // Generate barcode based on the information
        generateBarcode();
    }

    private void generateBarcode() {
        if (barcodeCanvas == null) return;

        GraphicsContext gc = barcodeCanvas.getGraphicsContext2D();
        double width = barcodeCanvas.getWidth();
        double height = barcodeCanvas.getHeight();

        // Clear canvas
        gc.clearRect(0, 0, width, height);

        // Get data to encode
        String id = idField.getText().trim().isEmpty() ? "0000" : idField.getText().trim();
        String fullName = fullNameField.getText().trim().isEmpty() ? "DefaultName" : fullNameField.getText().trim();

        // Generate a simple "seed" from the employee info
        String seedData = id + fullName;
        long seed = 0;
        for (char c : seedData.toCharArray()) {
            seed += (int) c;
        }
        random.setSeed(seed);

        // Draw barcode background
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);

        // Draw the bars
        gc.setFill(Color.BLACK);

        // Generate bars based on user info
        double barWidth = 3;
        double spacing = 1;
        int numBars = (int) (width / (barWidth + spacing));
        double startX = 0;

        for (int i = 0; i < numBars; i++) {
            if (random.nextBoolean()) {
                double barHeight = height * (0.3 + 0.7 * random.nextDouble());
                double yPos = (height - barHeight) / 2;
                gc.fillRect(startX, yPos, barWidth, barHeight);
            }
            startX += barWidth + spacing;
        }

        // Draw a border
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(0, 0, width, height);
    }

    @FXML
    private void handleSaveCard(ActionEvent event) {
        // Validate all required fields
        String id = idField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String position = positionField.getText().trim();
        String telephone = telephoneField.getText().trim();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        // Perform validation checks
        if (id.isEmpty() || fullName.isEmpty() || position.isEmpty() || telephone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Information",
                    "Please fill in all required fields before generating the ID card.");
            return;
        }

        if (startDate == null || endDate == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Dates",
                    "Please select both start and end dates for the ID card.");
            return;
        }

        if (endDate.isBefore(startDate)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Date Range",
                    "End date cannot be before start date.");
            return;
        }

        if (selectedImageFile == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("No Photo Selected");
            alert.setHeaderText(null);
            alert.setContentText("Do you want to continue without a photo?");

            ButtonType buttonYes = new ButtonType("Yes");
            ButtonType buttonNo = new ButtonType("No");

            alert.getButtonTypes().setAll(buttonYes, buttonNo);

            if (alert.showAndWait().get() == buttonNo) {
                return;
            }
        }

        // Save the ID card as PDF
        try {
            saveToPDF();
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Employee ID card has been successfully generated and saved as PDF!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "PDF Generation Error",
                    "Failed to generate PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveToPDF() throws IOException {
        // Let user choose where to save the PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save ID Card as PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        // Default filename with employee name
        String defaultFileName = (fullNameField.getText().trim().isEmpty() ?
                "employee" : fullNameField.getText().trim().replaceAll("\\s+", "_").toLowerCase()) + "_id_card.pdf";
        fileChooser.setInitialFileName(defaultFileName);

        File outputFile = fileChooser.showSaveDialog(cardPreview.getScene().getWindow());
        if (outputFile == null) return;

        // Take a snapshot of the ID card preview
        WritableImage cardSnapshot = cardPreview.snapshot(new SnapshotParameters(), null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(cardSnapshot, null);

        // Convert BufferedImage to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] cardImageData = baos.toByteArray();

        // Create PDF
        try (PdfWriter writer = new PdfWriter(new FileOutputStream(outputFile));
             PdfDocument pdf = new PdfDocument(writer)) {

            // Create a custom page size to match the ID card (portrait orientation)
            float cardWidth = (float) cardPreview.getWidth();
            float cardHeight = (float) cardPreview.getHeight();
            PageSize pageSize = new PageSize(cardWidth + 40, cardHeight + 40);
            pdf.setDefaultPageSize(pageSize);

            Document document = new Document(pdf);

            // Add the ID card image
            Image cardImage = new Image(ImageDataFactory.create(cardImageData));
            cardImage.setFixedPosition(20, 20);
            document.add(cardImage);

            document.close();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}