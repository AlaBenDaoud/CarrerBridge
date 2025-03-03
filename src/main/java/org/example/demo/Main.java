package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.demo.service.DatabaseService;
import org.example.demo.service.LeaveRequestService;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the database service :
      DatabaseService databaseService = new DatabaseService();

        // Initialize the services : crée une instance de la classe LeaveRequestService
        LeaveRequestService leaveRequestService = new LeaveRequestService();

        // Load the FXML file for the leave request interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/views/leave_request.fxml"));
        Parent root = loader.load();

        // Set up the primary stage (main window)
        primaryStage.setTitle("Gestion des Congés et Absences");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application /qui initialise et démarre l'application JavaFX
        launch(args);
    }
}