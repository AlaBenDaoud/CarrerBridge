package org.example.auth.controllers.Congee;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.auth.models.LeaveRequest;
import org.example.auth.models.OnlineJob;
import org.example.auth.services.LeaveRequestService;
import org.example.auth.services.OnlineJobService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ViewMyRequestsPopupController {
    @FXML
    private TextField employeeIdField;
    @FXML
    private TableView<LeaveRequest> leaveRequestsTable;
    @FXML
    private TableColumn<LeaveRequest, Integer> idColumn;
    @FXML
    private TableColumn<LeaveRequest, String> startDateColumn;
    @FXML
    private TableColumn<LeaveRequest, String> endDateColumn;
    @FXML
    private TableColumn<LeaveRequest, String> leaveTypeColumn;
    @FXML
    private TableColumn<LeaveRequest, String> descriptionColumn;
    @FXML
    private TableColumn<LeaveRequest, String> pdfPathColumn;
    @FXML
    private TableColumn<LeaveRequest, Boolean> isConfirmedColumn;
    @FXML
    private TableColumn<LeaveRequest, Void> actionsColumn;
    @FXML
    private TableColumn<LeaveRequest, Void> viewRemoteJobColumn;
    @FXML
    private TableColumn<LeaveRequest, Void> applyJobColumn;
    @FXML
    private Label confirmedDaysLabel;
    @FXML
    private Label remainingDaysLabel;

    private LeaveRequestService leaveRequestService = new LeaveRequestService();
    private OnlineJobService onlineJobService = new OnlineJobService();

    @FXML
    public void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        leaveTypeColumn.setCellValueFactory(new PropertyValueFactory<>("leaveType"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        pdfPathColumn.setCellValueFactory(new PropertyValueFactory<>("pdfPath"));

        // Set up the isConfirmed column
        isConfirmedColumn.setCellValueFactory(new PropertyValueFactory<>("confirmed"));
        isConfirmedColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Confirmed" : "Not Confirmed");
                }
            }
        });

        // Set up PDF path column with a clickable hyperlink
        pdfPathColumn.setCellFactory(column -> new TableCell<>() {
            private final Hyperlink hyperlink = new Hyperlink();

            {
                hyperlink.setOnAction(event -> {
                    String pdfPath = getTableView().getItems().get(getIndex()).getPdfPath();
                    if (pdfPath != null && !pdfPath.isEmpty()) {
                        openPdfViewer(pdfPath);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    hyperlink.setText(item);
                    setGraphic(hyperlink);
                }
            }
        });

        // Set up actions column
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button modifyButton = new Button("Modify");

            {
                deleteButton.setOnAction(event -> {
                    LeaveRequest request = getTableView().getItems().get(getIndex());
                    leaveRequestService.deleteLeaveRequest(request.getId());
                    refreshTable();
                });

                modifyButton.setOnAction(event -> {
                    LeaveRequest request = getTableView().getItems().get(getIndex());
                    openModifyRequestWindow(request);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, deleteButton, modifyButton));
                }
            }
        });

        // Set up view remote job column
        viewRemoteJobColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewRemoteJobButton = new Button("View Remote Job");

            {
                viewRemoteJobButton.setOnAction(event -> {
                    LeaveRequest request = getTableView().getItems().get(getIndex());
                    OnlineJob remoteJob = onlineJobService.getOnlineJobByLeaveRequestId(request.getId());
                    if (remoteJob != null) {
                        openRemoteJobDetailsWindow(remoteJob);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    LeaveRequest request = getTableView().getItems().get(getIndex());
                    // Check if a remote job exists for this leave request
                    OnlineJob remoteJob = onlineJobService.getOnlineJobByLeaveRequestId(request.getId());
                    if (remoteJob != null) {
                        setGraphic(viewRemoteJobButton); // Show the button if a remote job exists
                    } else {
                        setGraphic(null); // Hide the button if no remote job exists
                    }
                }
            }
        });

        // Set up apply job column
        applyJobColumn.setCellFactory(param -> new TableCell<>() {
            private final Button applyButton = new Button("Apply Job");

            {
                applyButton.setOnAction(event -> {
                    LeaveRequest request = getTableView().getItems().get(getIndex());
                    openApplyRemoteWorkWindow(request);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    LeaveRequest request = getTableView().getItems().get(getIndex());
                    // Check if the request is confirmed and no remote job exists
                    OnlineJob existingJob = onlineJobService.getOnlineJobByLeaveRequestId(request.getId());
                    if (request.isConfirmed() && existingJob == null) {
                        setGraphic(applyButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    @FXML
    public void handleViewRequests() {
        try {
            int employeeId = Integer.parseInt(employeeIdField.getText());
            List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestsByEmployeeId(employeeId);
            leaveRequestsTable.getItems().clear();
            leaveRequestsTable.getItems().addAll(leaveRequests);

            // Calculate total confirmed days and remaining days
            int totalConfirmedDays = calculateTotalConfirmedDays(leaveRequests);
            int remainingDays = 18 - totalConfirmedDays; // Assuming 18 is the total allowed leave days

            // Update the labels
            confirmedDaysLabel.setText(String.valueOf(totalConfirmedDays));
            remainingDaysLabel.setText(String.valueOf(remainingDays));

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Employee ID", "Please enter a valid employee ID.");
        }
    }

    private int calculateTotalConfirmedDays(List<LeaveRequest> leaveRequests) {
        int totalConfirmedDays = 0;
        for (LeaveRequest request : leaveRequests) {
            if (request.isConfirmed() && "normal".equals(request.getLeaveType())) {
                LocalDate startDate = request.getStartDate();
                LocalDate endDate = request.getEndDate();
                totalConfirmedDays += calculateLeaveDays(startDate, endDate);
            }
        }
        return totalConfirmedDays;
    }

    private int calculateLeaveDays(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1; // +1 to include both start and end dates
    }

    private void openApplyRemoteWorkWindow(LeaveRequest request) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/apply_remote_work.fxml"));
            VBox root = loader.load();

            ApplyRemoteWorkController controller = loader.getController();
            controller.setLeaveRequest(request);

            Stage stage = new Stage();
            stage.setTitle("Apply Remote Work");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        int employeeId = Integer.parseInt(employeeIdField.getText());
        List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestsByEmployeeId(employeeId);
        leaveRequestsTable.getItems().clear();
        leaveRequestsTable.getItems().addAll(leaveRequests);
    }

    private void openModifyRequestWindow(LeaveRequest request) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/modify_request.fxml"));
            VBox root = loader.load();

            ModifyRequestController controller = loader.getController();
            controller.setLeaveRequest(request);

            Stage stage = new Stage();
            stage.setTitle("Modify Leave Request");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openRemoteJobDetailsWindow(OnlineJob remoteJob) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/remote_job_details.fxml"));
            VBox root = loader.load();

            RemoteJobDetailsController controller = loader.getController();
            controller.setRemoteJob(remoteJob);

            Stage stage = new Stage();
            stage.setTitle("Remote Job Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openPdfViewer(String pdfPath) {
        try {
            System.out.println("Opening PDF: " + pdfPath);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/pdf_viewer.fxml"));
            VBox root = loader.load();

            PdfViewerController controller = loader.getController();
            controller.loadPdf(pdfPath);

            Stage stage = new Stage();
            stage.setTitle("PDF Viewer");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}