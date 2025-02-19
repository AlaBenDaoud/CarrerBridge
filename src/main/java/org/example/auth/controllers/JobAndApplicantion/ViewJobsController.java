package org.example.auth.controllers.JobAndApplicantion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.auth.models.Job;
import org.example.auth.services.JobService;
import org.example.auth.utils.AlertUtils;

import java.io.IOException;
import java.util.List;

public class ViewJobsController {

    @FXML
    private TableView<Job> jobsTable;

    @FXML
    private TableColumn<Job, Integer> idColumn;

    @FXML
    private TableColumn<Job, String> titleColumn;

    @FXML
    private TableColumn<Job, String> companyColumn;

    @FXML
    private TableColumn<Job, String> locationColumn;

    @FXML
    private TableColumn<Job, String> postedDateColumn;

    @FXML
    private TableColumn<Job, Void> actionColumn;

    @FXML
    private Button backButton;

    private final JobService jobService = new JobService();

    @FXML
    public void initialize() {
        // Set up column mappings
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("position")); // Map to 'position' field
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        postedDateColumn.setCellValueFactory(new PropertyValueFactory<>("postedDate"));

        // Add "View Details" button to the action column
        addViewDetailsButton();

        // Load jobs into the table
        loadJobs();
    }

    /**
     * Loads jobs from the database into the TableView.
     */
    private void loadJobs() {
        try {
            List<Job> jobs = jobService.getAllJobs(); // Fetch jobs from the database
            jobsTable.setItems(javafx.collections.FXCollections.observableArrayList(jobs));
        } catch (Exception e) {
            AlertUtils.showError("Failed to load jobs. Please try again.");
            e.printStackTrace();
        }
    }

    /**
     * Adds a "View Details" button to the action column.
     */
    private void addViewDetailsButton() {
        Callback<TableColumn<Job, Void>, TableCell<Job, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Job, Void> call(final TableColumn<Job, Void> param) {
                return new TableCell<>() {
                    private final Button viewButton = new Button("View Details");

                    {
                        viewButton.setOnAction(event -> {
                            Job job = getTableView().getItems().get(getIndex());
                            openJobDetails(job.getId(), event);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(viewButton);
                        }
                    }
                };
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    /**
     * Opens the job details view for the selected job.
     *
     * @param jobId  The ID of the job to view.
     * @param event  The action event.
     */
    private void openJobDetails(int jobId, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/job_detail.fxml"));
            Parent root = loader.load();

            // Pass job ID to the JobDetailController
            JobDetailController controller = loader.getController();
            controller.loadJobDetails(jobId);

            // Load new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            AlertUtils.showError("Failed to open job details. Please try again.");
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Back" button click event.
     *
     * @param event The action event.
     */
    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/userdash.fxml"));
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
