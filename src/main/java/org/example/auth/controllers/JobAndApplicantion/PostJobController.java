package org.example.auth.controllers.JobAndApplicantion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.auth.models.Job;
import org.example.auth.services.JobService;
import org.example.auth.utils.AlertUtils;
import org.example.auth.controllers.connexion.AuthCompanyController;  // Import the AuthCompanyController

import java.io.IOException;

public class PostJobController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField companyIdField;

    @FXML
    private TextField positionField; // Changed from companyNameField to positionField

    @FXML
    private TextField locationField;

    private final JobService jobService = new JobService();

    // Assuming the loggedInCompanyId will be fetched from the AuthCompanyController
    private int loggedInCompanyId;

    @FXML
    public void initialize() {
        // Fetch the logged-in company's ID from the AuthCompanyController
        this.loggedInCompanyId = AuthCompanyController.getLoggedInCompanyId(); // Directly access the static method

        // Set the companyIdField to the logged-in company's ID
        companyIdField.setText(String.valueOf(loggedInCompanyId));
        companyIdField.setDisable(true); // Disable the field so it can't be edited
    }

    @FXML
    private void handlePostJobButton(ActionEvent event) {
        // Validate input fields
        if (titleField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                companyIdField.getText().isEmpty() || positionField.getText().isEmpty() || // Updated to positionField
                locationField.getText().isEmpty()) {
            AlertUtils.showError("Please fill in all fields.");
            return;
        }

        // Parse company ID from the input field
        int companyId;
        try {
            companyId = Integer.parseInt(companyIdField.getText());
        } catch (NumberFormatException e) {
            AlertUtils.showError("Company ID must be a valid number.");
            return;
        }

        // Create a new Job object
        Job job = new Job();
        job.setTitle(titleField.getText());
        job.setDescription(descriptionField.getText());
        job.setCompanyId(companyId);
        job.setPosition(positionField.getText()); // Updated from companyNameField to positionField
        job.setLocation(locationField.getText());

        // Save the job to the database
        try {
            jobService.addJob(job);
            AlertUtils.showSuccess("Job posted successfully!");

            // Clear the form
            clearForm();
        } catch (Exception e) {
            AlertUtils.showError("An error occurred while posting the job. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            // Load the main dashboard view
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo/jobboardapp/views/main.fxml"));
            Stage stage = (Stage) titleField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Job Board Application");
            stage.show();
        } catch (IOException e) {
            AlertUtils.showError("Unable to navigate back to the dashboard.");
            e.printStackTrace();
        }
    }

    private void clearForm() {
        titleField.clear();
        descriptionField.clear();
        positionField.clear(); // Updated to positionField
        locationField.clear();
    }
}
