package org.example.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import org.example.demo.model.LeaveRequest;
import org.example.demo.service.LeaveRequestService;

import java.util.List;

public class ViewLeaveRequestsController {
    @FXML
    private VBox cardsContainer;

    private LeaveRequestService leaveRequestService = new LeaveRequestService();

    @FXML
    public void initialize() {
        // Fetch all leave requests
        List<LeaveRequest> leaveRequests = leaveRequestService.getAllLeaveRequests();

        // Create a card for each leave request
        for (LeaveRequest request : leaveRequests) {
            HBox card = createCard(request);
            cardsContainer.getChildren().add(card);
        }
    }

    private HBox createCard(LeaveRequest request) {
        // Create a card layout
        HBox card = new HBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        // Left side: Details
        VBox details = new VBox(5);
        details.getChildren().addAll(
                createLabel("ID: " + request.getId(), "-fx-font-size: 14px; -fx-text-fill: #555555;"),
                createLabel("Employee ID: " + request.getEmployeeId(), "-fx-font-size: 14px; -fx-text-fill: #555555;"),
                createLabel("Start Date: " + request.getStartDate(), "-fx-font-size: 14px; -fx-text-fill: #555555;"),
                createLabel("End Date: " + request.getEndDate(), "-fx-font-size: 14px; -fx-text-fill: #555555;"),
                createLabel("Description: " + request.getDescription(), "-fx-font-size: 14px; -fx-text-fill: #555555;"),
                createLabel("Leave Type: " + request.getLeaveType(), "-fx-font-size: 14px; -fx-text-fill: #555555;"),
                createLabel("Confirmed: " + (request.isConfirmed() ? "Yes" : "No"), "-fx-font-size: 14px; -fx-text-fill: #555555;")
        );

        // Right side: Actions
        VBox actions = new VBox(5);
        Hyperlink pdfLink = new Hyperlink("View PDF");
        pdfLink.setStyle("-fx-font-size: 14px; -fx-text-fill: #0078d7;");
        pdfLink.setOnAction(event -> openPdfViewer(request.getPdfPath()));

        Button viewDetailsButton = new Button("View Details");
        viewDetailsButton.setStyle("-fx-font-size: 14px; -fx-background-color: #0078d7; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 5 10;");
        viewDetailsButton.setOnAction(event -> openDetailsPopup(request));

        actions.getChildren().addAll(pdfLink, viewDetailsButton);

        // Add details and actions to the card
        card.getChildren().addAll(details, actions);
        return card;
    }

    private Label createLabel(String text, String style) {
        Label label = new Label(text);
        label.setStyle(style);
        return label;
    }

    @FXML
    public void handleBackToMain() {
        try {
            // Load the main interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/views/leave_request.fxml"));
            VBox root = loader.load();

            // Switch to the main interface
            Stage stage = (Stage) cardsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openDetailsPopup(LeaveRequest request) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/views/details_popup.fxml"));
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
            e.printStackTrace();
        }
    }

    private void openPdfViewer(String pdfPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/views/pdf_viewer.fxml"));
            VBox root = loader.load();

            PdfViewerController controller = loader.getController();
            controller.loadPdf(pdfPath);

            // Open a new PDF viewer window
            Stage stage = new Stage();
            stage.setTitle("PDF Viewer");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}