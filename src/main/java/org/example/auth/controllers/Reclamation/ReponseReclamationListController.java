package org.example.auth.controllers.Reclamation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.auth.models.ReponseReclamation;
import org.example.auth.services.ReponseReclamationService;

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
    private TableColumn<ReponseReclamation, Void> colActions; // For action buttons

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

        // Adding action buttons in the table
        colActions.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    ReponseReclamation response = getTableRow().getItem();

                    // Styling the buttons
                    editButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-padding: 5; -fx-border-radius: 5;");
                    editButton.setOnAction(event -> modifierReponse(response));

                    deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-padding: 5; -fx-border-radius: 5; -fx-margin-left: 5;"); // Add margin to the delete button
                    deleteButton.setOnAction(event -> confirmDeleteReponse(response));

                    // Add spacing between buttons
                    HBox buttonContainer = new HBox(editButton, deleteButton);
                    buttonContainer.setSpacing(10); // Set spacing between buttons
                    setGraphic(buttonContainer);
                }
            }
        });

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
            showAlert("Error", "Unable to load responses", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void confirmDeleteReponse(ReponseReclamation response) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete this response?");

        confirmationAlert.showAndWait().ifPresent(responseType -> {
            if (responseType == ButtonType.OK) {
                supprimerReponse(response);
            }
        });
    }

    private void supprimerReponse(ReponseReclamation response) {
        if (response != null) {
            try {
                service.deleteReponseReclamation(response.getId());
                observableList.remove(response);
                showAlert("Success", "Response deleted successfully", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Error", "Failed to delete", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    private void modifierReponse(ReponseReclamation response) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pi/ModifierReponseReclamation.fxml"));
            VBox root = loader.load();

            ModifierReponseReclamationController controller = loader.getController();
            controller.setResponse(response);  // Set the response to be edited

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Response");
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Unable to load edit window", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}