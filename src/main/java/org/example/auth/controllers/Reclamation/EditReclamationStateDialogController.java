package org.example.auth.controllers.Reclamation;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.example.auth.models.Reclamation;
import org.example.auth.services.ReclamationService;

import java.sql.SQLException;

public class EditReclamationStateDialogController {

    @FXML
    private ComboBox<String> statusComboBox;

    private Reclamation reclamation;
    private final ReclamationService reclamationService = new ReclamationService();

    @FXML
    public void initialize() {
        // Initialize with possible statuses
        statusComboBox.setItems(FXCollections.observableArrayList("Pending", "In Progress", "Resolved", "Rejected"));
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        statusComboBox.setValue(reclamation.getStatueOfReclamation());
    }

    @FXML
    private void handleSave() {
        if (statusComboBox.getValue() != null) {
            reclamation.setStatueOfReclamation(statusComboBox.getValue());
            try {
                reclamationService.updateReclamation(reclamation);
                closeDialog();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeDialog() {
        Stage stage = (Stage) statusComboBox.getScene().getWindow();
        stage.close();
    }
}