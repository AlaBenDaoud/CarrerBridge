package org.example.auth.controllers.JobAndApplicantion;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.example.auth.models.Reply;
import org.example.auth.services.ReplyService;

public class UpdateReplyController {

    @FXML
    private TextArea replyContentArea;

    private int replyId;
    private final ReplyService replyService = new ReplyService();

    // Setter to pass the replyId from the main controller
    public void setReplyId(int replyId) {
        this.replyId = replyId;
        loadReplyContent();
    }

    // Load the current content of the reply
    private void loadReplyContent() {
        Reply reply = replyService.getReplyById(replyId);
        if (reply != null) {
            replyContentArea.setText(reply.getContent());
        }
    }

    // Handle the update of the reply
    @FXML
    private void handleUpdateReply() {
        String newContent = replyContentArea.getText();

        if (newContent.isEmpty()) {
            System.out.println("Content cannot be empty!");
            return;
        }

        Reply reply = new Reply();
        reply.setId(replyId);
        reply.setContent(newContent);

        boolean success = replyService.updateReply(reply);
        if (success) {
            System.out.println("Reply updated successfully!");
        } else {
            System.err.println("Failed to update the reply.");
        }

        closeWindow();
    }

    // Cancel and close the window
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    // Close the window
    private void closeWindow() {
        Stage stage = (Stage) replyContentArea.getScene().getWindow();
        stage.close();
    }
}
