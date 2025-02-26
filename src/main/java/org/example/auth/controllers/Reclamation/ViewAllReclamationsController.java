package org.example.pi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.pi.models.Reclamation;
import org.example.pi.models.ReponseReclamation;
import org.example.pi.services.ReclamationService;

import org.example.pi.services.ReponseReclamationService;
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
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete this reclamation?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                reclamationService.deleteReclamation(reclamation.getId());
                showAlert("Success", "Reclamation deleted successfully.");
                refreshTable();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete the reclamation.");
            }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pi/reponse_reclamation_form.fxml"));
            DialogPane dialogPane = loader.load();

            ReponseReclamationController controller = loader.getController();
            controller.setReclamation(reclamation); // Pass the reclamation to the controller

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Answer to Reclamation");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                refreshTable(); // Refresh the table after submitting the response
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the response form.");
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
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Change Reclamation Status");
        dialog.setHeaderText("Change the status of the reclamation");

        // Create a ComboBox for the status selection
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Not Treated", "In Progress", "Resolved");
        statusComboBox.setValue(reclamation.getStatueOfReclamation()); // Set current status as default

        // Add the ComboBox to the dialog
        dialog.getDialogPane().setContent(statusComboBox);

        // Add OK and Cancel buttons
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Handle the result of the dialog
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return statusComboBox.getValue();
            }
            return null;
        });

        // Show the dialog and handle the new status
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newStatus -> {
            try {
                reclamation.setStatueOfReclamation(newStatus); // Update the status in the object
                reclamationService.updateReclamationStatus(reclamation); // Save the new status in the database
                refreshTable(); // Refresh the table to reflect the changes
                showAlert("Success", "Reclamation status updated successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to update the reclamation status.");
            }
        });
    }
}