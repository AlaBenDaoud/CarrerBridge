package org.example.pi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.pi.models.Reclamation;
import org.example.pi.services.ReclamationService;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ViewAllReclamationsController {

    @FXML
    private TableView<Reclamation> reclamationTable;
    @FXML
    private TableColumn<Reclamation, Integer> idColumn;
    @FXML
    private TableColumn<Reclamation, Integer> userIdColumn;
    @FXML
    private TableColumn<Reclamation, String> receiverColumn;
    @FXML
    private TableColumn<Reclamation, String> titleColumn;
    @FXML
    private TableColumn<Reclamation, String> descriptionColumn;
    @FXML
    private TableColumn<Reclamation, String> imagePathColumn;
    @FXML
    private TableColumn<Reclamation, String> pdfPathColumn;
    @FXML
    private TableColumn<Reclamation, String> dateColumn;
    @FXML
    private TableColumn<Reclamation, String> statueColumn;
    @FXML
    private TableColumn<Reclamation, Void> actionsColumn;

    private final ReclamationService reclamationService = new ReclamationService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        receiverColumn.setCellValueFactory(new PropertyValueFactory<>("receiver"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        imagePathColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        pdfPathColumn.setCellValueFactory(new PropertyValueFactory<>("pdfPath"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statueColumn.setCellValueFactory(new PropertyValueFactory<>("statueOfReclamation"));

        addActionButtonsToTable();
        refreshTable();
    }

    @FXML
    private void handleRefresh() {
        refreshTable();
    }

    private void addActionButtonsToTable() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button modifyButton = new Button("Modify");
            private final Button answerButton = new Button("Answer");
            private final Button changeStatusButton = new Button("Change Status");
            private final HBox buttonBox = new HBox(10, changeStatusButton, deleteButton, modifyButton, answerButton);

            {
                deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-padding: 5; -fx-background-radius: 5;");
                modifyButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-padding: 5; -fx-background-radius: 5;");
                answerButton.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-padding: 5; -fx-background-radius: 5;");
                changeStatusButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black; -fx-padding: 5; -fx-background-radius: 5;");

                deleteButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    if (reclamation != null) {
                        handleDelete(reclamation);
                    }
                });

                modifyButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    if (reclamation != null) {
                        handleModify(reclamation);
                    }
                });

                answerButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    if (reclamation != null) {
                        handleAnswer(reclamation);
                    }
                });

                changeStatusButton.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    if (reclamation != null) {
                        openEditStatusDialog(reclamation);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        });
    }

    private void handleDelete(Reclamation reclamation) {
        try {
            reclamationService.deleteReclamation(reclamation.getId());
            refreshTable();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete the reclamation.");
        }
    }

    private void handleModify(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pi/EditReclamationDialog.fxml"));
            DialogPane dialogPane = loader.load();

            EditReclamationDialogController controller = loader.getController();
            controller.setReclamation(reclamation);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Reclamation");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Reclamation updatedReclamation = controller.getUpdatedReclamation();
                reclamationService.updateReclamation(updatedReclamation);
                refreshTable();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleAnswer(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pi/AnswerDialog.fxml"));
            DialogPane dialogPane = loader.load();

            AnswerDialogController controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Answer to Reclamation");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String title = controller.getTitle();
                String message = controller.getMessage();
                // Here you can handle the title and message, e.g., send them to a service
                System.out.println("Title: " + title);
                System.out.println("Message: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        try {
            reclamationTable.getItems().clear();
            reclamationTable.getItems().addAll(reclamationService.getAllReclamations());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openEditStatusDialog(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pi/reponse_reclamation_form.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Edit Reclamation Status");

            EditReclamationStateDialogController controller = loader.getController();
            controller.setReclamation(reclamation);

            stage.showAndWait();
            refreshTable(); // Refresh the table after editing
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}