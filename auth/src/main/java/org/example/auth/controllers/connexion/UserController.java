package org.example.auth.controllers.connexion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.auth.models.User;
import org.example.auth.services.TranslationService;
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
    private Button chooseCvButton;// New button to choose CV file

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private Label registrationTitle;

    @FXML
    private Label nameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label cvLabel;

    private TranslationService translationService;


    public UserController() {
        this.userService = new UserService();  // Initialize the service
    }

    @FXML
    public void initialize() {
        // Register button click event handler
        registerButton.setOnAction(event -> handleRegister());

        // Button to choose CV file
        chooseCvButton.setOnAction(event -> handleChooseCv());


        ObservableList<String> languages = FXCollections.observableArrayList("English", "Français");
        languageComboBox.setItems(languages);
        languageComboBox.setValue("English"); // Langue par défaut

        // Initialiser le service de traduction
        translationService = new TranslationService("en");

        // Mettre à jour l'interface avec la langue par défaut
        updateUI();

        // Gérer le changement de langue
        languageComboBox.setOnAction(event -> {
            String selectedLanguage = languageComboBox.getValue();
            String languageCode = selectedLanguage.equals("Français") ? "fr" : "en";
            translationService = new TranslationService(languageCode);
            updateUI();
        });
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



    private void updateUI() {
        registrationTitle.setText(translationService.getTranslation("user.registration.title", "User Registration"));
        nameLabel.setText(translationService.getTranslation("user.registration.name", "Name:"));
        nameField.setPromptText(translationService.getTranslation("user.registration.namePrompt", "Enter your name"));
        emailLabel.setText(translationService.getTranslation("user.registration.email", "Email:"));
        emailField.setPromptText(translationService.getTranslation("user.registration.emailPrompt", "Enter your email"));
        passwordLabel.setText(translationService.getTranslation("user.registration.password", "Password:"));
        passwordField.setPromptText(translationService.getTranslation("user.registration.passwordPrompt", "Enter your password"));
        cvLabel.setText(translationService.getTranslation("user.registration.cv", "CV:"));
        cvField.setPromptText(translationService.getTranslation("user.registration.cvPrompt", "No file selected"));
        chooseCvButton.setText(translationService.getTranslation("user.registration.chooseCvButton", "Choose CV File"));
        registerButton.setText(translationService.getTranslation("user.registration.registerButton", "Register"));
    }
}
