package com.example.testfx;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RoleSelectionController {


    @FXML
    private Button adminButton;
    @FXML
    private Button rhButton;
    @FXML
    private Button utilisateurButton;

    @FXML
    public void initialize() {
        adminButton.setOnAction(event -> goToAdminPage());
        rhButton.setOnAction(event -> goToRhPage());
        utilisateurButton.setOnAction(event -> goToUtilisateurPage());
    }

    private void goToAdminPage() {
        // Rediriger vers la page d'admin
        navigateToPage("loginAdmin.fxml", "Admin");
    }

    private void goToRhPage() {
        // Rediriger vers la page RH
        navigateToPage("selectlogin.fxml", "RH");
    }

    private void goToUtilisateurPage() {
        // Rediriger vers la page utilisateur
        navigateToPage("selectlogin.fxml", "Utilisateur");
    }

    private void navigateToPage(String fxmlFile, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Passer le rôle à HelloController
            HelloController helloController = loader.getController();
            helloController.setSelectedRole(role);

            // Mettre à jour la scène
            Stage stage = (Stage) adminButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
