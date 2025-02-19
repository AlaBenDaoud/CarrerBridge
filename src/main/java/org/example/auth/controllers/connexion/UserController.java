package org.example.auth.controllers.connexion;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.auth.models.User;
import org.example.auth.services.UserService;

import java.io.File;

public class UserController {

    private UserService userService;

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField cvField;  // Keep the cvField for displaying the file path
    @FXML
    private Button registerButton;
    @FXML
    private Button chooseCvButton; // New button to choose CV file

    public UserController() {
        this.userService = new UserService();  // Initialize the service
    }

    @FXML
    public void initialize() {
        // Register button click event handler
        registerButton.setOnAction(event -> handleRegister());

        // Button to choose CV file
        chooseCvButton.setOnAction(event -> handleChooseCv());
    }

    private void handleRegister() {
        // Retrieve data from input fields
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String cvFilePath = cvField.getText();

        // Create a new user object
        User newUser = new User(name, email, password, cvFilePath);

        // Try to register the user
        boolean isRegistered = userService.registerUser(newUser);

        // Show a message based on whether the registration was successful
        if (isRegistered) {
            showAlert("Registration Successful", "User registered successfully.", AlertType.INFORMATION);
        } else {
            showAlert("Registration Failed", "Email already exists.", AlertType.ERROR);
        }
    }

    private void handleChooseCv() {
        // Create a FileChooser for selecting a CV (PDF or TXT)
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF and TXT Files", "*.pdf", "*.txt"));

        // Show the file chooser and get the selected file
        File selectedFile = fileChooser.showOpenDialog(null);

        // If a file is selected, set the path in the CV field
        if (selectedFile != null) {
            cvField.setText(selectedFile.getAbsolutePath());
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
