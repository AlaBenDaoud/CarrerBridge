package org.example.pi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.pi.models.Reclamation;
import org.example.pi.services.ReclamationService;
import javafx.stage.Stage;

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
        // No need to update the date as it is non-editable
        return reclamation;
    }

    @FXML
    private void handleSave() {
        try {
            // Update the reclamation object with the new data
            reclamation.setUserId(Integer.parseInt(userIdField.getText()));
            reclamation.setReceiver(receiverField.getText());
            reclamation.setTitle(titleField.getText());
            reclamation.setDescription(descriptionField.getText());
            reclamation.setImagePath(imagePathField.getText());
            reclamation.setPdfPath(pdfPathField.getText());
            // No need to update the date

            // Save the updated reclamation to the database
            reclamationService.updateReclamation(reclamation);

            // Close the dialog
            Stage stage = (Stage) userIdField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally show an error dialog
        }
    }
}