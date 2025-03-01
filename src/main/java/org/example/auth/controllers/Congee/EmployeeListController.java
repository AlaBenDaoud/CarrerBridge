package org.example.auth.controllers.Congee;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.auth.models.Employee;
import org.example.auth.services.EmployeeService;
import org.example.auth.controllers.connexion.AuthUserController;
import org.example.auth.controllers.Reclamation.ReclamationSubmissionController;

import java.util.List;

public class EmployeeListController {

    @FXML
    private VBox employeeContainer;

    private EmployeeService employeeService;

    public EmployeeListController() {
        this.employeeService = new EmployeeService();
    }

    @FXML
    public void initialize() {
        // Fetch employees based on the logged-in user's ID and update the card layout
        int loggedInUserId = AuthUserController.getLoggedInUserId();
        List<Employee> employees = employeeService.getEmployeesByUserId(loggedInUserId);
        loadEmployees(employees);
    }

    /**
     * Loads employees into the card layout.
     *
     * @param employees The list of employees to display.
     */
    private void loadEmployees(List<Employee> employees) {
        employeeContainer.getChildren().clear(); // Clear existing cards

        if (!employees.isEmpty()) {
            for (Employee employee : employees) {
                employeeContainer.getChildren().add(createEmployeeCard(employee));
            }
        } else {
            Label noEmployeesLabel = new Label("No employees found.");
            noEmployeesLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");
            employeeContainer.getChildren().add(noEmployeesLabel);
        }
    }

    /**
     * Creates a card for the given employee.
     *
     * @param employee The employee to display.
     * @return A VBox representing the employee card.
     */
    private VBox createEmployeeCard(Employee employee) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setStyle("-fx-padding: 15; -fx-background-color: rgba(30, 41, 59, 0.7); -fx-border-radius: 10; -fx-border-color: rgba(59, 130, 246, 0.2); -fx-background-radius: 10;");
        card.setPrefWidth(760);

        // Employee ID
        Label idLabel = new Label("ID: " + employee.getId());
        idLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Company ID
        Label companyIdLabel = new Label("Company ID: " + employee.getCompanyId());
        companyIdLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Submit Leave Request Button
        Button submitButton = new Button("Submit Leave Request");
        submitButton.setStyle("-fx-text-fill: white; -fx-background-color: #3b82f6; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");
        submitButton.setOnAction(event -> openLeaveRequestForm(employee.getId(), employee.getCompanyId()));

        // Submit Reclamation Button
        Button reclamationButton = new Button("Submit Reclamation");
        reclamationButton.setStyle("-fx-text-fill: white; -fx-background-color: #3b82f6; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");
        reclamationButton.setOnAction(event -> openReclamationForm(employee.getId(), employee.getCompanyId()));

        // Add all elements to the card
        card.getChildren().addAll(idLabel, companyIdLabel, submitButton, reclamationButton);
        return card;
    }

    /**
     * Opens the leave request form for the given employee.
     *
     * @param employeeId The ID of the employee.
     * @param companyId  The ID of the company.
     */
    private void openLeaveRequestForm(int employeeId, int companyId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/leave_request.fxml"));
            VBox root = loader.load();

            // Get the controller and set the employee and company IDs
            LeaveRequestController controller = loader.getController();
            controller.setEmployeeId(employeeId);
            controller.setCompanyId(companyId);

            // Create a new stage for the leave request form
            Stage stage = new Stage();
            stage.setTitle("Submit Leave Request");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the reclamation form for the given employee.
     *
     * @param employeeId The ID of the employee.
     * @param companyId  The ID of the company.
     */
    private void openReclamationForm(int employeeId, int companyId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/reclamation/ReclamationSubmissionView.fxml"));
            VBox root = loader.load();

            // Get the controller and set the employee and company IDs
            ReclamationSubmissionController controller = loader.getController();
            controller.setEmployeeId(employeeId);
            controller.setCompanyId(companyId);

            // Create a new stage for the reclamation form
            Stage stage = new Stage();
            stage.setTitle("Submit Reclamation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Back to Dashboard" button click event.
     */
    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/userdash.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) employeeContainer.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to navigate back to the dashboard: " + e.getMessage());
        }
    }
}