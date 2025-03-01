package org.example.auth.controllers.Reclamation;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.example.auth.models.Reclamation;
import org.example.auth.services.ReclamationService;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReclamationController {
    @FXML
    private TextField userIdField;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField imagePathField;
    @FXML
    private TextField pdfPathField;

    private ReclamationService reclamationService = new ReclamationService();
    private int companyId; // Added companyId field

    /**
     * Sets the company ID.
     *
     * @param companyId The ID of the company.
     */
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
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
        if (companyId == 0) {
            System.out.println("Error: Company ID is not set.");
            return;
        }

        try {
            Reclamation reclamation = new Reclamation();
            reclamation.setUserId(Integer.parseInt(userIdField.getText()));
            reclamation.setCompanyId(companyId); // Assign company ID
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
        } catch (NumberFormatException e) {
            System.out.println("Invalid User ID format.");
        }
    }
}
