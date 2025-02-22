package org.example.auth;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.auth.utils.DatabaseService;

import java.sql.Connection;
import java.sql.SQLException;

public class MainApp extends Application {

    private DatabaseService databaseService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize DatabaseService
        databaseService = new DatabaseService();

        // Try establishing the connection and display message in the terminal
        try (Connection connection = databaseService.getConnection()) {
            System.out.println("Connection Successful!");
        } catch (SQLException e) {
            System.out.println("Connection Failed: " + e.getMessage());
        }

        // Load the FXML UI for user registration
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/main.fxml"));
        VBox root = loader.load();

        // Set up the scene with a larger aspect ratio
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Business Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}