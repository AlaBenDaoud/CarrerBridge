package org.example.auth.controllers.Congee;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.auth.models.LeaveRequest;
import org.example.auth.models.OnlineJob;
import org.example.auth.services.OnlineJobService;

import java.time.LocalDate;

public class ApplyRemoteWorkController {

    @FXML
    private TextField titleField; // Corresponds to fx:id="titleField" in FXML

    @FXML
    private TextField postField; // Corresponds to fx:id="postField" in FXML

    @FXML
    private DatePicker startDatePicker; // Corresponds to fx:id="startDatePicker" in FXML

    @FXML
    private DatePicker endDatePicker; // Corresponds to fx:id="endDatePicker" in FXML

    @FXML
    private Button submitButton; // Corresponds to fx:id="submitButton" in FXML

    private LeaveRequest leaveRequest; // Store the LeaveRequest object
    private OnlineJobService onlineJobService = new OnlineJobService(); // Service to handle database operations

    // This method is called by the ViewMyRequestsPopupController to pass the LeaveRequest object
    public void setLeaveRequest(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
        populateFields(); // Populate fields with LeaveRequest data if needed
    }

    // Populate fields with LeaveRequest data (if applicable)
    private void populateFields() {
        if (leaveRequest != null) {
            // Example: Populate the title field with the leave request ID
            titleField.setText("Remote Work for Leave Request ID: " + leaveRequest.getId());

            // Set default start and end dates
            startDatePicker.setValue(LocalDate.now()); // Default start date is today
            endDatePicker.setValue(leaveRequest.getEndDate()); // Default end date is the leave request's end date
        }
    }

    // This method is called when the "Submit" button is clicked
    @FXML
    private void handleSubmit() {
        // Get the input values from the text fields
        String title = titleField.getText();
        String post = postField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        // Validate the input
        if (title.isEmpty() || post.isEmpty() || startDate == null || endDate == null) {
            showAlert("Input Error", "Empty Fields", "Please fill in all fields and select both start and end dates.");
            return;
        }

        // Ensure the end date is after the start date
        if (endDate.isBefore(startDate)) {
            showAlert("Input Error", "Invalid Dates", "The end date must be after the start date.");
            return;
        }

        // Create an OnlineJob object
        OnlineJob onlineJob = new OnlineJob();
        onlineJob.setLeaveRequestId(leaveRequest.getId());
        onlineJob.setTitle(title);
        onlineJob.setPost(post);
        onlineJob.setStartDate(startDate); // Set the selected start date
        onlineJob.setEndDate(endDate); // Set the selected end date
        onlineJob.setConfirmed(false); // Default to not confirmed

        // Save the online job to the database
        onlineJobService.addOnlineJob(onlineJob);

        // Show a success message
        showAlert("Success", "Application Submitted", "Your remote work application has been submitted successfully.");

        // Clear the form fields
        titleField.clear();
        postField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }

    // Helper method to show an alert dialog
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}