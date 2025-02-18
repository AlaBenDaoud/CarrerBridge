package com.example.testfx;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class NotificationController {
    @FXML
    private ListView<String> notificationsList;

    @FXML
    private Button btnSchedule;

    @FXML
    public void initialize() {
        loadNotifications();
    }

    private void loadNotifications() {
        String query = "SELECT message, interview_date, interview_time FROM notifications WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/careerbridge", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, getCurrentUserId()); // Récupérer l'ID de l'utilisateur connecté
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String message = rs.getString("message");
                String interviewDate = rs.getString("interview_date");
                String interviewTime = rs.getString("interview_time");

                String notificationText = message;
                if (interviewDate != null && interviewTime != null) {
                    notificationText += " (Entretien prévu le " + interviewDate + " à " + interviewTime + ")";
                }

                notificationsList.getItems().add(notificationText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleScheduleButtonClick() {
        String selectedNotification = notificationsList.getSelectionModel().getSelectedItem();
        if (selectedNotification != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("scheduleInterview.fxml"));
                Parent root = loader.load();

                // Passer la notification sélectionnée au contrôleur de planification
                ScheduleInterviewController controller = loader.getController();
                controller.setSelectedNotification(selectedNotification);

                // Afficher la fenêtre de planification
                Stage stage = (Stage) btnSchedule.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Planifier un entretien");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getCurrentUserId() {
        return 1; // Exemple, remplacez par la logique réelle
    }
}
