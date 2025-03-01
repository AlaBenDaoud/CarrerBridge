package org.example.auth.controllers.Reclamation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.example.auth.models.Reclamation;
import org.example.auth.services.ReclamationService;
import org.example.auth.controllers.connexion.AuthCompanyController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ViewAllReclamationsController {

    @FXML
    private TableView<Reclamation> reclamationTable;
    @FXML
    private TableColumn<Reclamation, Integer> idColumn;
    @FXML
    private TableColumn<Reclamation, Integer> userIdColumn;
    @FXML
    private TableColumn<Reclamation, Integer> companyIdColumn;
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
    private TableColumn<Reclamation, String> statusColumn;
    @FXML
    private TableColumn<Reclamation, Void> actionsColumn;

    private final ReclamationService reclamationService = new ReclamationService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        companyIdColumn.setCellValueFactory(new PropertyValueFactory<>("companyId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        imagePathColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        pdfPathColumn.setCellValueFactory(new PropertyValueFactory<>("pdfPath"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statueOfReclamation"));

        addActionButtonsToTable();
        refreshTable();
    }

    @FXML
    private void handleRefresh() {
        refreshTable();
    }

    private void addActionButtonsToTable() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button answerButton = new Button("Answer");
            private final Button changeStatusButton = new Button("Change Status");
            private final HBox buttonBox = new HBox(10, changeStatusButton, answerButton); // Only include Answer and Change Status buttons

            {
                answerButton.setOnAction(event -> handleAnswer(getTableView().getItems().get(getIndex())));
                changeStatusButton.setOnAction(event -> openEditStatusDialog(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonBox);
            }
        });
    }

    private void handleAnswer(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pi/reponse_reclamation_form.fxml"));
            DialogPane dialogPane = loader.load();
            ReponseReclamationController controller = loader.getController();
            controller.setReclamation(reclamation);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Answer to Reclamation");

            if (dialog.showAndWait().filter(response -> response == ButtonType.OK).isPresent()) {
                refreshTable();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the response form.");
        }
    }

    private void refreshTable() {
        try {
            // Get the logged-in company's ID
            int loggedInCompanyId = AuthCompanyController.getLoggedInCompanyId();

            // Fetch all reclamations
            List<Reclamation> allReclamations = reclamationService.getAllReclamations();

            // Filter reclamations to only include those that belong to the logged-in company
            List<Reclamation> filteredReclamations = allReclamations.stream()
                    .filter(reclamation -> reclamation.getCompanyId() == loggedInCompanyId)
                    .collect(Collectors.toList());

            // Set the filtered reclamations to the table
            reclamationTable.getItems().setAll(filteredReclamations);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load reclamations.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openEditStatusDialog(Reclamation reclamation) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Change Reclamation Status");
        dialog.setHeaderText("Change the status of the reclamation");

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Not Treated", "In Progress", "Resolved");
        statusComboBox.setValue(reclamation.getStatueOfReclamation());
        dialog.getDialogPane().setContent(statusComboBox);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> dialogButton == okButtonType ? statusComboBox.getValue() : null);

        dialog.showAndWait().ifPresent(newStatus -> {
            try {
                reclamationService.updateReclamationStatus(reclamation.getId(), newStatus);
                refreshTable();
                showAlert("Success", "Reclamation status updated successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to update the reclamation status.");
            }
        });
    }
}