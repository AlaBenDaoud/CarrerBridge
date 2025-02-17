package org.example.pi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.pi.models.ReponseReclamation;
import org.example.pi.services.ReponseReclamationService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReponseReclamationController {

    @FXML
    private TextField idRecField, idUserField, idReceiverField;

    @FXML
    private TextArea reponseField;

    @FXML
    private Label pdfPathLabel;

    private String pdfPath = null;
    private final ReponseReclamationService reponseService = new ReponseReclamationService();

    @FXML
    public void initialize() {
        // Initialization logic if needed
    }

    @FXML
    private void handleSelectPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            pdfPath = selectedFile.getAbsolutePath();
            pdfPathLabel.setText(selectedFile.getName());
        } else {
            pdfPathLabel.setText("Aucun fichier sélectionné");
        }
    }

    @FXML
    private void handleSubmit() {
        try {
            int idRec = Integer.parseInt(idRecField.getText());
            int idUser = Integer.parseInt(idUserField.getText());
            int idReceiver = Integer.parseInt(idReceiverField.getText());
            String reponse = reponseField.getText();
            LocalDateTime date = LocalDateTime.now();

            if (reponse.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La réponse ne peut pas être vide.");
                return;
            }

            // Create ReponseReclamation object
            ReponseReclamation reponseReclamation = new ReponseReclamation();
            reponseReclamation.setIdRec(idRec);
            reponseReclamation.setIdUser(idUser);
            reponseReclamation.setIdReceiver(idReceiver);
            reponseReclamation.setReponse(reponse);
            reponseReclamation.setPdfPath(pdfPath);
            reponseReclamation.setDate(date);
            reponseReclamation.setStatueOfReponseReclamation("Not Treated");

            // Save to database
            reponseService.addReponseReclamation(reponseReclamation);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réponse soumise avec succès !");
            clearForm();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Les ID doivent être des nombres valides.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Problème lors de l'enregistrement de la réponse.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        clearForm();
    }

    private void clearForm() {
        idRecField.clear();
        idUserField.clear();
        idReceiverField.clear();
        reponseField.clear();
        pdfPathLabel.setText("Aucun fichier sélectionné");
        pdfPath = null;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleViewAllResponses() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pi/ReponseReclamationList.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Voir Toutes les Réponses");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}