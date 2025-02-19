package org.example.demo.controller;// Indique le package dans lequel cette classe est définie

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;//bibliothèque PDFBox pour manipuler des documents PDF
import org.apache.pdfbox.rendering.PDFRenderer;
import javafx.embed.swing.SwingFXUtils;
import org.example.demo.model.LeaveRequest;
import org.example.demo.service.LeaveRequestService;

import java.awt.image.BufferedImage;//référence à l'Abstract Window Toolkit
import java.io.File;//gérer l'entrée/sortie de données, telles que la lecture et l'écriture de fichiers.
import java.io.IOException;

public class DetailsPopupController {
    @FXML
    private Label idLabel;
    @FXML
    private Label employeeIdLabel;
    @FXML
    private Label startDateLabel;
    @FXML
    private Label endDateLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label leaveTypeLabel;
    @FXML
    private Label pdfPathLabel;
    @FXML
    private Label isConfirmedLabel;
    @FXML
    private Button confirmButton;
    @FXML
    private Button notConfirmButton;
    @FXML
    private Button closeButton;
    @FXML
    private ImageView pdfImageView;

    private LeaveRequest leaveRequest;
    private LeaveRequestService leaveRequestService = new LeaveRequestService();

    public void setLeaveRequest(LeaveRequest request) {
        this.leaveRequest = request;

        // Populate labels with leave request details
        idLabel.setText(String.valueOf(request.getId()));
        employeeIdLabel.setText(String.valueOf(request.getEmployeeId()));
        startDateLabel.setText(request.getStartDate().toString());
        endDateLabel.setText(request.getEndDate().toString());
        descriptionLabel.setText(request.getDescription());
        leaveTypeLabel.setText(request.getLeaveType());
        pdfPathLabel.setText(request.getPdfPath());
        isConfirmedLabel.setText(request.isConfirmed() ? "Yes" : "No");

        // Load and render the PDF preview
        loadPdfPreview(request.getPdfPath());
    }

    private void loadPdfPreview(String pdfPath) {
        try {
            File file = new File(pdfPath);
            if (!file.exists()) {
                System.out.println("PDF file does not exist: " + pdfPath);
                return;
            }

            // Load the PDF document and render the first page
            PDDocument document = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage bufferedImage = renderer.renderImage(0); // Render the first page

            // Convert BufferedImage to JavaFX Image
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);

            // Set the image to the ImageView
            pdfImageView.setImage(image);

            // Close the document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleConfirm() {
        leaveRequest.setConfirmed(true);
        leaveRequestService.updateLeaveRequest(leaveRequest);
        isConfirmedLabel.setText("Yes");

    }

    @FXML
    private void handleNotConfirm() {
        leaveRequest.setConfirmed(false);
        leaveRequestService.updateLeaveRequest(leaveRequest);
        isConfirmedLabel.setText("No");
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}