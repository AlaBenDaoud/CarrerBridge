package com.example.testfx;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class ScheduleInterviewController {
    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField timeField;

    private String selectedNotification;
    public void setSelectedNotification(String selectedNotification) {
        this.selectedNotification = selectedNotification;
    }

    @FXML
    private void handleConfirmButtonClick() {
        String date = datePicker.getValue().toString();
        String time = timeField.getText();

        String query = "UPDATE notifications SET interview_date = ?, interview_time = ?, is_read = true WHERE user_id = ? AND message = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/careerbridge", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, date);
            pstmt.setString(2, time);
            pstmt.setInt(3, getCurrentUserId());
            pstmt.setString(4, getSelectedNotificationMessage());

            pstmt.executeUpdate();
            System.out.println("Entretien planifié avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getCurrentUserId() {
        return 1; // Exemple, remplacez par la logique réelle
    }

    private String getSelectedNotificationMessage() {
        return "Vous avez été sélectionné pour un entretien"; // Exemple, remplacez par la logique réelle
    }
}
