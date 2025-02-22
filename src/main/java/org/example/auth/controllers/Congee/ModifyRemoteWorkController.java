package org.example.auth.controllers.Congee;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.auth.models.OnlineJob;
import org.example.auth.services.OnlineJobService;

public class ModifyRemoteWorkController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea postField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    private OnlineJob onlineJob;
    private OnlineJobService onlineJobService = new OnlineJobService();

    public void setOnlineJob(OnlineJob onlineJob) {
        this.onlineJob = onlineJob;
        populateFields();
    }

    private void populateFields() {
        if (onlineJob != null) {
            titleField.setText(onlineJob.getTitle());
            postField.setText(onlineJob.getPost());
            startDatePicker.setValue(onlineJob.getStartDate());
            endDatePicker.setValue(onlineJob.getEndDate());
        }
    }

    @FXML
    private void handleSave() {
        onlineJob.setTitle(titleField.getText());
        onlineJob.setPost(postField.getText());
        onlineJob.setStartDate(startDatePicker.getValue());
        onlineJob.setEndDate(endDatePicker.getValue());

        onlineJobService.updateOnlineJob(onlineJob);

        // Close the window
        titleField.getScene().getWindow().hide();
    }
}