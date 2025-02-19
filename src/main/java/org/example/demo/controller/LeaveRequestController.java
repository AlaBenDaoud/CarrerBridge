package org.example.demo.controller;

import org.example.demo.model.LeaveRequest;
import org.example.demo.service.LeaveRequestService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.io.File;
import java.time.LocalDate;

public class LeaveRequestController {
    @FXML
    private TextField employeeIdField;
    @FXML
    private TextField companyIdField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ComboBox<String> leaveTypeComboBox;
    @FXML
    private Label pdfPathLabel;
    @FXML
    private Button submitButton;

    private LeaveRequestService leaveRequestService = new LeaveRequestService();

    @FXML
    public void initialize() {
        // Populate leave type options
        leaveTypeComboBox.getItems().addAll("maladie", "maternite", "paternite", "normal");

        // Disable submit button initially
        submitButton.setDisable(true);

        // Add listeners to enable/disable submit button based on input
        employeeIdField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        companyIdField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        descriptionArea.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        leaveTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        pdfPathLabel.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
    }

    @FXML
    public void handleUploadPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            pdfPathLabel.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    public void handleSubmit() {//Méthode appelée lors du clic sur le bouton de soumission
        try {
            int employeeId = Integer.parseInt(employeeIdField.getText());
            int companyId = Integer.parseInt(companyIdField.getText());
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String description = descriptionArea.getText();
            String leaveType = leaveTypeComboBox.getValue();
            String pdfPath = pdfPathLabel.getText();

            // Validate required fields
            if (startDate == null || endDate == null || leaveType == null || pdfPath.isEmpty()) {
                showAlert("Error", "Please fill in all required fields.");
                return;
            }

            if (startDate.isAfter(endDate)) {
                showAlert("Error", "Start date must be before end date.");
                return;
            }

            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.setEmployeeId(employeeId);
            leaveRequest.setCompanyId(companyId);
            leaveRequest.setStartDate(startDate);
            leaveRequest.setEndDate(endDate);
            leaveRequest.setDescription(description);
            leaveRequest.setLeaveType(leaveType);
            leaveRequest.setPdfPath(pdfPath);

            leaveRequestService.addLeaveRequest(leaveRequest);

            // Clear form
            employeeIdField.clear();
            companyIdField.clear();
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            descriptionArea.clear();
            leaveTypeComboBox.setValue(null);
            pdfPathLabel.setText("");

            showAlert("Success", "Leave request submitted successfully!");
        } catch (NumberFormatException e) {
            showAlert("Error", "Employee ID and Company ID must be valid numbers.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred while submitting the leave request.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleViewAllLeaveRequests() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/views/view_leave_requests.fxml"));
            VBox root = loader.load();

            Stage stage = (Stage) employeeIdField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleViewMyRequests() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/views/view_my_requests_popup.fxml"));
            //Récupère l'URL du fichier FXML
            VBox root = loader.load();// crée l'interface utilisateur à partir du fichier FXML

            Stage popupStage = new Stage();
            popupStage.setTitle("View My Requests");
            popupStage.setScene(new Scene(root));

            popupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateInputs() {
        boolean isValid = !employeeIdField.getText().isEmpty() &&
                !companyIdField.getText().isEmpty() &&
                startDatePicker.getValue() != null &&
                endDatePicker.getValue() != null &&
                !descriptionArea.getText().isEmpty() &&
                leaveTypeComboBox.getValue() != null &&
                !pdfPathLabel.getText().isEmpty();

        submitButton.setDisable(!isValid);
    }
    @FXML
    public void handleViewAllRemoteWork() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/views/view_remote_work.fxml"));
            VBox root = loader.load();

            Stage stage = (Stage) employeeIdField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}