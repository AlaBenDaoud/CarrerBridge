package org.example.auth.controllers.JobAndApplicantion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.auth.models.Job;
import org.example.auth.services.JobService;
import org.example.auth.utils.AlertUtils;
import org.example.auth.controllers.connexion.AuthCompanyController;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class RHViewDetailsController implements Initializable {

    @FXML
    private TableView<Job> jobsTable;

    @FXML
    private TableColumn<Job, Integer> idColumn;

    @FXML
    private TableColumn<Job, String> titleColumn;

    @FXML
    private TableColumn<Job, String> descriptionColumn;

    @FXML
    private TableColumn<Job, String> locationColumn;

    @FXML
    private TableColumn<Job, Timestamp> postedDateColumn;

    @FXML
    private TableColumn<Job, Void> actionsColumn;

    private JobService jobService = new JobService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        postedDateColumn.setCellValueFactory(new PropertyValueFactory<>("postedDate"));

        // Add buttons to the actions column
        addButtonsToTable();

        // Fetch jobs as soon as the view is loaded
        handleFetchJobs();
    }

    @FXML
    private void handleFetchJobs() {
        // Retrieve the logged-in company ID from the AuthCompanyController
        int companyId = AuthCompanyController.getLoggedInCompanyId();

        if (companyId != -1) {
            // Fetch jobs for the logged-in company
            ObservableList<Job> jobs = FXCollections.observableArrayList(jobService.getJobsByCompanyId(companyId));
            jobsTable.setItems(jobs);
        } else {
            AlertUtils.showError("Company not logged in. Please log in first.");
        }
    }

    private void addButtonsToTable() {
        Callback<TableColumn<Job, Void>, TableCell<Job, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Job, Void> call(final TableColumn<Job, Void> param) {
                return new TableCell<>() {
                    private final Button modifyButton = new Button("Modify");
                    private final Button deleteButton = new Button("Delete");

                    {
                        // Modify button action
                        modifyButton.setOnAction(event -> {
                            Job job = getTableView().getItems().get(getIndex());
                            handleModifyJob(job);
                        });

                        // Delete button action
                        deleteButton.setOnAction(event -> {
                            Job job = getTableView().getItems().get(getIndex());
                            handleDeleteJob(job);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(new javafx.scene.layout.HBox(modifyButton, deleteButton));
                        }
                    }
                };
            }
        };

        actionsColumn.setCellFactory(cellFactory);
    }

    private void handleModifyJob(Job job) {
        try {
            // Load the edit form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/jobboardapp/views/edit_job.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the job to it
            EditJobController controller = loader.getController();
            controller.initializeData(job);

            // Create a new stage for the edit form
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 500));
            stage.setTitle("Edit Job");
            stage.show();
        } catch (IOException e) {
            AlertUtils.showError("Failed to open edit form. Please try again.");
            e.printStackTrace();
        }
    }

    private void handleDeleteJob(Job job) {
        boolean isDeleted = jobService.deleteJob(job.getId());
        if (isDeleted) {
            jobsTable.getItems().remove(job);
            System.out.println("Job deleted: " + job.getId());
        } else {
            System.out.println("Failed to delete job: " + job.getId());
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/jobboardapp/views/main.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            AlertUtils.showError("Failed to navigate back. Please try again.");
            e.printStackTrace();
        }
    }
}
