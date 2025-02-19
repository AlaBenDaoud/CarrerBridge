package org.example.auth.controllers.connexion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.auth.models.Company;
import org.example.auth.services.CompanyService;

public class CompanyController {

    private final CompanyService companyService;

    @FXML
    private TextField companyNameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField secteurField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    public CompanyController() {
        this.companyService = new CompanyService();  // Initialize the service
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        // Retrieve data from input fields
        String companyName = companyNameField.getText();
        String location = locationField.getText();
        String secteur = secteurField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Create a new company object
        Company newCompany = new Company(companyName, location, secteur, email, password);

        // Try to register the company
        boolean isRegistered = companyService.registerCompany(newCompany);

        // Show a message based on whether the registration was successful
        if (isRegistered) {
            showAlert("Registration Successful", "Company registered successfully.", AlertType.INFORMATION);
        } else {
            showAlert("Registration Failed", "Email already exists.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}