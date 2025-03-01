package org.example.auth.controllers.Reclamation;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import org.example.auth.models.Reclamation;
import org.example.auth.services.ReclamationService;

public class EditReclamationStatusDialogController {

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private DialogPane dialogPane;

    private Reclamation reclamation;
    private final ReclamationService reclamationService = new ReclamationService();

    @FXML
    public void initialize() {
        // Récupérer le bouton "Save" du DialogPane et lui assigner un événement
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

        if (reclamation != null) {
            statusComboBox.setValue(reclamation.getStatueOfReclamation());
        }
    }

    @FXML
    private void handleSave() {
        if (reclamation == null) {
            showAlert("Error", "No reclamation selected.");
            return;
        }

        String newStatus = statusComboBox.getValue();
        if (newStatus != null && !newStatus.isEmpty()) {
            try {
                // Mise à jour du statut dans la base de données
                reclamationService.updateReclamationStatus(reclamation.getId(), newStatus);

                // Mise à jour locale de l'objet Reclamation
                reclamation.setStatueOfReclamation(newStatus);

                // Fermeture du dialogue après succès
                closeDialog();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to update the reclamation status.");
            }
        } else {
            showAlert("Warning", "Please select a status before saving.");
        }
    }

    private void closeDialog() {
        statusComboBox.getScene().getWindow().hide();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
