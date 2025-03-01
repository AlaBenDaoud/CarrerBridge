package org.example.auth.controllers.Congee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.shape.Circle;
import org.example.auth.controllers.connexion.AuthCompanyController;
import org.example.auth.models.Employee;
import org.example.auth.models.LeaveRequest;
import org.example.auth.services.EmployeeService;
import org.example.auth.services.LeaveRequestService;

import java.util.Comparator;
import java.util.List;

public class EmployeeRankingsController {

    private final EmployeeService employeeService = new EmployeeService();
    private final LeaveRequestService leaveRequestService = new LeaveRequestService();

    @FXML
    private TableView<EmployeeLeaveData> tableView;

    @FXML
    private TableColumn<EmployeeLeaveData, Integer> colRanking;

    @FXML
    private TableColumn<EmployeeLeaveData, Integer> colEmployeeId;

    @FXML
    private TableColumn<EmployeeLeaveData, Integer> colJobId;

    @FXML
    private TableColumn<EmployeeLeaveData, Integer> colConfirmedLeave;

    @FXML
    private TableColumn<EmployeeLeaveData, Integer> colNotConfirmedLeave;

    @FXML
    private TableColumn<EmployeeLeaveData, String> colAchievement;

    // Podium elements
    @FXML
    private Label firstPlaceInitial;

    @FXML
    private Label firstPlaceId;

    @FXML
    private Label firstPlaceAchievement;

    @FXML
    private Label firstPlaceLeaves;

    @FXML
    private Label secondPlaceInitial;

    @FXML
    private Label secondPlaceId;

    @FXML
    private Label secondPlaceAchievement;

    @FXML
    private Label secondPlaceLeaves;

    @FXML
    private Label thirdPlaceInitial;

    @FXML
    private Label thirdPlaceId;

    @FXML
    private Label thirdPlaceAchievement;

    @FXML
    private Label thirdPlaceLeaves;

    @FXML
    private Circle firstPlaceCircle;

    @FXML
    private Circle secondPlaceCircle;

    @FXML
    private Circle thirdPlaceCircle;

    @FXML
    public void initialize() {
        setupTable();
        loadEmployeeData();
    }

    private void setupTable() {
        // Setting up ranking column
        colRanking.setCellValueFactory(new PropertyValueFactory<>("ranking"));
        colRanking.setCellFactory(column -> new TableCell<EmployeeLeaveData, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setText(String.valueOf(item));
                    if (item <= 3) {
                        setFont(Font.font("System", FontWeight.BOLD, 14));
                        switch (item) {
                            case 1:
                                setTextFill(Color.web("#FFD700"));
                                setStyle("-fx-background-color: rgba(255, 215, 0, 0.2); -fx-background-radius: 3;");
                                break;
                            case 2:
                                setTextFill(Color.web("#C0C0C0"));
                                setStyle("-fx-background-color: rgba(192, 192, 192, 0.2); -fx-background-radius: 3;");
                                break;
                            case 3:
                                setTextFill(Color.web("#CD7F32"));
                                setStyle("-fx-background-color: rgba(205, 127, 50, 0.2); -fx-background-radius: 3;");
                                break;
                        }
                    } else {
                        setTextFill(Color.web("#2c3e50"));
                        setFont(Font.font("System", FontWeight.NORMAL, 13));
                        setStyle("");
                    }
                }
            }
        });

        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colJobId.setCellValueFactory(new PropertyValueFactory<>("jobId"));
        colConfirmedLeave.setCellValueFactory(new PropertyValueFactory<>("confirmedLeaveRequests"));
        colNotConfirmedLeave.setCellValueFactory(new PropertyValueFactory<>("notConfirmedLeaveRequests"));

        // Setting up achievement column
        colAchievement.setCellValueFactory(new PropertyValueFactory<>("achievement"));
        colAchievement.setCellFactory(column -> new TableCell<EmployeeLeaveData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-padding: 4 8; -fx-background-radius: 10;");
                    if (item.equals("Perfect Attendance")) {
                        setTextFill(Color.WHITE);
                        setStyle("-fx-background-color: rgba(46, 125, 50, 0.7); -fx-padding: 4 8; -fx-background-radius: 10;");
                        setFont(Font.font("System", FontWeight.BOLD, 12));
                    } else if (item.equals("Good Attendance")) {
                        setTextFill(Color.WHITE);
                        setStyle("-fx-background-color: rgba(25, 118, 210, 0.7); -fx-padding: 4 8; -fx-background-radius: 10;");
                        setFont(Font.font("System", FontWeight.NORMAL, 12));
                    } else if (item.equals("Regular Attendance")) {
                        setTextFill(Color.WHITE);
                        setStyle("-fx-background-color: rgba(251, 192, 45, 0.7); -fx-padding: 4 8; -fx-background-radius: 10;");
                        setFont(Font.font("System", FontWeight.NORMAL, 12));
                    } else {
                        setTextFill(Color.WHITE);
                        setStyle("-fx-background-color: rgba(211, 47, 47, 0.7); -fx-padding: 4 8; -fx-background-radius: 10;");
                        setFont(Font.font("System", FontWeight.NORMAL, 12));
                    }
                }
            }
        });

        // Apply modern styling to the table rows
        tableView.setRowFactory(tv -> {
            javafx.scene.control.TableRow<EmployeeLeaveData> row = new javafx.scene.control.TableRow<>();
            row.setStyle("-fx-background-color: transparent;");
            row.setOnMouseEntered(event -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: rgba(52, 152, 219, 0.1);");
                }
            });
            row.setOnMouseExited(event -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: transparent;");
                }
            });
            return row;
        });
    }

    @FXML
    public void refreshLeaderboard() {
        loadEmployeeData();
    }

    private void loadEmployeeData() {
        int companyId = AuthCompanyController.getLoggedInCompanyId();
        if (companyId == -1) {
            System.out.println("No company is currently logged in.");
            return;
        }

        List<Employee> employees = employeeService.getEmployeesByCompanyId(companyId);
        ObservableList<EmployeeLeaveData> employeeLeaveDataList = FXCollections.observableArrayList();

        for (Employee employee : employees) {
            List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestsByEmployeeId(employee.getId());
            int confirmedRequests = 0;
            int notConfirmedRequests = 0;

            for (LeaveRequest request : leaveRequests) {
                if (request.isConfirmed()) {
                    confirmedRequests++;
                } else {
                    notConfirmedRequests++;
                }
            }

            String achievement = determineAchievement(confirmedRequests);

            employeeLeaveDataList.add(new EmployeeLeaveData(
                    0, // Ranking will be set after sorting
                    employee.getId(),
                    employee.getUserId(),
                    employee.getJobId(),
                    confirmedRequests,
                    notConfirmedRequests,
                    achievement
            ));
        }

        // Sort the list by confirmed leave requests (ascending)
        employeeLeaveDataList.sort(Comparator.comparingInt(EmployeeLeaveData::getConfirmedLeaveRequests));

        // Set ranking after sorting
        for (int i = 0; i < employeeLeaveDataList.size(); i++) {
            employeeLeaveDataList.get(i).setRanking(i + 1);
        }

        tableView.setItems(employeeLeaveDataList);

        // Update podium with top 3 employees
        updatePodium(employeeLeaveDataList);
    }

    /**
     * Updates the podium visualization with the top 3 employees
     */
    private void updatePodium(ObservableList<EmployeeLeaveData> data) {
        // Clear podium if no data
        if (data == null || data.isEmpty()) {
            clearPodium();
            return;
        }

        // First place (gold)
        if (data.size() >= 1) {
            EmployeeLeaveData first = data.get(0);
            firstPlaceInitial.setText("E" + first.getEmployeeId());
            firstPlaceId.setText("ID: " + first.getEmployeeId());
            firstPlaceAchievement.setText(first.getAchievement());
            firstPlaceLeaves.setText("Leaves: " + first.getConfirmedLeaveRequests());

            // Set achievement background color
            setAchievementStyle(firstPlaceAchievement, first.getAchievement());
        } else {
            clearFirstPlace();
        }

        // Second place (silver)
        if (data.size() >= 2) {
            EmployeeLeaveData second = data.get(1);
            secondPlaceInitial.setText("E" + second.getEmployeeId());
            secondPlaceId.setText("ID: " + second.getEmployeeId());
            secondPlaceAchievement.setText(second.getAchievement());
            secondPlaceLeaves.setText("Leaves: " + second.getConfirmedLeaveRequests());

            // Set achievement background color
            setAchievementStyle(secondPlaceAchievement, second.getAchievement());
        } else {
            clearSecondPlace();
        }

        // Third place (bronze)
        if (data.size() >= 3) {
            EmployeeLeaveData third = data.get(2);
            thirdPlaceInitial.setText("E" + third.getEmployeeId());
            thirdPlaceId.setText("ID: " + third.getEmployeeId());
            thirdPlaceAchievement.setText(third.getAchievement());
            thirdPlaceLeaves.setText("Leaves: " + third.getConfirmedLeaveRequests());

            // Set achievement background color
            setAchievementStyle(thirdPlaceAchievement, third.getAchievement());
        } else {
            clearThirdPlace();
        }
    }

    /**
     * Sets the appropriate background style for achievement labels
     */
    private void setAchievementStyle(Label label, String achievement) {
        if (achievement.equals("Perfect Attendance")) {
            label.setStyle("-fx-background-color: rgba(46, 125, 50, 0.7); -fx-text-fill: white; -fx-padding: 2 8; -fx-background-radius: 10;");
        } else if (achievement.equals("Good Attendance")) {
            label.setStyle("-fx-background-color: rgba(25, 118, 210, 0.7); -fx-text-fill: white; -fx-padding: 2 8; -fx-background-radius: 10;");
        } else if (achievement.equals("Regular Attendance")) {
            label.setStyle("-fx-background-color: rgba(251, 192, 45, 0.7); -fx-text-fill: white; -fx-padding: 2 8; -fx-background-radius: 10;");
        } else {
            label.setStyle("-fx-background-color: rgba(211, 47, 47, 0.7); -fx-text-fill: white; -fx-padding: 2 8; -fx-background-radius: 10;");
        }
    }

    private void clearFirstPlace() {
        firstPlaceInitial.setText("--");
        firstPlaceId.setText("ID: --");
        firstPlaceAchievement.setText("--");
        firstPlaceLeaves.setText("Leaves: 0");
    }

    private void clearSecondPlace() {
        secondPlaceInitial.setText("--");
        secondPlaceId.setText("ID: --");
        secondPlaceAchievement.setText("--");
        secondPlaceLeaves.setText("Leaves: 0");
    }

    private void clearThirdPlace() {
        thirdPlaceInitial.setText("--");
        thirdPlaceId.setText("ID: --");
        thirdPlaceAchievement.setText("--");
        thirdPlaceLeaves.setText("Leaves: 0");
    }

    private void clearPodium() {
        clearFirstPlace();
        clearSecondPlace();
        clearThirdPlace();
    }

    private String determineAchievement(int confirmedLeaves) {
        if (confirmedLeaves == 0) {
            return "Perfect Attendance";
        } else if (confirmedLeaves <= 2) {
            return "Good Attendance";
        } else if (confirmedLeaves <= 5) {
            return "Regular Attendance";
        } else {
            return "Frequent Leaves";
        }
    }

    public static class EmployeeLeaveData {
        private int ranking;
        private final int employeeId;
        private final int userId;
        private final int jobId;
        private final int confirmedLeaveRequests;
        private final int notConfirmedLeaveRequests;
        private final String achievement;

        public EmployeeLeaveData(int ranking, int employeeId, int userId, int jobId,
                                 int confirmedLeaveRequests, int notConfirmedLeaveRequests,
                                 String achievement) {
            this.ranking = ranking;
            this.employeeId = employeeId;
            this.userId = userId;
            this.jobId = jobId;
            this.confirmedLeaveRequests = confirmedLeaveRequests;
            this.notConfirmedLeaveRequests = notConfirmedLeaveRequests;
            this.achievement = achievement;
        }

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }

        public int getEmployeeId() {
            return employeeId;
        }

        public int getUserId() {
            return userId;
        }

        public int getJobId() {
            return jobId;
        }

        public int getConfirmedLeaveRequests() {
            return confirmedLeaveRequests;
        }

        public int getNotConfirmedLeaveRequests() {
            return notConfirmedLeaveRequests;
        }

        public String getAchievement() {
            return achievement;
        }
    }
}