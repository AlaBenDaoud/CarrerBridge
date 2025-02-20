package org.example.auth.controllers.JobAndApplicantion;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.example.auth.models.Reply;
import org.example.auth.services.ReplyService;
import org.example.auth.controllers.connexion.AuthUserController;

public class ReplyController {

    @FXML
    private TextArea replyContent;

    @FXML
    private Button submitReplyButton;

    private final ReplyService replyService = new ReplyService();
    private int postId;

    // Method to set the postId for the reply
    public void setPostId(int postId) {
        this.postId = postId;
    }

    // Method to handle reply submission
    @FXML
    private void submitReply() {
        String content = replyContent.getText().trim();

        // Check if content is empty
        if (content.isEmpty()) {
            System.out.println("La réponse ne peut pas être vide.");
            return;
        }

        // Get the logged-in user's ID
        int loggedInUserId = AuthUserController.getLoggedInUserId();
        if (loggedInUserId == -1) {
            System.err.println("Utilisateur non connecté !");
            return;
        }

        // Create a new Reply object
        Reply reply = new Reply();
        reply.setPostId(postId);
        reply.setUserId(loggedInUserId); // Use the logged-in user's ID
        reply.setContent(content);

        // Add the reply using the ReplyService
        boolean success = replyService.addReply(reply);
        if (success) {
            System.out.println("Réponse ajoutée avec succès !");

            // Close the current window after successful reply submission
            Stage stage = (Stage) submitReplyButton.getScene().getWindow();
            stage.close();
        } else {
            System.out.println("Erreur lors de l'ajout de la réponse.");
        }
    }
}
