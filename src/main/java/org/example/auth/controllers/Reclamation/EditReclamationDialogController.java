package org.example.auth.controllers.Reclamation;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.auth.models.Reclamation;
import org.example.auth.services.ReclamationService;

import java.io.File;

public class EditReclamationDialogController {
    @FXML
    private TextField userIdField;
    @FXML
    private TextField companyIdField; // Anciennement receiverField
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
    private Label companyIdError;
    @FXML
    private Label titleError;
    @FXML
    private Label descriptionError;
    @FXML
    private Label imageError;
    @FXML
    private Label pdfError;

    private Reclamation reclamation;
    private final ReclamationService reclamationService = new ReclamationService();

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        userIdField.setText(String.valueOf(reclamation.getUserId()));
        companyIdField.setText(String.valueOf(reclamation.getCompanyId())); // Correction du champ
        titleField.setText(reclamation.getTitle());
        descriptionField.setText(reclamation.getDescription());
        imagePathField.setText(reclamation.getImagePath());
        pdfPathField.setText(reclamation.getPdfPath());
        dateLabel.setText(reclamation.getDate().toLocalDate().toString()); // Affichage de la date
    }

    public void updateReclamationFromFields() {
        reclamation.setUserId(Integer.parseInt(userIdField.getText()));
        reclamation.setCompanyId(Integer.parseInt(companyIdField.getText())); // Correction ici
        reclamation.setTitle(titleField.getText());
        reclamation.setDescription(descriptionField.getText());
        reclamation.setImagePath(imagePathField.getText());
        reclamation.setPdfPath(pdfPathField.getText());
    }

    @FXML
    private void handleSave() {
        clearErrors();
        boolean isValid = true;

        // Validation User ID
        if (userIdField.getText().isEmpty() || !isNumeric(userIdField.getText())) {
            userIdError.setText("Invalid User ID.");
            isValid = false;
        }

        // Validation Company ID
        if (companyIdField.getText().isEmpty() || !isNumeric(companyIdField.getText())) {
            companyIdError.setText("Invalid Company ID.");
            isValid = false;
        }

        // Validation Title
        if (titleField.getText().isEmpty()) {
            titleError.setText("Title cannot be empty.");
            isValid = false;
        }

        // Validation Description
        if (descriptionField.getText().isEmpty()) {
            descriptionError.setText("Description cannot be empty.");
            isValid = false;
        }

        // Validation Image Path
        if (imagePathField.getText().isEmpty()) {
            imageError.setText("Image path cannot be empty.");
            isValid = false;
        }

        // Validation PDF Path
        if (pdfPathField.getText().isEmpty()) {
            pdfError.setText("PDF path cannot be empty.");
            isValid = false;
        }

        if (isValid) {
            try {
                // Mise à jour des champs avant enregistrement
                updateReclamationFromFields();

                // Mise à jour en base de données
                reclamationService.updateReclamation(reclamation);

                // Affichage d'un message de succès
                showAlert("Success", "Reclamation updated successfully!");

                // Fermeture de la fenêtre
                closeDialog();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to update the reclamation.");
            }
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
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
        companyIdError.setText(""); // Correction du champ
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

    private void closeDialog() {
        Stage stage = (Stage) userIdField.getScene().getWindow();
        stage.close();
    }
}
