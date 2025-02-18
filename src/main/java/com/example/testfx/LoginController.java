package com.example.testfx;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.sql.*;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button connexionButton;
    @FXML
    private Button retourButton;




    @FXML
    public void initialize() {
        connexionButton.setOnAction(event -> handleConnexion());
        retourButton.setOnAction(event -> goBack());
    }

    private void handleConnexion() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Logique de vérification des identifiants
        if (authenticateUser(email, password)) {
            System.out.println("Connexion réussie !");
            String role = getRoleByEmail(email); // Obtenir le rôle depuis la base de données
            if (role != null) {
                navigateToDashboard(role); // Rediriger vers le dashboard en fonction du rôle
            } else {
                showAlert(AlertType.ERROR, "Erreur de connexion", "Rôle non trouvé pour cet utilisateur.");
            }
        } else {
            showAlert(AlertType.ERROR, "Erreur de connexion", "Identifiants incorrects.");
        }
    }

    private boolean authenticateUser(String email, String password) {
        // Vérification des identifiants dans la base de données
        // Utilisez ici votre logique d'authentification avec la base de données.
        return email != null && password != null && !email.isEmpty() && !password.isEmpty();
    }

    private String getRoleByEmail(String email) {
        String role = null;

        // Connexion à la base de données et vérification dans les trois tables
        String sqlAdmin = "SELECT * FROM admin WHERE email = ? AND password = ?";
        String sqlRh = "SELECT * FROM rh WHERE email = ? AND password = ?";
        String sqlUser = "SELECT * FROM utilisateur WHERE email = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/careerbridge", "root", "")) {

            // Vérification dans la table admins
            role = checkUserInTable(conn, sqlAdmin, email);
            if (role != null) return "admin";

            // Vérification dans la table rh
            role = checkUserInTable(conn, sqlRh, email);
            if (role != null) return "rh";

            // Vérification dans la table utilisateur
            role = checkUserInTable(conn, sqlUser, email);
            if (role != null) return "utilisateur";

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Aucun rôle trouvé
    }

    private String checkUserInTable(Connection conn, String sql, String email) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, passwordField.getText());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("email"); // Si l'utilisateur est trouvé, on retourne l'email.
            }
        }
        return null;
    }

    private void navigateToDashboard(String role) {
        String fxmlFile = "dashUser.fxml"; // Par défaut, pour un utilisateur normal
        if ("admin".equals(role)) {
            fxmlFile = "dashAdmin.fxml";
        } else if ("rh".equals(role)) {
            fxmlFile = "dashRh.fxml";
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) connexionButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void goBack() {
        navigateToPage("selectlogin.fxml");
    }

    private void navigateToPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) connexionButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
