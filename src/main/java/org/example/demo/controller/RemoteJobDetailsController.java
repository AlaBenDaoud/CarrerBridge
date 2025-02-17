package org.example.demo.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.model.OnlineJob;
import org.example.demo.service.OnlineJobService;

public class RemoteJobDetailsController {

    @FXML
    private Label titleLabel;

    @FXML
    private TextArea postArea;

    @FXML
    private Label startDateLabel;

    @FXML
    private Label endDateLabel;

    @FXML
    private Label confirmedLabel;

    private OnlineJob remoteJob;
    private OnlineJobService onlineJobService = new OnlineJobService();

    public void setRemoteJob(OnlineJob remoteJob) {
        this.remoteJob = remoteJob;
        populateFields();
    }

    private void populateFields() {
        if (remoteJob != null) {
            titleLabel.setText(remoteJob.getTitle());
            postArea.setText(remoteJob.getPost());
            startDateLabel.setText(remoteJob.getStartDate().toString());
            endDateLabel.setText(remoteJob.getEndDate().toString());
            confirmedLabel.setText(remoteJob.isConfirmed() ? "Yes" : "No");
        }
    }

    @FXML
    private void handleModify() {
        try {
            // Open the modify remote job window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/views/modify_remote_work.fxml"));
            VBox root = loader.load();

            ModifyRemoteWorkController controller = loader.getController();
            controller.setOnlineJob(remoteJob);

            Stage stage = new Stage();
            stage.setTitle("Modify Remote Job");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh the details after modification
            setRemoteJob(onlineJobService.getOnlineJobById(remoteJob.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        // Delete the remote job from the database
        onlineJobService.deleteOnlineJob(remoteJob.getId());

        // Close the details window
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}