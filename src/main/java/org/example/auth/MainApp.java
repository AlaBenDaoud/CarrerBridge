package org.example.auth;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox; // Use VBox because the FXML root is a VBox
import javafx.stage.Stage;
import org.example.auth.utils.DatabaseService;

import java.sql.Connection;
import java.sql.SQLException;

public class MainApp extends Application {

    // Hold the DatabaseService instance
    private DatabaseService databaseService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize DatabaseService
        databaseService = new DatabaseService();

        // Try establishing the connection and display message in the terminal
        try (Connection connection = databaseService.getConnection()) {
            // Show message in the terminal if connection is successful
            System.out.println("Connection Successful!");
        } catch (SQLException e) {
            // Print failure message in the terminal if the connection fails
            System.out.println("Connection Failed: " + e.getMessage());
        }

        // Load the FXML UI for user registration
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/main.fxml"));
        VBox root = loader.load(); // Correctly cast to VBox
        root.setPadding(new javafx.geometry.Insets(20, 20, 20, 20)); // Optional: Add padding programmatically

        // Set up the scene
        Scene scene = new Scene(root, 300, 300);
        primaryStage.setTitle("User Registration");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}