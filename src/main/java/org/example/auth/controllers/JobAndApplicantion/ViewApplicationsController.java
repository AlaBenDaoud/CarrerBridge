package org.example.auth.controllers.JobAndApplicantion;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ComboBox;
import org.example.auth.models.Applicant;
import org.example.auth.models.Employee;
import org.example.auth.services.ApplicantService;
import org.example.auth.services.EmployeeService;
import org.example.auth.controllers.connexion.AuthCompanyController;

import java.util.List;

public class ViewApplicationsController {

    @FXML
    private TableView<Applicant> applicationsTable;

    @FXML
    private TableColumn<Applicant, Integer> idColumn;

    @FXML
    private TableColumn<Applicant, Integer> userIdColumn;

    @FXML
    private TableColumn<Applicant, Integer> jobIdColumn;

    @FXML
    private TableColumn<Applicant, Integer> companyIdColumn;

    @FXML
    private TableColumn<Applicant, String> commentColumn;

    @FXML
    private TableColumn<Applicant, String> additionalFileColumn;

    @FXML
    private TableColumn<Applicant, String> appliedDateColumn;

    @FXML
    private TableColumn<Applicant, String> statusColumn;

    @FXML
    private ComboBox<String> statusComboBox;

    private ApplicantService applicantService = new ApplicantService();
    private EmployeeService employeeService = new EmployeeService();

    /**
     * Initializes the controller and fetches applications automatically.
     */
    @FXML
    public void initialize() {
        // Bind table columns to Applicant properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        jobIdColumn.setCellValueFactory(new PropertyValueFactory<>("jobId"));
        companyIdColumn.setCellValueFactory(new PropertyValueFactory<>("companyId"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        additionalFileColumn.setCellValueFactory(new PropertyValueFactory<>("additionalFile"));
        appliedDateColumn.setCellValueFactory(new PropertyValueFactory<>("appliedDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set up status options in the ComboBox
        statusComboBox.getItems().addAll("Pending", "Rejected", "Scheduled for Interview", "Employed");

        // Fetch applications automatically on initialization
        handleFetchApplications();
    }

    private void handleFetchApplications() {
        // Get the logged-in company ID from AuthCompanyController
        int companyId = AuthCompanyController.getLoggedInCompanyId();

        // If company ID is valid, fetch the applications for that company
        if (companyId != -1) {
            List<Applicant> applications = applicantService.getApplicationsByCompanyId(companyId);
            applicationsTable.getItems().setAll(applications); // Populate the table with fetched applications
        } else {
            // Handle the case when the company ID is invalid or not set (e.g., not logged in)
            System.out.println("Company is not logged in or ID is invalid.");
        }
    }

    // Handle status change for selected application
    @FXML
    private void handleChangeStatus() {
        Applicant selectedApplicant = applicationsTable.getSelectionModel().getSelectedItem();
        if (selectedApplicant != null) {
            // Get the selected status from the ComboBox
            String newStatus = statusComboBox.getValue();
            if (newStatus != null) {
                // Set the new status for the selected applicant
                selectedApplicant.setStatus(newStatus);

                // If the new status is "Employed", save the user and company information to the Employee table
                if ("Employed".equals(newStatus)) {
                    boolean success = saveToEmployeeTable(selectedApplicant);
                    if (success) {
                        System.out.println("Employee information saved successfully.");
                    } else {
                        System.out.println("Failed to save employee information.");
                    }
                }

                // Update the status in the database
                boolean success = applicantService.updateApplicant(selectedApplicant);
                if (success) {
                    System.out.println("Status updated successfully.");
                    applicationsTable.refresh();  // Refresh table to reflect the updated status
                } else {
                    System.out.println("Failed to update status.");
                }
            }
        } else {
            System.out.println("No application selected.");
        }
    }

    private boolean saveToEmployeeTable(Applicant selectedApplicant) {
        // Extract userId, companyId, and jobId from the selected applicant
        int userId = selectedApplicant.getUserId();
        int companyId = AuthCompanyController.getLoggedInCompanyId();  // Fetch the companyId from AuthCompanyController
        int jobId = selectedApplicant.getJobId();  // Extract jobId from the selected applicant

        // Create an instance of Employee with the jobId
        Employee employee = new Employee(0, companyId, userId, jobId);  // Assuming `id` is auto-generated

        // Use EmployeeService to add the employee to the Employee table
        boolean success = employeeService.addEmployee(employee);
        return success;
    }

}
