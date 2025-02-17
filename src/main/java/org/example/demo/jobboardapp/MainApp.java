package org.example.demo.jobboardapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.demo.jobboardapp.Services.JobCreation;
import org.example.demo.jobboardapp.Services.ApplicantCreation;


public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the database (create the jobs table if it doesn't exist)
        JobCreation.initializeTables();
        ApplicantCreation.initializeApplicantsTable();

        // Use classpath-relative loading for FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/jobboardapp/views/main.fxml"));
        Parent root = loader.load();

        // Set up the stage
        primaryStage.setTitle("Job Board Application");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}