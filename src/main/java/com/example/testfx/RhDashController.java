package com.example.testfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent; // Correct import for JavaFX ActionEvent

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

public class RhDashController {
    @FXML
    private Button btnSortie; // Correspond au bouton "sortie" du FXML

    @FXML
    private Button btnMoins; // Correspond au bouton "moins" du FXML

    @FXML
    private Button btnOffres;

    @FXML private TextField titreField;
    @FXML private TextField skillsField;
    @FXML private TextField experienceField;
    @FXML private TextField locationField;

    @FXML
    private TableView<Offre> offresTable;

    @FXML
    private TableColumn<Offre, String> titreColumn;

    @FXML
    private TableColumn<Offre, String> competencesColumn;

    @FXML
    private TableColumn<Offre, Integer> experienceColumn;

    @FXML
    private TableColumn<Offre, String> localisationColumn;


    @FXML
    private TableColumn<Offre, Void> actionsColumn;

    @FXML
    private Button btnCandidats;

    @FXML
    public void initialize() {


        // Associer des événements aux boutons
        btnSortie.setOnAction(event -> retourPageConnexion());
        btnMoins.setOnAction(event -> reduireFenetre());
        btnOffres.setOnAction(event -> allerPageOffres());
        btnCandidats.setOnAction(event -> handleCandidatsButtonClick(event));


        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        competencesColumn.setCellValueFactory(new PropertyValueFactory<>("competences"));
        experienceColumn.setCellValueFactory(new PropertyValueFactory<>("experience"));
        localisationColumn.setCellValueFactory(new PropertyValueFactory<>("localisation"));

        // Configurer la colonne "Actions"
        actionsColumn.setCellFactory(column -> new TableCell<Offre, Void>() {
            private final Button modifyButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                // Action pour le bouton "Modifier"
                modifyButton.setOnAction(event -> {
                    Offre offre = getTableView().getItems().get(getIndex());
                    modifierOffre(offre);
                });

                // Action pour le bouton "Supprimer"
                deleteButton.setOnAction(event -> {
                    Offre offre = getTableView().getItems().get(getIndex());
                    supprimerOffre(offre);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, modifyButton, deleteButton));
                }
            }
        });


        List<Offre> offres = getOffresFromDatabase();
        ObservableList<Offre> observableOffres = FXCollections.observableArrayList(offres);
        offresTable.setItems(observableOffres);






    }

    private void modifierOffre(Offre offre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifierOffre.fxml"));
            Parent root = loader.load();

            // Passer l'offre sélectionnée au contrôleur de modification
            ModifierOffreController controller = loader.getController();
            controller.setOffre(offre);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'offre");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer une offre
    private void supprimerOffre(Offre offre) {
        String query = "DELETE FROM offres WHERE id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/careerbridge", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, offre.getId());
            stmt.executeUpdate();

            // Actualiser la TableView
            offresTable.getItems().remove(offre);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private void allerPageOffres() {
        try {
            // Charger le fichier FXML de la page des offres
            FXMLLoader loader = new FXMLLoader(getClass().getResource("offre.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle
            Stage stage = (Stage) btnOffres.getScene().getWindow();

            // Changer la scène pour afficher offre.fxml
            stage.setScene(new Scene(root));
            stage.setTitle("Offres");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Afficher l'erreur si le fichier FXML n'est pas trouvé
        }
    }


    private List<Offre> getOffresFromDatabase() {
        List<Offre> offres = new ArrayList<>();
        String query = "SELECT * FROM offres";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/careerbridge", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String competences = rs.getString("competences");
                int experience = rs.getInt("experience");
                String localisation = rs.getString("localisation");

                Offre offre = new Offre(id, titre, competences, experience, localisation);
                offres.add(offre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offres;
    }
    @FXML
    private void handleCandidatsButtonClick(ActionEvent event) {
        try {
            // Charger la page candidats.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("candidats.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle
            Stage stage = (Stage) btnCandidats.getScene().getWindow();

            // Changer la scène pour afficher candidats.fxml
            stage.setScene(new Scene(root));
            stage.setTitle("Candidats");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
