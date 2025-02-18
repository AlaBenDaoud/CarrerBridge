package com.example.testfx;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.nio.file.Files;


public class UserDashController {
    @FXML
    private Button btnSortie; // Correspond au bouton "sortie" du FXML

    @FXML
    private Button btnMoins; // Correspond au bouton "moins" du FXML

    @FXML
    private Button btnCv;
    @FXML
    private Button btnChargerCV;  // Le bouton "Sélectionner un CV"

    @FXML
    private Label lblDropZone;  // Le label qui affichera le nom du fichier sélectionné


    @FXML
    private Button btnSchedule;

    @FXML
    private Button btnNotifications;


    @FXML
    private ListView<String> notificationsList;


    @FXML
    public void initialize() {
        // Associer des événements aux boutons
        btnSortie.setOnAction(event -> retourPageConnexion());
        btnMoins.setOnAction(event -> reduireFenetre());


    }

    // Méthode pour fermer complètement l'application
    private void retourPageConnexion() {
        try {
            // Charger le fichier FXML de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("selectlogin.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle
            Stage stage = (Stage) btnSortie.getScene().getWindow();

            // Changer la scène pour afficher loginAdmin.fxml
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion - Admin");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Afficher l'erreur si le fichier FXML n'est pas trouvé
        }
    }

    // Méthode pour réduire la fenêtre
    private void reduireFenetre() {
        Stage stage = (Stage) btnMoins.getScene().getWindow(); // Obtenir la fenêtre actuelle
        stage.setIconified(true); // Réduire la fenêtre
    }

    @FXML
    public void handleCvButtonClick(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page 'usercv.fxml' en utilisant BorderPane
            FXMLLoader loader = new FXMLLoader(getClass().getResource("usercv.fxml"));
            Parent userCvPage = loader.load();  // Charge la page usercv.fxml

            // Créer une nouvelle scène pour la page usercv.fxml
            Scene scene = new Scene(userCvPage);

            // Récupérer la fenêtre actuelle (Stage)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(scene);
            stage.setTitle("CV Page");  // Vous pouvez définir un titre pour la fenêtre ici
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

            // Afficher une alerte si le chargement échoue
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Page non trouvée");
            alert.setContentText("Impossible de charger la page 'usercv.fxml'. Vérifiez le chemin du fichier.");
            alert.showAndWait();
        }
    }



    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // Méthode pour obtenir le stage (fenêtre principale) de l'application
    private Stage getStage() {
        return (Stage) btnChargerCV.getScene().getWindow();
    }
    @FXML
    private void handleRetourButtonClick(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page 'dashUser.fxml'
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashUser.fxml"));
            Parent root = loader.load();  // Charge le fichier FXML (la vue)

            // Récupérer la fenêtre actuelle (Stage)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(new Scene(root));  // Crée une nouvelle scène à partir du fichier FXML
            stage.setTitle("Page d'Utilisateur"); // Optionnel : définir un titre pour la fenêtre
            stage.show();  // Affiche la nouvelle scène dans la fenêtre
        } catch (IOException e) {
            e.printStackTrace();  // Affiche l'erreur si le fichier FXML ne peut pas être chargé

            // Afficher une alerte si le chargement échoue
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Page non trouvée");
            alert.setContentText("Impossible de charger la page 'dashUser.fxml'. Vérifiez le chemin du fichier.");
            alert.showAndWait();  // Affiche l'alerte
        }
    }


    @FXML
    private void handleChargerCV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(getStage());

        if (selectedFile != null) {
            lblDropZone.setText("Vous avez sélectionné : " + selectedFile.getName());

            try {
                // Extraire le texte du CV
                String cvText = CVTextExtractor.extractTextFromFile(selectedFile);

                // Récupérer les offres depuis la base de données
                OffreDAO offreDAO = new OffreDAO();
                List<Offre> offres = offreDAO.getOffresFromDatabase();

                // Trouver les offres correspondantes
                CVMatcher cvMatcher = new CVMatcher();
                List<Offre> matchingOffres = cvMatcher.matchCVWithOffres(cvText, offres);

                // Afficher les offres correspondantes
                displayMatchingOffres(matchingOffres);
            } catch (IOException e) {
                showErrorAlert("Erreur", "Impossible de lire le fichier CV.");
            }
        } else {
            lblDropZone.setText("Aucun fichier sélectionné.");
        }
    }

    private void displayMatchingOffres(List<Offre> matchingOffres) {
        if (matchingOffres.isEmpty()) {
            // Afficher un message si aucune offre n'est trouvée
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aucune offre trouvée");
            alert.setHeaderText(null);
            alert.setContentText("Désolé, aucune offre ne correspond à votre CV pour le moment.");
            alert.showAndWait();
        } else {
            // Créer une ListView pour afficher les offres
            ListView<HBox> listView = new ListView<>();

            // Ajouter chaque offre avec un bouton "Détails"
            for (Offre offre : matchingOffres) {
                // Créer un conteneur horizontal pour l'offre et le bouton
                HBox hbox = new HBox(10); // Espacement de 10 pixels entre les éléments
                Label offreLabel = new Label(offre.getTitre()); // Afficher le titre de l'offre
                Button detailsButton = new Button("Détails"); // Bouton "Détails"

                // Définir l'action du bouton "Détails"
                detailsButton.setOnAction(e -> showOffreDetails(offre));

                // Ajouter le label et le bouton au conteneur horizontal
                hbox.getChildren().addAll(offreLabel, detailsButton);

                // Ajouter le conteneur à la ListView
                listView.getItems().add(hbox);
            }

            // Créer une boîte de dialogue pour afficher la ListView
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Offres correspondantes");
            alert.setHeaderText("Voici les offres correspondantes à votre CV :");
            alert.getDialogPane().setContent(new VBox(listView)); // Ajouter la ListView à la boîte de dialogue
            alert.getButtonTypes().clear(); // Supprimer les boutons par défaut
            alert.getButtonTypes().add(ButtonType.OK); // Ajouter un bouton "OK"
            alert.showAndWait();
        }
    }

    /**
     * Affiche les détails d'une offre dans une boîte de dialogue.
     *
     * @param offre L'offre à afficher.
     */

    private void showOffreDetails(Offre offre) {
        // Créer une boîte de dialogue
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de l'offre");
        alert.setHeaderText(offre.getTitre());

        // Contenu de la boîte de dialogue
        Label competencesLabel = new Label("Compétences requises : " + offre.getCompetences());
        Label experienceLabel = new Label("Expérience : " + offre.getExperience());
        Label localisationLabel = new Label("Localisation : " + offre.getLocalisation());

        // Bouton "Postuler"
        Button postulerButton = new Button("Postuler");
        postulerButton.setOnAction(e -> {
            // Enregistrer la candidature dans la base de données
            boolean success = enregistrerCandidature(offre.getId()); // Supposons que l'utilisateur est déjà connecté
            if (success) {
                showSuccessAlert("Succès", "Votre candidature a été enregistrée avec succès !");
            } else {
                showErrorAlert("Erreur", "Une erreur s'est produite lors de l'enregistrement de votre candidature.");
            }
        });

        // Ajouter les éléments à la boîte de dialogue
        VBox content = new VBox(10, competencesLabel, experienceLabel, localisationLabel, postulerButton);
        alert.getDialogPane().setContent(content);

        // Afficher la boîte de dialogue
        alert.showAndWait();
    }
    private boolean enregistrerCandidature(int offreId) {
        String query = "INSERT INTO candidatures (offre_id, user_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/careerbridge", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Supposons que l'ID de l'utilisateur est déjà connu (par exemple, stocké dans une variable de session)
            int userId = getCurrentUserId(); // Méthode à implémenter pour récupérer l'ID de l'utilisateur connecté

            // Définir les paramètres de la requête
            pstmt.setInt(1, offreId);
            pstmt.setInt(2, userId);

            // Exécuter la requête
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Retourne true si au moins une ligne a été insérée
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private int getCurrentUserId() {
        // Implémentez cette méthode pour récupérer l'ID de l'utilisateur connecté
        // Par exemple, vous pouvez stocker l'ID dans une variable de session après la connexion
        return 1; // Exemple : retourne 1 pour l'utilisateur connecté
    }
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    private void handleNotificationsButtonClick() {
        try {
            // Assurez-vous que le chemin est correct
            FXMLLoader loader = new FXMLLoader(getClass().getResource("notifications.fxml"));
            Parent root = loader.load();

            // Afficher la nouvelle scène
            Stage stage = (Stage) btnNotifications.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Notifications");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

            // Afficher une alerte en cas d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Fichier FXML non trouvé");
            alert.setContentText("Impossible de charger notification.fxml. Vérifiez le chemin du fichier.");
            alert.showAndWait();
        }
    }







}
