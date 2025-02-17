package org.example.pi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.example.pi.services.ReclamationService;
import org.example.pi.models.Reclamation;
import java.io.File;
import java.time.LocalDateTime;
import java.sql.SQLException;


public class ReclamationController {
    @FXML
    private TextField userIdField;
    @FXML
    private ComboBox<String> receiverComboBox;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField imagePathField;
    @FXML
    private TextField pdfPathField;

    private ReclamationService reclamationService = new ReclamationService();

    @FXML
    public void initialize() {
        // Initialize receiver options
        receiverComboBox.getItems().addAll("RH", "Admin");
    }

    @FXML
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePathField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleChoosePdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            pdfPathField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleSubmitReclamation() {
        try {
            Reclamation reclamation = new Reclamation();
            reclamation.setUserId(Integer.parseInt(userIdField.getText()));
            reclamation.setReceiver(receiverComboBox.getValue());
            reclamation.setTitle(titleField.getText());
            reclamation.setDescription(descriptionField.getText());
            reclamation.setImagePath(imagePathField.getText());
            reclamation.setPdfPath(pdfPathField.getText());
            reclamation.setDate(LocalDateTime.now());

            reclamationService.addReclamation(reclamation);
            System.out.println("Reclamation submitted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error submitting reclamation.");
        }
    }
}