package org.example.auth.controllers.Congee;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.auth.models.LeaveRequest;
import org.example.auth.services.LeaveRequestService;

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

        // Disable editing for the ComboBox and DatePickers
        leaveTypeComboBox.setEditable(false);
        leaveTypeComboBox.setMouseTransparent(true);
        leaveTypeComboBox.setFocusTraversable(false);

        startDatePicker.setEditable(false);
        startDatePicker.setMouseTransparent(true);
        startDatePicker.setFocusTraversable(false);

        endDatePicker.setEditable(false);
        endDatePicker.setMouseTransparent(true);
        endDatePicker.setFocusTraversable(false);
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
        leaveRequest.setDescription(descriptionArea.getText());
        leaveRequest.setPdfPath(pdfPathLabel.getText());

        // Save changes to the database
        leaveRequestService.updateLeaveRequest(leaveRequest);

        // Close the window
        ((Stage) startDatePicker.getScene().getWindow()).close();
    }
}