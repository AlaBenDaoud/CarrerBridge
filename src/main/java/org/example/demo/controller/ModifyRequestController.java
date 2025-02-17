package org.example.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.demo.model.LeaveRequest;
import org.example.demo.service.LeaveRequestService;

import java.io.File;

public class ModifyRequestController {
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

    private LeaveRequest leaveRequest;
    private LeaveRequestService leaveRequestService = new LeaveRequestService();

    @FXML
    public void initialize() {
        // Populate the leave type ComboBox
        leaveTypeComboBox.getItems().addAll("maladie", "maternite", "paternite", "normal");
    }

    public void setLeaveRequest(LeaveRequest request) {
        this.leaveRequest = request;
        // Populate fields with the current request data
        startDatePicker.setValue(request.getStartDate());
        endDatePicker.setValue(request.getEndDate());
        descriptionArea.setText(request.getDescription());
        leaveTypeComboBox.setValue(request.getLeaveType());
        pdfPathLabel.setText(request.getPdfPath());
    }

    @FXML
    public void handleUploadPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(pdfPathLabel.getScene().getWindow());
        if (selectedFile != null) {
            pdfPathLabel.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    public void handleSaveChanges() {
        // Update the leave request
        leaveRequest.setStartDate(startDatePicker.getValue());
        leaveRequest.setEndDate(endDatePicker.getValue());
        leaveRequest.setDescription(descriptionArea.getText());
        leaveRequest.setLeaveType(leaveTypeComboBox.getValue());
        leaveRequest.setPdfPath(pdfPathLabel.getText());

        // Save changes to the database
        leaveRequestService.updateLeaveRequest(leaveRequest);

        // Close the window
        ((Stage) startDatePicker.getScene().getWindow()).close();
    }
}