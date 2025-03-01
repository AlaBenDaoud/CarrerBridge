package org.example.auth.controllers.Reclamation;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.auth.models.Reclamation;
import org.example.auth.services.ReclamationService;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReclamationSubmissionController {

    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField imagePathField;
    @FXML
    private TextField pdfPathField;

    @FXML
    private Label titleError;
    @FXML
    private Label descriptionError;
    @FXML
    private Label imageError;
    @FXML
    private Label pdfError;

    private ReclamationService reclamationService = new ReclamationService();

    private int employeeId;
    private int companyId;

    @FXML
    public void initialize() {
        // Initialize any necessary components here
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    /**
     * Handles the "Choose Image" button click event.
     */
    @FXML
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            imagePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Handles the "Choose PDF" button click event.
     */
    @FXML
    private void handleChoosePdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            pdfPathField.setText(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Handles the "Submit Reclamation" button click event.
     */
    @FXML
    private void handleSubmitReclamation() {
        clearErrors();
        boolean isValid = true;

        if (titleField.getText().isEmpty()) {
            titleError.setText("Title cannot be empty.");
            isValid = false;
        }

        if (descriptionField.getText().isEmpty()) {
            descriptionError.setText("Description cannot be empty.");
            isValid = false;
        }

        if (isValid) {
            try {
                Reclamation reclamation = new Reclamation();
                reclamation.setUserId(employeeId);
                reclamation.setCompanyId(companyId);
                reclamation.setTitle(titleField.getText());
                reclamation.setDescription(descriptionField.getText());
                reclamation.setImagePath(imagePathField.getText());
                reclamation.setPdfPath(pdfPathField.getText());
                reclamation.setDate(LocalDateTime.now());

                reclamationService.addReclamation(reclamation);
                clearForm();

                showAlert(Alert.AlertType.INFORMATION, "Submission Successful", "Your reclamation has been submitted successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Submission Error", "There was an error submitting your reclamation.");
            }
        }
    }

    /**
     * Clears all error messages.
     */
    private void clearErrors() {
        titleError.setText("");
        descriptionError.setText("");
        imageError.setText("");
        pdfError.setText("");
    }

    /**
     * Clears the form fields and error messages.
     */
    private void clearForm() {
        titleField.clear();
        descriptionField.clear();
        imagePathField.clear();
        pdfPathField.clear();
        clearErrors();
    }

    /**
     * Displays an alert dialog.
     *
     * @param alertType The type of alert (e.g., ERROR, INFORMATION).
     * @param title     The title of the alert.
     * @param message   The message to display.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}