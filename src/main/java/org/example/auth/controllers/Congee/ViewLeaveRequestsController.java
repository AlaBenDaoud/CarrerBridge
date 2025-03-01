package org.example.auth.controllers.Congee;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.auth.models.LeaveRequest;
import org.example.auth.services.LeaveRequestService;
import org.example.auth.controllers.connexion.AuthCompanyController;
import org.example.auth.utils.AlertUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ViewLeaveRequestsController {
    @FXML
    private VBox cardsContainer;

    private LeaveRequestService leaveRequestService = new LeaveRequestService();

    @FXML
    public void initialize() {
        // Fetch all leave requests
        List<LeaveRequest> leaveRequests = leaveRequestService.getAllLeaveRequests();

        // Filter leave requests by the logged-in company's ID
        int loggedInCompanyId = AuthCompanyController.getLoggedInCompanyId();
        List<LeaveRequest> filteredRequests = leaveRequests.stream()
                .filter(request -> request.getCompanyId() == loggedInCompanyId)
                .collect(Collectors.toList());

        // Create a card for each filtered leave request
        for (LeaveRequest request : filteredRequests) {
            VBox card = createCard(request);
            cardsContainer.getChildren().add(card);
        }
    }

    private VBox createCard(LeaveRequest request) {
        // Create a card layout
        VBox card = new VBox();
        card.setSpacing(10);
        card.setStyle("-fx-padding: 15; -fx-background-color: rgba(30, 41, 59, 0.7); -fx-border-radius: 10; -fx-border-color: rgba(59, 130, 246, 0.2); -fx-background-radius: 10;");
        card.prefWidthProperty().bind(cardsContainer.widthProperty().subtract(20)); // Adjust for padding

        // Leave Request Details
        Label idLabel = new Label("ID: " + request.getId());
        idLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #f8fafc;");

        Label employeeIdLabel = new Label("Employee ID: " + request.getEmployeeId());
        employeeIdLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        Label startDateLabel = new Label("Start Date: " + request.getStartDate());
        startDateLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        Label endDateLabel = new Label("End Date: " + request.getEndDate());
        endDateLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        Label descriptionLabel = new Label("Description: " + request.getDescription());
        descriptionLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        Label leaveTypeLabel = new Label("Leave Type: " + request.getLeaveType());
        leaveTypeLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        Label confirmedLabel = new Label("Confirmed: " + (request.isConfirmed() ? "Yes" : "No"));
        confirmedLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // View Details Button
        Button viewDetailsButton = new Button("View Details");
        viewDetailsButton.setStyle("-fx-text-fill: white; -fx-background-color: #3b82f6; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");
        viewDetailsButton.setOnAction(event -> openDetailsPopup(request));

        // View PDF Hyperlink
        Hyperlink pdfLink = new Hyperlink("View PDF");
        pdfLink.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #3b82f6;");
        pdfLink.setOnAction(event -> openPdfViewer(request.getPdfPath()));

        // Add all elements to the card
        card.getChildren().addAll(idLabel, employeeIdLabel, startDateLabel, endDateLabel, descriptionLabel, leaveTypeLabel, confirmedLabel, viewDetailsButton, pdfLink);
        return card;
    }

    @FXML
    public void handleBackToMain() {
        try {
            // Load the main interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/leave_request.fxml"));
            VBox root = loader.load();

            // Switch to the main interface
            Stage stage = (Stage) cardsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            AlertUtils.showError("Failed to navigate back. Please try again.");
            e.printStackTrace();
        }
    }

    private void openDetailsPopup(LeaveRequest request) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/details_popup.fxml"));
            VBox root = loader.load();

            DetailsPopupController controller = loader.getController();
            controller.setLeaveRequest(request); // Pass the leave request to the controller

            // Open a dialog window
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Leave Request Details");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            AlertUtils.showError("Failed to open details pop-up. Please try again.");
            e.printStackTrace();
        }
    }

    private void openPdfViewer(String pdfPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/pdf_viewer.fxml"));
            VBox root = loader.load();

            PdfViewerController controller = loader.getController();
            controller.loadPdf(pdfPath);

            // Open a new PDF viewer window
            Stage stage = new Stage();
            stage.setTitle("PDF Viewer");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            AlertUtils.showError("Failed to open PDF viewer. Please try again.");
            e.printStackTrace();
        }
    }
}