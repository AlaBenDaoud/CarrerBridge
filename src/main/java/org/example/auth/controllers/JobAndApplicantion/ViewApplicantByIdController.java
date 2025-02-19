package org.example.auth.controllers.JobAndApplicantion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.auth.models.Applicant;
import org.example.auth.services.ApplicantService;
import org.example.auth.controllers.connexion.AuthUserController;

public class ViewApplicantByIdController {

    // FXML Components
    @FXML
    private TextField applicantIdField;

    @FXML
    private TableView<Applicant> applicationsTable;

    @FXML
    private TableColumn<Applicant, Integer> idColumn;

    @FXML
    private TableColumn<Applicant, Integer> jobIdColumn;

    @FXML
    private TableColumn<Applicant, Integer> companyIdColumn;

    @FXML
    private TableColumn<Applicant, String> commentColumn;

    @FXML
    private TableColumn<Applicant, String> additionalFileColumn;

    @FXML
    private TableColumn<Applicant, String> statusColumn;  // Added column for status

    @FXML
    private TableColumn<Applicant, String> appliedDateColumn;

    @FXML
    private Button fetchButton;

    @FXML
    private VBox detailsContainer;  // Ensure this is injected correctly

    // Services
    private ApplicantService applicantService = new ApplicantService();

    // Initialize the controller
    @FXML
    public void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        jobIdColumn.setCellValueFactory(new PropertyValueFactory<>("jobId"));
        companyIdColumn.setCellValueFactory(new PropertyValueFactory<>("companyId"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        additionalFileColumn.setCellValueFactory(new PropertyValueFactory<>("additionalFile"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));  // Set status column
        appliedDateColumn.setCellValueFactory(new PropertyValueFactory<>("appliedDate"));

        // Automatically populate applicantIdField with the logged-in user's ID
        int loggedInUserId = AuthUserController.getLoggedInUserId();
        applicantIdField.setText(String.valueOf(loggedInUserId));
        applicantIdField.setEditable(false);  // Make it non-editable since it's auto-filled

        // Automatically fetch and display applications for the logged-in user
        handleFetchApplicant();
    }

    // Fetch and display all applications for the given applicant ID
    @FXML
    private void handleFetchApplicant() {
        String applicantId = applicantIdField.getText();
        if (applicantId != null && !applicantId.isEmpty()) {
            int id = Integer.parseInt(applicantId);
            ObservableList<Applicant> applications = FXCollections.observableArrayList(applicantService.getApplicationsByApplicantId(id));

            if (!applications.isEmpty()) {
                // Populate the table with applications
                applicationsTable.setItems(applications);
                applicationsTable.setVisible(true);
                detailsContainer.setVisible(true);
            } else {
                System.out.println("No applications found for Applicant ID: " + id);
                applicationsTable.setVisible(false);
                detailsContainer.setVisible(false);
            }
        }
    }

    // Delete an application
    @FXML
    private void handleDeleteApplication() {
        Applicant selectedApplicant = applicationsTable.getSelectionModel().getSelectedItem();
        if (selectedApplicant != null) {
            boolean success = applicantService.deleteApplicantById(selectedApplicant.getId());
            if (success) {
                System.out.println("Application deleted successfully.");
                handleFetchApplicant();  // Refresh the table
            } else {
                System.out.println("Failed to delete application.");
            }
        } else {
            System.out.println("No application selected.");
        }
    }

    // Edit an application (enable editing in the table)
    @FXML
    private void handleEditApplication() {
        Applicant selectedApplicant = applicationsTable.getSelectionModel().getSelectedItem();
        if (selectedApplicant != null) {
            // Enable editing in the table
            applicationsTable.setEditable(true);
            commentColumn.setEditable(true);
            additionalFileColumn.setEditable(true);
            System.out.println("Editing application with ID: " + selectedApplicant.getId());
        } else {
            System.out.println("No application selected.");
        }
    }

    // Save changes to an application
    @FXML
    private void handleSaveApplication() {
        Applicant selectedApplicant = applicationsTable.getSelectionModel().getSelectedItem();
        if (selectedApplicant != null) {
            // Update the selected applicant object with edited values
            selectedApplicant.setComment(commentColumn.getCellData(selectedApplicant));
            selectedApplicant.setAdditionalFile(additionalFileColumn.getCellData(selectedApplicant));

            // Save changes to the database
            boolean success = applicantService.updateApplicant(selectedApplicant);
            if (success) {
                System.out.println("Application updated successfully.");
                applicationsTable.setEditable(false);  // Disable editing after saving
            } else {
                System.out.println("Failed to update application.");
            }
        } else {
            System.out.println("No application selected.");
        }
    }
}
