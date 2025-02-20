package org.example.auth.controllers.JobAndApplicantion;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.example.auth.controllers.connexion.AuthUserController;
import org.example.auth.models.Post;
import org.example.auth.services.PostService;

import java.io.File;

public class PostFormController {

    @FXML
    private TextArea postContent;

    @FXML
    private Button handleUploadImage, handleUploadPdf, handleSubmitPost, handleCancel;

    private String selectedImagePath = null;
    private String selectedPdfPath = null;

    private final PostService postService = new PostService(); // Instance du service

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            showAlert("Image sélectionnée", "Fichier : " + selectedImagePath, Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void handleUploadPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            selectedPdfPath = selectedFile.getAbsolutePath();
            showAlert("PDF sélectionné", "Fichier : " + selectedPdfPath, Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void handleSubmitPost() {
        String content = postContent.getText();
        int loggedInUserId = AuthUserController.getLoggedInUserId(); // Récupération de l'utilisateur connecté

        if (loggedInUserId == -1) {
            showAlert("Erreur", "Aucun utilisateur connecté !", Alert.AlertType.ERROR);
            return;
        }

        if (content.isEmpty()) {
            showAlert("Attention", "Le contenu du post ne peut pas être vide.", Alert.AlertType.WARNING);
            return;
        }

        // Création du post
        Post post = new Post();
        post.setUserId(loggedInUserId);
        post.setContent(content);
        post.setImagePath(selectedImagePath);
        post.setPdfPath(selectedPdfPath);

        // Enregistrement dans la base de données
        boolean success = postService.addPost(post);

        if (success) {
            showAlert("Succès", "Votre post a été publié avec succès !", Alert.AlertType.INFORMATION);
            clearForm();
        } else {
            showAlert("Erreur", "Une erreur s'est produite lors de la publication du post.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        clearForm();
        showAlert("Annulation", "Le post a été annulé.", Alert.AlertType.INFORMATION);
    }

    /**
     * Nettoie le formulaire après soumission ou annulation.
     */
    private void clearForm() {
        postContent.clear();
        selectedImagePath = null;
        selectedPdfPath = null;
    }

    /**
     * Affiche une boîte de dialogue d'alerte.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
