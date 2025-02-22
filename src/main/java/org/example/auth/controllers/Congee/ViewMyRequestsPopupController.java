package org.example.auth.controllers.Congee;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.auth.models.LeaveRequest;
import org.example.auth.models.OnlineJob;
import org.example.auth.models.Employee;
import org.example.auth.services.LeaveRequestService;
import org.example.auth.services.OnlineJobService;
import org.example.auth.services.EmployeeService;
import org.example.auth.controllers.connexion.AuthUserController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ViewMyRequestsPopupController {

    @FXML
    private VBox requestsContainer;

    @FXML
    private Label confirmedDaysLabel;

    @FXML
    private Label remainingDaysLabel;

    private LeaveRequestService leaveRequestService = new LeaveRequestService();
    private OnlineJobService onlineJobService = new OnlineJobService();
    private EmployeeService employeeService = new EmployeeService();

    @FXML
    public void initialize() {
        // Automatically load the leave requests for the logged-in user
        handleViewRequests();
    }

    /**
     * Loads leave requests for the logged-in user and displays them as cards.
     */
    @FXML
    public void handleViewRequests() {
        int userId = AuthUserController.getLoggedInUserId();
        if (userId != -1) {
            // Fetch the employee ID associated with the logged-in user
            List<Employee> employees = employeeService.getEmployeesByUserId(userId);
            if (!employees.isEmpty()) {
                int employeeId = employees.get(0).getId(); // Assuming one user has one employee record
                List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestsByEmployeeId(employeeId);
                loadRequests(leaveRequests);

                // Calculate total confirmed days and remaining days
                int totalConfirmedDays = calculateTotalConfirmedDays(leaveRequests);
                int remainingDays = 18 - totalConfirmedDays; // Assuming 18 is the total allowed leave days

                // Update the labels
                confirmedDaysLabel.setText(String.valueOf(totalConfirmedDays));
                remainingDaysLabel.setText(String.valueOf(remainingDays));
            } else {
                showAlert("Error", "No Employee Found", "No employee record found for the logged-in user.");
            }
        } else {
            showAlert("Error", "No User Logged In", "Please log in to view your leave requests.");
        }
    }

    /**
     * Loads leave requests into the card layout.
     *
     * @param leaveRequests The list of leave requests to display.
     */
    private void loadRequests(List<LeaveRequest> leaveRequests) {
        requestsContainer.getChildren().clear(); // Clear existing cards

        if (!leaveRequests.isEmpty()) {
            for (LeaveRequest request : leaveRequests) {
                requestsContainer.getChildren().add(createRequestCard(request));
            }
        } else {
            Label noRequestsLabel = new Label("No leave requests found.");
            noRequestsLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");
            requestsContainer.getChildren().add(noRequestsLabel);
        }
    }

    /**
     * Creates a card for the given leave request.
     *
     * @param request The leave request to display.
     * @return A VBox representing the leave request card.
     */
    private VBox createRequestCard(LeaveRequest request) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setStyle("-fx-padding: 15; -fx-background-color: rgba(30, 41, 59, 0.7); -fx-border-radius: 10; -fx-border-color: rgba(59, 130, 246, 0.2); -fx-background-radius: 10;");
        card.setPrefWidth(760);

        // Request ID
        Label idLabel = new Label("Request ID: " + request.getId());
        idLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #f8fafc;");

        // Start Date
        Label startDateLabel = new Label("Start Date: " + request.getStartDate());
        startDateLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // End Date
        Label endDateLabel = new Label("End Date: " + request.getEndDate());
        endDateLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Leave Type
        Label leaveTypeLabel = new Label("Leave Type: " + request.getLeaveType());
        leaveTypeLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Description
        Label descriptionLabel = new Label("Description: " + request.getDescription());
        descriptionLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Confirmation Status
        Label confirmedLabel = new Label("Confirmed: " + (request.isConfirmed() ? "Yes" : "No"));
        confirmedLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // PDF Path
        Label pdfPathLabel = new Label("PDF Path: " + request.getPdfPath());
        pdfPathLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Action Buttons
        HBox buttonBox = new HBox(10);
        Button deleteButton = new Button("Delete");
        Button modifyButton = new Button("Modify");
        Button applyJobButton = new Button("Apply Job");
        Button viewRemoteJobButton = new Button("View Remote Job");

        deleteButton.setStyle("-fx-text-fill: white; -fx-background-color: #ef4444; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");
        modifyButton.setStyle("-fx-text-fill: white; -fx-background-color: #3b82f6; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");
        applyJobButton.setStyle("-fx-text-fill: white; -fx-background-color: #3b82f6; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");
        viewRemoteJobButton.setStyle("-fx-text-fill: white; -fx-background-color: #3b82f6; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");

        deleteButton.setOnAction(event -> {
            leaveRequestService.deleteLeaveRequest(request.getId());
            handleViewRequests(); // Refresh the cards
        });

        modifyButton.setOnAction(event -> openModifyRequestWindow(request));

        applyJobButton.setOnAction(event -> openApplyRemoteWorkWindow(request));

        viewRemoteJobButton.setOnAction(event -> {
            OnlineJob remoteJob = onlineJobService.getOnlineJobByLeaveRequestId(request.getId());
            if (remoteJob != null) {
                openRemoteJobDetailsWindow(remoteJob);
            }
        });

        // Show Apply Job button only if the request is confirmed and no remote job exists
        OnlineJob existingJob = onlineJobService.getOnlineJobByLeaveRequestId(request.getId());
        if (request.isConfirmed() && existingJob == null) {
            buttonBox.getChildren().add(applyJobButton);
        }

        // Show View Remote Job button only if a remote job exists
        if (existingJob != null) {
            buttonBox.getChildren().add(viewRemoteJobButton);
        }

        buttonBox.getChildren().addAll(deleteButton, modifyButton);

        // Add all elements to the card
        card.getChildren().addAll(idLabel, startDateLabel, endDateLabel, leaveTypeLabel, descriptionLabel, confirmedLabel, pdfPathLabel, buttonBox);
        return card;
    }

    private int calculateTotalConfirmedDays(List<LeaveRequest> leaveRequests) {
        int totalConfirmedDays = 0;
        for (LeaveRequest request : leaveRequests) {
            if (request.isConfirmed() && "normal".equals(request.getLeaveType())) {
                LocalDate startDate = request.getStartDate();
                LocalDate endDate = request.getEndDate();
                totalConfirmedDays += calculateLeaveDays(startDate, endDate);
            }
        }
        return totalConfirmedDays;
    }

    private int calculateLeaveDays(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1; // +1 to include both start and end dates
    }

    private void openApplyRemoteWorkWindow(LeaveRequest request) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/apply_remote_work.fxml"));
            VBox root = loader.load();

            ApplyRemoteWorkController controller = loader.getController();
            controller.setLeaveRequest(request);

            Stage stage = new Stage();
            stage.setTitle("Apply Remote Work");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            handleViewRequests(); // Refresh the cards
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openModifyRequestWindow(LeaveRequest request) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/modify_request.fxml"));
            VBox root = loader.load();

            ModifyRequestController controller = loader.getController();
            controller.setLeaveRequest(request);

            Stage stage = new Stage();
            stage.setTitle("Modify Leave Request");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            handleViewRequests(); // Refresh the cards
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openRemoteJobDetailsWindow(OnlineJob remoteJob) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/remote_job_details.fxml"));
            VBox root = loader.load();

            RemoteJobDetailsController controller = loader.getController();
            controller.setRemoteJob(remoteJob);

            Stage stage = new Stage();
            stage.setTitle("Remote Job Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}