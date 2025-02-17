package org.example.demo.jobboardapp.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.demo.jobboardapp.Models.Applicant;
import org.example.demo.jobboardapp.Services.ApplicantService;

import java.util.List;

public class ViewApplicationsController {

    @FXML
    private TextField companyIdField;

    @FXML
    private TableView<Applicant> applicationsTable;

    @FXML
    private TableColumn<Applicant, Integer> idColumn;

    @FXML
    private TableColumn<Applicant, Integer> jobIdColumn;

    @FXML
    private TableColumn<Applicant, String> nameColumn;

    @FXML
    private TableColumn<Applicant, String> emailColumn;

    @FXML
    private TableColumn<Applicant, String> appliedDateColumn;

    private ApplicantService applicantService = new ApplicantService();

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        // Bind table columns to Applicant properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        jobIdColumn.setCellValueFactory(new PropertyValueFactory<>("jobId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        appliedDateColumn.setCellValueFactory(new PropertyValueFactory<>("appliedDate"));
    }

    @FXML
    private void handleFetchApplications() {
        String companyId = companyIdField.getText();
        if (companyId != null && !companyId.isEmpty()) {
            int id = Integer.parseInt(companyId);
            List<Applicant> applications = applicantService.getApplicationsByCompanyId(id);
            applicationsTable.getItems().setAll(applications); // Populate the table with fetched applications
        }
    }
}