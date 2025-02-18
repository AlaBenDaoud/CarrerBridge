package com.example.testfx;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class AdminDashController {
    @FXML
    private Button btnSortie; // Correspond au bouton "sortie" du FXML

    @FXML
    private Button btnMoins; // Correspond au bouton "moins" du FXML

    @FXML
    private Button btnReglageIA;

    @FXML
    public void initialize() {
        // Associer des événements aux boutons
        btnSortie.setOnAction(event -> retourPageConnexion());
        btnMoins.setOnAction(event -> reduireFenetre());
        btnReglageIA.setOnAction(event -> ouvrirReglageIA());

    }

    // Méthode pour fermer complètement l'application
    private void retourPageConnexion() {
        try {
            // Charger le fichier FXML de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginAdmin.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle
            Stage stage = (Stage) btnSortie.getScene().getWindow();

            // Changer la scène pour afficher loginAdmin.fxml
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion - Admin");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Afficher l'erreur si le fichier FXML n'est pas trouvé
        }
    }

    // Méthode pour réduire la fenêtre
    private void reduireFenetre() {
        Stage stage = (Stage) btnMoins.getScene().getWindow(); // Obtenir la fenêtre actuelle
        stage.setIconified(true); // Réduire la fenêtre
    }

    private void ouvrirReglageIA() {
        try {
            // Charger le fichier FXML de réglage IA
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reglageIA.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle
            Stage stage = (Stage) btnReglageIA.getScene().getWindow();

            // Changer la scène pour afficher reglageIA.fxml
            stage.setScene(new Scene(root));
            stage.setTitle("Réglage IA");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Afficher l'erreur si le fichier FXML n'est pas trouvé
        }
    }
    @FXML
    private void handleRetourButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tableau de bord Admin");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Page non trouvée");
            alert.setContentText("Impossible de charger la page 'dashAdmin.fxml'. Vérifiez le chemin du fichier.");
            alert.showAndWait();
        }
    }




}
