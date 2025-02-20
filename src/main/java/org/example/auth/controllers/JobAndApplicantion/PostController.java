package org.example.auth.controllers.JobAndApplicantion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.auth.models.Post;
import org.example.auth.models.Reply;

import org.example.auth.services.PostService;
import org.example.auth.services.ReplyService;
import org.example.auth.controllers.JobAndApplicantion.ReplyController;

import java.io.IOException;
import java.util.List;

public class PostController {

    @FXML
    private VBox postContainer;

    private final PostService postService = new PostService();
    private final ReplyService replyService = new ReplyService();

    @FXML
    public void initialize() {
        loadPosts();
    }

    private void loadPosts() {
        postContainer.getChildren().clear();
        List<Post> posts = postService.getAllPosts();

        for (Post post : posts) {
            postContainer.getChildren().add(createPostCard(post));
        }
    }

    private VBox createPostCard(Post post) {
        VBox card = new VBox();
        card.setSpacing(5);
        card.setStyle("-fx-padding: 10; -fx-background-color: #f8f9fa; -fx-border-radius: 10; -fx-border-color: #ddd; -fx-background-radius: 10;");
        card.setPrefWidth(600);

        Label content = new Label(post.getContent());
        content.setWrapText(true);
        content.setPrefWidth(580);

        Label createdAt = new Label("Publié le : " + post.getCreatedAt());
        createdAt.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        HBox interactionBox = new HBox(10);
        Button likeButton = new Button("👍 " + post.getLikeCount());
        Button dislikeButton = new Button("👎 " + post.getDislikeCount());
        Button replyButton = new Button("💬 Répondre");

        likeButton.setOnAction(e -> updateLikeCount(post.getId(), true, likeButton));
        dislikeButton.setOnAction(e -> updateDislikeCount(post.getId(), true, dislikeButton));
        replyButton.setOnAction(e -> openReplyPopup(post.getId()));

        interactionBox.getChildren().addAll(likeButton, dislikeButton, replyButton);

        // Create a VBox for replies to be added under each post
        VBox repliesContainer = new VBox(5);
        repliesContainer.setStyle("-fx-padding: 10; -fx-background-color: #f1f1f1; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Load and display replies under the post
        loadRepliesForPost(post.getId(), repliesContainer);

        card.getChildren().addAll(content, createdAt, interactionBox, repliesContainer);
        return card;
    }

    private void loadRepliesForPost(int postId, VBox repliesContainer) {
        // Get the list of replies for this post
        List<Reply> replies = replyService.getRepliesByPostId(postId);

        // Iterate through the replies and add them as labels
        for (Reply reply : replies) {
            Label replyContent = new Label(reply.getContent());
            replyContent.setStyle("-fx-font-size: 12px; -fx-text-fill: #333; -fx-wrap-text: true;");
            repliesContainer.getChildren().add(replyContent);
        }
    }

    private void openReplyPopup(int postId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/reply.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Répondre au post");

            Scene scene = new Scene(loader.load());
            ReplyController controller = loader.getController();
            controller.setPostId(postId);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture de la fenêtre de réponse: " + e.getMessage());
        }
    }

    private void updateLikeCount(int postId, boolean increment, Button button) {
        boolean success = postService.updateLikeCount(postId, increment);
        if (success) {
            int currentValue = Integer.parseInt(button.getText().split(" ")[1]);
            button.setText(button.getText().split(" ")[0] + " " + (increment ? currentValue + 1 : currentValue - 1));
        }
    }

    private void updateDislikeCount(int postId, boolean increment, Button button) {
        boolean success = postService.updateDislikeCount(postId, increment);
        if (success) {
            int currentValue = Integer.parseInt(button.getText().split(" ")[1]);
            button.setText(button.getText().split(" ")[0] + " " + (increment ? currentValue + 1 : currentValue - 1));
        }
    }
}
