package com.example.testfx;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
public class OffreController {
    @FXML
    private Button btnPublierOffre;

    @FXML
    private TextField titreField;

    @FXML
    private TextField skillsField;

    @FXML
    private TextField experienceField;

    @FXML
    private TextField locationField;

    @FXML
    private Button btnRetour;

    @FXML
    public void initialize() {
        btnPublierOffre.setOnAction(event -> saveOffer());
        btnRetour.setOnAction(event -> handleRetourButtonClick());
    }

    @FXML
    private void saveOffer() {
        String titre = titreField.getText();
        String skills = skillsField.getText();
        String experienceText = experienceField.getText();
        String location = locationField.getText();

        if (titre.isEmpty() || skills.isEmpty() || experienceText.isEmpty() || location.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        int experience;
        try {
            experience = Integer.parseInt(experienceText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'expérience doit être un nombre.");
            return;
        }

        int rh_id = 1;

        String query = "INSERT INTO offres (titre, competences, experience, localisation, rh_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/careerbridge", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, titre);
            stmt.setString(2, skills);
            stmt.setInt(3, experience);
            stmt.setString(4, location);
            stmt.setInt(5, rh_id);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "✅ Offre enregistrée avec succès !");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "❌ Échec de l'enregistrement de l'offre.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'enregistrement : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleRetourButtonClick() {
        try {
            // Charger la page dashRh.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashRh.fxml")); // Assurez-vous que le chemin est correct
            Parent root = loader.load();

            // Obtenir la scène actuelle
            Stage stage = (Stage) btnRetour.getScene().getWindow();

            // Changer la scène pour afficher la page dashRh.fxml
            stage.setScene(new Scene(root));
            stage.setTitle("Tableau de Bord RH"); // Vous pouvez changer le titre si nécessaire
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page : " + e.getMessage());
        }
    }

}
