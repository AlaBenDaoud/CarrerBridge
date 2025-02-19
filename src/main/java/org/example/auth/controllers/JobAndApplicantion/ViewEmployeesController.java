package org.example.auth.controllers.JobAndApplicantion;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.auth.controllers.connexion.AuthCompanyController;
import org.example.auth.models.Employee;
import org.example.auth.services.EmployeeService;

import java.util.List;

public class ViewEmployeesController {

    @FXML
    private TableView<Employee> employeesTable;

    @FXML
    private TableColumn<Employee, Integer> idColumn;

    @FXML
    private TableColumn<Employee, Integer> userIdColumn;

    @FXML
    private TableColumn<Employee, Integer> companyIdColumn;

    @FXML
    private TableColumn<Employee, Integer> jobIdColumn; // Add the jobId column

    @FXML
    private Button deleteEmployeeButton;

    private EmployeeService employeeService = new EmployeeService();

    @FXML
    public void initialize() {
        // Bind table columns to Employee properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        companyIdColumn.setCellValueFactory(new PropertyValueFactory<>("companyId"));
        jobIdColumn.setCellValueFactory(new PropertyValueFactory<>("jobId")); // Bind jobId column

        // Fetch employees automatically on initialization
        handleFetchEmployees();
    }

    private void handleFetchEmployees() {
        // Get the logged-in company ID from AuthCompanyController
        int companyId = AuthCompanyController.getLoggedInCompanyId();

        // If company ID is valid, fetch the employees for that company
        if (companyId != -1) {
            List<Employee> employees = employeeService.getEmployeesByCompanyId(companyId);
            employeesTable.getItems().setAll(employees); // Populate the table with fetched employees
        } else {
            // Handle the case when the company ID is invalid or not set (e.g., not logged in)
            System.out.println("Company is not logged in or ID is invalid.");
        }
    }

    @FXML
    private void handleDeleteEmployee() {
        Employee selectedEmployee = employeesTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            boolean success = employeeService.deleteEmployeeAndApplication(selectedEmployee.getId());
            if (success) {
                // Refresh the table after successful deletion
                employeesTable.getItems().remove(selectedEmployee);
                showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", "Employee and their application have been deleted.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Failed to delete employee and their application.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Employee Selected", "Please select an employee to delete.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
