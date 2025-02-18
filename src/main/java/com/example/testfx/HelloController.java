package com.example.testfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloController {
    private static final Logger logger = Logger.getLogger(HelloController.class.getName());
    private String selectedRole;

    @FXML
    private Button inscriptionButton;
    @FXML
    private Button connexionButton;
    @FXML
    private Button retourButton;
    public void setSelectedRole(String role) {
        this.selectedRole = role;
        System.out.println("Rôle défini dans HelloController : " + role);
    }


    @FXML
    public void initialize() {
        if (inscriptionButton != null) {
            // Appeler handleInscription lors du clic sur le bouton "S'inscrire"
            inscriptionButton.setOnAction(event -> handleInscription(event));
        } else {
            logger.warning("Le bouton inscriptionButton n'a pas été initialisé.");
        }

        if (connexionButton != null) {
            connexionButton.setOnAction(event -> navigateToPage("newlogin.fxml", event));
        } else {
            logger.warning("Le bouton connexionButton n'a pas été initialisé.");
        }

        if (retourButton != null) {
            retourButton.setOnAction(event -> goBack(event));
        } else {
            logger.warning("Le bouton retourButton n'a pas été initialisé.");
        }
    }

    private void handleInscription(ActionEvent event) {
        System.out.println("Rôle actuel lors de l'inscription : " + selectedRole);
        if ("RH".equals(selectedRole)) {
            System.out.println("Redirection vers registreRh.fxml");
            navigateToPage("registreRh.fxml", event);
        } else {
            System.out.println("Redirection vers newregistre.fxml");
            navigateToPage("newregistre.fxml", event);
        }
    }




    private void goBack(ActionEvent event) {
        navigateToPage("selectrole.fxml", event);
    }


    private void navigateToPage(String fxmlFile, ActionEvent event) {
        try {
            // Charge le fichier FXML correctement
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Récupère la scène à partir du bouton
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            if (stage == null) {
                logger.warning("La scène n'a pas été récupérée correctement.");
            } else {
                stage.setScene(new Scene(root));
                stage.show();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erreur lors du chargement du fichier FXML: " + fxmlFile, e);
        } catch (NullPointerException e) {
            logger.log(Level.SEVERE, "Erreur lors de la navigation vers " + fxmlFile, e);
        }
    }

}