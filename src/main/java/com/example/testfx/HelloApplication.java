package com.example.testfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;



import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    private Stage primaryStage; // Stage principal pour la navigation

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Application de gestion des employés");

        // Charger la scène principale
        loadScene("selectrole.fxml");
    }

    /**
     * Charge une nouvelle scène en fonction du fichier FXML donné.
     * @param fxmlFile Nom du fichier FXML à charger.
     */
    public void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de : " + fxmlFile);
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Le fichier FXML est introuvable : " + fxmlFile);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}