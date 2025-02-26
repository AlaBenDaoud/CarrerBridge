package org.example.pi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.pi.models.ReponseReclamation;
import org.example.pi.services.ReponseReclamationService;

import java.sql.SQLException;

public class ModifierReponseReclamationController {

    @FXML
    private TextField txtId;
    @FXML
    private TextArea txtReponse;

    private ReponseReclamation selectedReponse;
    private final ReponseReclamationService service = new ReponseReclamationService();

    public void setResponse(ReponseReclamation reponse) {
        this.selectedReponse = reponse;
        if (reponse != null) {
            txtId.setText(String.valueOf(reponse.getId()));
            txtReponse.setText(reponse.getReponse());
        }
    }

    @FXML
    private void enregistrerModification() {
        if (selectedReponse != null) {
            selectedReponse.setReponse(txtReponse.getText());

            try {
                service.updateReponseReclamation(selectedReponse);
                showAlert("Success", "Response modified successfully.", Alert.AlertType.INFORMATION);
                fermerFenetre();
            } catch (SQLException e) {
                showAlert("Error", "Failed to modify the response.", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) txtReponse.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}