package org.example.pi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.pi.models.Reclamation;
import org.example.pi.models.ReponseReclamation;
import org.example.pi.services.ReponseReclamationService;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReponseReclamationController {

    @FXML
    private DialogPane dialogPane; // Ensure this matches the fx:id in the FXML file

    @FXML
    private TextField idRecField, idUserField, idReceiverField;
    @FXML
    private TextArea reponseField;
    @FXML
    private Label pdfPathLabel;
    @FXML
    private Label pdfError;

    private String pdfPath = null; // Path to the selected PDF file
    private final ReponseReclamationService reponseService = new ReponseReclamationService(); // Service for database operations

    private Reclamation reclamation; // Object to store reclamation data

    /**
     * Initialize the controller and set up button handlers.
     */
    @FXML
    public void initialize() {
        // Ensure dialogPane is not null
        if (dialogPane != null) {
            // Add event handler for the Submit button
            ButtonType submitButtonType = dialogPane.getButtonTypes().stream()
                    .filter(buttonType -> "Submit".equals(buttonType.getText()))
                    .findFirst()
                    .orElse(null);

            if (submitButtonType != null) {
                Button submitButton = (Button) dialogPane.lookupButton(submitButtonType);
                submitButton.setOnAction(event -> handleSubmit());
                submitButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 10; -fx-border-radius: 5;"); // Green button
            }
        } else {
            System.err.println("DialogPane is not initialized!");
        }
    }

    /**
     * Set reclamation data to populate the form fields.
     *
     * @param reclamation the Reclamation object containing the data
     */
    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        idRecField.setText(String.valueOf(reclamation.getId()));
        idUserField.setText(String.valueOf(reclamation.getUserId()));
        // Do not set the idReceiverField here, let the user input it
    }

    /**
     * Handle the PDF file selection by opening a file chooser window.
     */
    @FXML
    private void handleSelectPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            pdfPath = selectedFile.getAbsolutePath();
            pdfPathLabel.setText(selectedFile.getName());
            pdfError.setText(""); // Clear any previous error message
        } else {
            pdfPathLabel.setText("No file selected");
        }
    }

    /**
     * Handle form submission to save the response data to the database.
     */
    private void handleSubmit() {
        // Validate the response field
        if (reponseField.getText().isEmpty()) {
            pdfError.setText("Response cannot be empty.");
            return;
        }

        // Validate PDF file selection
        if (pdfPath == null) {
            pdfError.setText("Please select a PDF file.");
            return;
        }

        // Validate receiver ID
        int receiverId;
        try {
            receiverId = Integer.parseInt(idReceiverField.getText());
        } catch (NumberFormatException e) {
            pdfError.setText("Invalid receiver ID format.");
            return;
        }

        try {
            // Create a new ReponseReclamation object
            ReponseReclamation reponseReclamation = new ReponseReclamation();
            reponseReclamation.setIdRec(reclamation.getId());
            reponseReclamation.setIdUser(reclamation.getUserId());
            reponseReclamation.setIdReceiver(receiverId); // Use the user-input receiver ID
            reponseReclamation.setReponse(reponseField.getText());
            reponseReclamation.setPdfPath(pdfPath);
            reponseReclamation.setDate(LocalDateTime.now());
            reponseReclamation.setStatueOfReponseReclamation("Pending");

            // Debugging: Print the ReponseReclamation object to verify data
            System.out.println("ReponseReclamation to be saved: " + reponseReclamation);

            // Save the response to the database
            reponseService.addReponseReclamation(reponseReclamation);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Response submitted successfully!");

            // Close the dialog
            closeDialog();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save the response.");
        }
    }

    /**
     * Close the current dialog window.
     */
    private void closeDialog() {
        Stage stage = (Stage) idRecField.getScene().getWindow();
        stage.close();
    }

    /**
     * Show an alert dialog with the specified type, title, and message.
     *
     * @param type    the type of the alert (e.g., INFORMATION, ERROR)
     * @param title   the title of the alert
     * @param message the content message of the alert
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}