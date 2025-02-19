package org.example.auth.controllers.connexion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Navigate to Admin Login
    @FXML
    private void handleAdminLogin(ActionEvent event) throws IOException {
        navigateToPage(event, "/org/example/auth/connexionview/AdminLogin.fxml");
    }



    // Navigate to Company Login
    @FXML
    private void handleCompanyLogin(ActionEvent event) throws IOException {
        navigateToPage(event, "/org/example/auth/connexionview/loginCompany.fxml");
    }

    // Navigate to Company Register
    @FXML
    private void handleCompanyRegister(ActionEvent event) throws IOException {
        navigateToPage(event, "/org/example/auth/connexionview/company_register.fxml");
    }

    // Navigate to User Login
    @FXML
    private void handleUserLogin(ActionEvent event) throws IOException {
        navigateToPage(event, "/org/example/auth/connexionview/UserLoginView.fxml");
    }

    // Navigate to User Register
    @FXML
    private void handleUserRegister(ActionEvent event) throws IOException {
        navigateToPage(event, "/org/example/auth/connexionview/register.fxml");
    }

    // Helper method to load the FXML file and navigate to a new page
    private void navigateToPage(ActionEvent event, String fxmlPath) throws IOException {
        URL fxmlUrl = getClass().getResource(fxmlPath);
        if (fxmlUrl == null) {
            System.out.println("FXML file not found at: " + fxmlPath);
            throw new IOException("FXML file not found: " + fxmlPath);
        }
        root = FXMLLoader.load(fxmlUrl);

        // Correctly retrieve the stage from the MenuItem
        MenuItem menuItem = (MenuItem) event.getSource();
        Scene scene = ((Stage) menuItem.getParentPopup().getOwnerWindow()).getScene();
        Stage stage = (Stage) scene.getWindow();

        // Set the new scene
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}