package org.example.pi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import org.example.pi.models.Reclamation;
import org.example.pi.services.ReclamationService;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReclamationSubmissionController {
    @FXML
    private TextField userIdField;
    @FXML
    private ComboBox<String> receiverComboBox;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField imagePathField;
    @FXML
    private TextField pdfPathField;

    @FXML
    private Label userIdError;
    @FXML
    private Label receiverError;
    @FXML
    private Label titleError;
    @FXML
    private Label descriptionError;
    @FXML
    private Label imageError;
    @FXML
    private Label pdfError;

    private ReclamationService reclamationService = new ReclamationService();

    @FXML
    public void initialize() {
        receiverComboBox.getItems().addAll("RH", "Admin");
    }

    @FXML
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePathField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleChoosePdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            pdfPathField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleSubmitReclamation() {
        clearErrors();

        boolean isValid = true;

        if (userIdField.getText().isEmpty() || !isNumeric(userIdField.getText())) {
            userIdError.setText("Invalid User ID.");
            isValid = false;
        }

        if (receiverComboBox.getValue() == null) {
            receiverError.setText("Please select a receiver.");
            isValid = false;
        }

        if (titleField.getText().isEmpty()) {
            titleError.setText("Title cannot be empty.");
            isValid = false;
        }

        if (descriptionField.getText().isEmpty()) {
            descriptionError.setText("Description cannot be empty.");
            isValid = false;
        }

        if (imagePathField.getText().isEmpty()) {
            imageError.setText("Please choose an image.");
            isValid = false;
        }

        if (pdfPathField.getText().isEmpty()) {
            pdfError.setText("Please choose a PDF.");
            isValid = false;
        }

        if (isValid) {
            try {
                Reclamation reclamation = new Reclamation();
                reclamation.setUserId(Integer.parseInt(userIdField.getText()));
                reclamation.setReceiver(receiverComboBox.getValue());
                reclamation.setTitle(titleField.getText());
                reclamation.setDescription(descriptionField.getText());
                reclamation.setImagePath(imagePathField.getText());
                reclamation.setPdfPath(pdfPathField.getText());
                reclamation.setDate(LocalDateTime.now());

                reclamationService.addReclamation(reclamation);
                clearForm();

                showAlert(AlertType.INFORMATION, "Submission Successful", "Your reclamation has been submitted successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Submission Error", "There was an error submitting your reclamation.");
            }
        }
    }

    private void clearErrors() {
        userIdError.setText("");
        receiverError.setText("");
        titleError.setText("");
        descriptionError.setText("");
        imageError.setText("");
        pdfError.setText("");
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    private void clearForm() {
        userIdField.clear();
        receiverComboBox.getSelectionModel().clearSelection();
        titleField.clear();
        descriptionField.clear();
        imagePathField.clear();
        pdfPathField.clear();
        clearErrors();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleViewAllReclamations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pi/ViewAllReclamations.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("View All Reclamations");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewAllAnswers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pi/ReponseReclamationList.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("View All Answers");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}