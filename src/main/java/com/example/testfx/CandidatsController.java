package com.example.testfx;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class CandidatsController {
    @FXML
    private TableView<Candidature> candidaturesTable;

    @FXML
    private TableColumn<Candidature, String> candidatColumn;

    @FXML
    private TableColumn<Candidature, String> offreColumn;

    @FXML
    private TableColumn<Candidature, String> datePostulationColumn;

    @FXML
    private TableColumn<Candidature, Void> actionsColumn;

    @FXML
    private Button btnOffres;

    @FXML
    public void initialize() {
        // Configurer les colonnes de la TableView
        candidatColumn.setCellValueFactory(new PropertyValueFactory<>("candidat"));
        offreColumn.setCellValueFactory(new PropertyValueFactory<>("offre"));
        datePostulationColumn.setCellValueFactory(new PropertyValueFactory<>("datePostulation"));

        // Charger les candidatures depuis la base de données
        List<Candidature> candidatures = getCandidaturesFromDatabase();
        candidaturesTable.getItems().addAll(candidatures);

        // Ajouter un bouton "Action" dans la colonne Actions
        addActionButtons();

        // Gérer le clic sur le bouton "Retour"
        btnOffres.setOnAction(event -> handleRetourButtonClick());
    }

    /**
     * Récupère la liste des candidatures depuis la base de données.
     *
     * @return Une liste de candidatures.
     */
    private List<Candidature> getCandidaturesFromDatabase() {
        List<Candidature> candidatures = new ArrayList<>();
        String query = "SELECT c.id, u.nom AS candidat, o.titre AS offre, c.date_postulation " +
                "FROM candidatures c " +
                "JOIN utilisateur u ON c.user_id = u.id " +
                "JOIN offres o ON c.offre_id = o.id";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/careerbridge", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String candidat = rs.getString("candidat");
                String offre = rs.getString("offre");
                String datePostulation = rs.getString("date_postulation");

                Candidature candidature = new Candidature(candidat, offre, datePostulation);
                candidatures.add(candidature);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return candidatures;
    }

    /**
     * Ajoute un bouton "Action" dans la colonne Actions.
     */
    private void addActionButtons() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button actionButton = new Button("Ajouter");

            {
                actionButton.setOnAction(event -> {
                    Candidature candidature = getTableView().getItems().get(getIndex());
                    handleActionButtonClick(candidature);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButton);
                }
            }
        });
    }

    /**
     * Gère le clic sur le bouton "Action".
     *
     * @param candidature La candidature sélectionnée.
     */
    private void handleActionButtonClick(Candidature candidature) {
        System.out.println("Action pour la candidature : " + candidature.getCandidat());

        String query = "INSERT INTO notifications (user_id, message, is_read) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/careerbridge", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            int userId = candidature.getUserId(); // Supposons que vous avez un getter pour user_id
            String message = "Vous avez été sélectionné pour un entretien pour l'offre : " + candidature.getOffre();

            pstmt.setInt(1, userId);
            pstmt.setString(2, message);
            pstmt.setBoolean(3, false); // La notification n'est pas encore lue

            pstmt.executeUpdate();
            System.out.println("Notification envoyée à l'utilisateur : " + userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gère le clic sur le bouton "Retour".
     */
    private void handleRetourButtonClick() {
        try {
            // Charger la page précédente (par exemple, la page principale)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashRh.fxml")); // Remplacez par le bon fichier FXML
            Parent root = loader.load();

            // Obtenir la scène actuelle
            Stage stage = (Stage) btnOffres.getScene().getWindow();

            // Changer la scène pour afficher la page précédente
            stage.setScene(new Scene(root));
            stage.setTitle("Page Principale"); // Remplacez par le bon titre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
