package org.example.auth.controllers.Congee;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.auth.models.Employee;
import org.example.auth.services.EmployeeService;
import org.example.auth.controllers.connexion.AuthUserController;

import java.util.List;

public class EmployeeListController {

    @FXML
    private ListView<Employee> employeeListView;

    private EmployeeService employeeService;

    public EmployeeListController() {
        this.employeeService = new EmployeeService();
    }

    @FXML
    public void initialize() {
        // Fetch employees based on the logged-in user's ID and update the ListView
        int loggedInUserId = AuthUserController.getLoggedInUserId();
        List<Employee> employees = employeeService.getEmployeesByUserId(loggedInUserId);
        setEmployees(employees);

        // Set custom cell factory
        employeeListView.setCellFactory(new Callback<ListView<Employee>, ListCell<Employee>>() {
            @Override
            public ListCell<Employee> call(ListView<Employee> param) {
                return new EmployeeListCell();
            }
        });
    }

    public void setEmployees(List<Employee> employees) {
        employeeListView.getItems().clear();
        employeeListView.getItems().addAll(employees);
    }

    // Custom ListCell to display employee information and a button
    private class EmployeeListCell extends ListCell<Employee> {
        @Override
        protected void updateItem(Employee employee, boolean empty) {
            super.updateItem(employee, empty);

            if (empty || employee == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Create a button for submitting a leave request
                Button submitButton = new Button("Submit Leave Request");
                submitButton.setOnAction(event -> {
                    // Open the leave request form with the employee's ID and company ID
                    openLeaveRequestForm(employee.getId(), employee.getCompanyId());
                });

                // Create a label to display employee information
                Label employeeLabel = new Label(employee.toString());

                // Create an HBox to hold the label and button
                HBox hbox = new HBox(10, employeeLabel, submitButton);
                hbox.setAlignment(Pos.CENTER_LEFT);

                // Set the HBox as the graphic for the cell
                setGraphic(hbox);
            }
        }
    }

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
}