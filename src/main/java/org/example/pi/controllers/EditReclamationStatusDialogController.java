package org.example.pi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonBar.ButtonData;
import org.example.pi.models.Reclamation;
import org.example.pi.services.ReclamationService;

public class EditReclamationStatusDialogController {

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private DialogPane dialogPane;

    private Reclamation reclamation;
    private final ReclamationService reclamationService = new ReclamationService();

    @FXML
    public void initialize() {
        // Set up the event handler for the "Save" button
        ButtonType saveButtonType = dialogPane.getButtonTypes().stream()
                .filter(buttonType -> buttonType.getButtonData() == ButtonData.OK_DONE)
                .findFirst()
                .orElse(null);

        if (saveButtonType != null) {
            javafx.scene.control.Button saveButton = (javafx.scene.control.Button) dialogPane.lookupButton(saveButtonType);
            saveButton.setOnAction(event -> handleSave());
        }
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        statusComboBox.getItems().addAll("Pending", "In Progress", "Resolved", "Closed");
        statusComboBox.setValue(reclamation.getStatueOfReclamation());
    }

    @FXML
    private void handleSave() {
        String newStatus = statusComboBox.getValue();
        if (newStatus != null && !newStatus.isEmpty()) {
            reclamation.setStatueOfReclamation(newStatus);
            try {
                reclamationService.updateReclamationStatus(reclamation); // Update status in the database
                closeDialog();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to update the reclamation status.");
            }
        }
    }

    private void closeDialog() {
        statusComboBox.getScene().getWindow().hide();
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}