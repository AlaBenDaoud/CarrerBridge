package org.example.demo.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.example.demo.model.OnlineJob;
import org.example.demo.service.OnlineJobService;

import java.util.List;

public class ViewRemoteWorkController {

    @FXML
    private TableView<OnlineJob> remoteWorkTable;

    @FXML
    private TableColumn<OnlineJob, Integer> idColumn;

    @FXML
    private TableColumn<OnlineJob, Integer> leaveRequestIdColumn;

    @FXML
    private TableColumn<OnlineJob, String> titleColumn;

    @FXML
    private TableColumn<OnlineJob, String> postColumn;

    @FXML
    private TableColumn<OnlineJob, String> startDateColumn;

    @FXML
    private TableColumn<OnlineJob, String> endDateColumn;

    @FXML
    private TableColumn<OnlineJob, Boolean> isConfirmedColumn;

    @FXML
    private TableColumn<OnlineJob, Void> actionsColumn;

    private OnlineJobService onlineJobService = new OnlineJobService();

    @FXML
    public void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        leaveRequestIdColumn.setCellValueFactory(new PropertyValueFactory<>("leaveRequestId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        postColumn.setCellValueFactory(new PropertyValueFactory<>("post"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        isConfirmedColumn.setCellValueFactory(new PropertyValueFactory<>("confirmed"));

        // Set up actions column
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button confirmButton = new Button();

            {
                // Confirm/Not Confirm button action
                confirmButton.setOnAction(event -> {
                    OnlineJob onlineJob = getTableView().getItems().get(getIndex());
                    onlineJob.setConfirmed(!onlineJob.isConfirmed()); // Toggle the confirmation status
                    onlineJobService.updateOnlineJob(onlineJob);
                    refreshTable(); // Refresh the table to reflect the changes
                });

                // Style button
                confirmButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 5 10;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    OnlineJob onlineJob = getTableView().getItems().get(getIndex());
                    // Set the button text based on the confirmation status
                    if (onlineJob.isConfirmed()) {
                        confirmButton.setText("Not Confirm");
                    } else {
                        confirmButton.setText("Confirm");
                    }
                    setGraphic(new HBox(5, confirmButton));
                }
            }
        });

        // Load data into the table
        loadRemoteWorkData();
    }

    private void loadRemoteWorkData() {
        List<OnlineJob> onlineJobs = onlineJobService.getAllOnlineJobs();
        remoteWorkTable.getItems().clear();
        remoteWorkTable.getItems().addAll(onlineJobs);
    }

    private void refreshTable() {
        loadRemoteWorkData();
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/views/leave_request.fxml"));
            VBox root = loader.load();

            Stage stage = (Stage) remoteWorkTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}