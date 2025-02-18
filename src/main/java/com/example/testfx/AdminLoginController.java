package com.example.testfx;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdminLoginController {
    @FXML
    private Button connexionButton;
    @FXML
    private Button retourButton;
    @FXML
    private TextField emailField;  // Email field
    @FXML
    private PasswordField passwordField;  // Password field


    @FXML
    public void initialize() {
        // Lier les actions des boutons
        connexionButton.setOnAction(this::handleConnexion);
        retourButton.setOnAction(this::goBack);
    }



    // Gérer l'action de connexion
    private void handleConnexion(ActionEvent event) {
        // Récupérer les données saisies
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();


        if (email.isEmpty() || password.isEmpty()) {
            // Vérifier si les champs sont vides
            showAlert("Erreur", "Veuillez entrer un email et un mot de passe.", Alert.AlertType.ERROR);
            return;
        }

        if (authenticateAdmin(email, password)) {
            // Connexion réussie
            System.out.println("Connexion réussie");
            navigateToPage("dashAdmin.fxml", event); // Rediriger vers le tableau de bord de l'admin
        } else {
            // Échec de connexion
            showAlert("Erreur de connexion", "Identifiants incorrects.", Alert.AlertType.ERROR);
        }
    }



    private boolean authenticateAdmin(String email, String password) {
        String query = "SELECT * FROM admin WHERE email = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de se connecter à la base de données.", Alert.AlertType.ERROR);
            return false;
        }
    }

    // Méthode générique pour afficher des alertes
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    // Retour à la page précédente
    private void goBack(ActionEvent event) {
        navigateToPage("loginAdmin.fxml", event);
    }

    // Méthode générique pour naviguer vers une page
    private void navigateToPage(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
