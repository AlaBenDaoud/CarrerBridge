package org.example.demo.jobboardapp.Controller;

import org.example.demo.jobboardapp.Models.Job;
import org.example.demo.jobboardapp.Services.JobService;
import org.example.demo.jobboardapp.Utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PostJobController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField companyIdField;

    @FXML
    private TextField companyNameField;

    @FXML
    private TextField locationField;

    private final JobService jobService = new JobService();

    /**
     * Handles the "Post Job" button click event.
     *
     * @param event The action event.
     */
    @FXML
    private void handlePostJobButton(ActionEvent event) {
        // Validate input fields
        if (titleField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                companyIdField.getText().isEmpty() || companyNameField.getText().isEmpty() ||
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
        job.setCompanyName(companyNameField.getText());
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

    /**
     * Handles the "Back" button click event.
     *
     * @param event The action event.
     */
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

    /**
     * Clears all input fields in the form.
     */
    private void clearForm() {
        titleField.clear();
        descriptionField.clear();
        companyIdField.clear();
        companyNameField.clear();
        locationField.clear();
    }
}