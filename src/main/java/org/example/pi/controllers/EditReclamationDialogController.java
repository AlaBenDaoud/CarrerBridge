package org.example.pi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.pi.models.Reclamation;
import org.example.pi.services.ReclamationService;

import java.io.File;

public class EditReclamationDialogController {
    @FXML
    private TextField userIdField;
    @FXML
    private TextField receiverField;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField imagePathField;
    @FXML
    private TextField pdfPathField;
    @FXML
    private Label dateLabel;

    @FXML
    private Label userIdError;
    @FXML
    private Label receiverError;
    @FXML
    private Label titleError;
    @FXML
    private Label descriptionError;
    @FXML
    private Label imageError;
    @FXML
    private Label pdfError;

    private Reclamation reclamation;
    private ReclamationService reclamationService = new ReclamationService();

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        userIdField.setText(String.valueOf(reclamation.getUserId()));
        receiverField.setText(reclamation.getReceiver());
        titleField.setText(reclamation.getTitle());
        descriptionField.setText(reclamation.getDescription());
        imagePathField.setText(reclamation.getImagePath());
        pdfPathField.setText(reclamation.getPdfPath());
        dateLabel.setText(reclamation.getDate().toLocalDate().toString()); // Display the date
    }

    public Reclamation getUpdatedReclamation() {
        reclamation.setUserId(Integer.parseInt(userIdField.getText()));
        reclamation.setReceiver(receiverField.getText());
        reclamation.setTitle(titleField.getText());
        reclamation.setDescription(descriptionField.getText());
        reclamation.setImagePath(imagePathField.getText());
        reclamation.setPdfPath(pdfPathField.getText());
        // Return the updated reclamation object
        return reclamation;
    }

    @FXML
    private void handleSave() {
        clearErrors();
        boolean isValid = true;

        // Validate User ID
        if (userIdField.getText().isEmpty() || !isNumeric(userIdField.getText())) {
            userIdError.setText("Invalid User ID.");
            isValid = false;
        }

        // Validate Receiver
        if (receiverField.getText().isEmpty()) {
            receiverError.setText("Receiver cannot be empty.");
            isValid = false;
        }

        // Validate Title
        if (titleField.getText().isEmpty()) {
            titleError.setText("Title cannot be empty.");
            isValid = false;
        }

        // Validate Description
        if (descriptionField.getText().isEmpty()) {
            descriptionError.setText("Description cannot be empty.");
            isValid = false;
        }

        // Validate Image Path
        if (imagePathField.getText().isEmpty()) {
            imageError.setText("Image path cannot be empty.");
            isValid = false;
        }

        // Validate PDF Path
        if (pdfPathField.getText().isEmpty()) {
            pdfError.setText("PDF path cannot be empty.");
            isValid = false;
        }

        if (isValid) {
            try {
                // Update the reclamation object with the new data
                getUpdatedReclamation(); // Update the reclamation before saving

                // Save the updated reclamation to the database
                reclamationService.updateReclamation(reclamation);

                // Show success alert
                showAlert("Success", "Reclamation updated successfully!");

                // Close the dialog
                Stage stage = (Stage) userIdField.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to update the reclamation.");
            }
        }
    }

    @FXML
    private void handleCancel() {
        // Close the dialog without saving
        Stage stage = (Stage) userIdField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePathField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleUploadPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            pdfPathField.setText(file.getAbsolutePath());
        }
    }

    private void clearErrors() {
        userIdError.setText("");
        receiverError.setText("");
        titleError.setText("");
        descriptionError.setText("");
        imageError.setText("");
        pdfError.setText("");
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}