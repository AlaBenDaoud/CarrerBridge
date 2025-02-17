package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.demo.service.DatabaseService;
import org.example.demo.service.LeaveRequestService;
import org.example.demo.service.OnlineJobService;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the database service
        DatabaseService databaseService = new DatabaseService();

        // Initialize the services
        LeaveRequestService leaveRequestService = new LeaveRequestService();
        OnlineJobService onlineJobService = new OnlineJobService();
        // Load the FXML file for the leave request interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/views/leave_request.fxml"));
        Parent root = loader.load();

        // Set up the primary stage (main window)
        primaryStage.setTitle("Gestion des Congés et Absences");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}