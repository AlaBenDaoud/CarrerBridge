package org.example.pi.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.pi.models.ReponseReclamation;
import org.example.pi.services.ReponseReclamationService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ReponseReclamationListController {

    @FXML
    private TableView<ReponseReclamation> tableViewReponses;
    @FXML
    private TableColumn<ReponseReclamation, Integer> colId;
    @FXML
    private TableColumn<ReponseReclamation, Integer> colIdRec;
    @FXML
    private TableColumn<ReponseReclamation, Integer> colIdUser;
    @FXML
    private TableColumn<ReponseReclamation, String> colReponse;
    @FXML
    private TableColumn<ReponseReclamation, String> colPdfPath;
    @FXML
    private TableColumn<ReponseReclamation, String> colStatut;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnRefresh;

    private final ReponseReclamationService service = new ReponseReclamationService();
    private ObservableList<ReponseReclamation> observableList;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colIdRec.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdRec()).asObject());
        colIdUser.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdUser()).asObject());
        colReponse.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getReponse()));
        colPdfPath.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPdfPath()));
        colStatut.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatueOfReponseReclamation()));

        VBox.setVgrow(tableViewReponses, Priority.ALWAYS);
        loadReponses();
    }

    @FXML
    private void handleRefresh() {
        loadReponses();
    }

    private void loadReponses() {
        try {
            List<ReponseReclamation> reponses = service.getAllReponseReclamations();
            observableList = FXCollections.observableArrayList(reponses);
            tableViewReponses.setItems(observableList);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les réponses", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimerReponse() {
        ReponseReclamation selected = tableViewReponses.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                service.deleteReponseReclamation(selected.getId());
                observableList.remove(selected);
                showAlert("Succès", "Réponse supprimée avec succès", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur", "Échec de la suppression", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        } else {
            showAlert("Aucune sélection", "Veuillez sélectionner une réponse à supprimer", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void modifierReponse() {
        // Implementation of modifierReponse logic
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}