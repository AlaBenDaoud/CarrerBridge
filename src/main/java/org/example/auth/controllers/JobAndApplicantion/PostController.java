package org.example.auth.controllers.JobAndApplicantion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.auth.models.Post;
import org.example.auth.models.Reply;
import org.example.auth.services.PostService;
import org.example.auth.services.ReplyService;
import org.example.auth.controllers.connexion.AuthUserController;

// Import PDFRenderer library
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javafx.embed.swing.SwingFXUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PostController {

    @FXML
    private VBox postContainer;

    private final PostService postService = new PostService();
    private final ReplyService replyService = new ReplyService();

    // Get the logged-in user ID dynamically from AuthUserController
    private int loggedInUserId = AuthUserController.getLoggedInUserId();

    @FXML
    public void initialize() {
        loadPosts();
    }

    @FXML
    private void refreshPosts() {
        loadPosts();  // This will reload all the posts
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
        card.setSpacing(10);
        card.setStyle("-fx-padding: 15; -fx-background-color: rgba(30, 41, 59, 0.7); -fx-border-radius: 10; -fx-border-color: rgba(59, 130, 246, 0.2); -fx-background-radius: 10;");
        card.setPrefWidth(600);

        Label content = new Label(post.getContent());
        content.setWrapText(true);
        content.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #f8fafc;");

        Label createdAt = new Label("Publié le : " + post.getCreatedAt());
        createdAt.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #94a3b8;");

        HBox interactionBox = new HBox(10);
        Button likeButton = new Button("👍 " + post.getLikeCount());
        Button dislikeButton = new Button("👎 " + post.getDislikeCount());
        Button replyButton = new Button("💬 Répondre");

        likeButton.setStyle("-fx-text-fill: white; -fx-background-color: #3b82f6; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");
        dislikeButton.setStyle("-fx-text-fill: white; -fx-background-color: #3b82f6; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");
        replyButton.setStyle("-fx-text-fill: white; -fx-background-color: #3b82f6; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");

        likeButton.setOnAction(e -> updateLikeCount(post.getId(), true, likeButton));
        dislikeButton.setOnAction(e -> updateDislikeCount(post.getId(), true, dislikeButton));
        replyButton.setOnAction(e -> openReplyPopup(post.getId()));

        interactionBox.getChildren().addAll(likeButton, dislikeButton, replyButton);

        // Create a VBox for replies to be added under each post
        VBox repliesContainer = new VBox(5);
        repliesContainer.setStyle("-fx-padding: 10; -fx-background-color: rgba(30, 41, 59, 0.5); -fx-border-radius: 10; -fx-background-radius: 10;");

        // Load and display replies under the post
        loadRepliesForPost(post.getId(), repliesContainer);

        // Create an HBox to hold image and PDF previews side by side
        HBox mediaContainer = new HBox(10); // 10 is the spacing between image and PDF
        mediaContainer.setStyle("-fx-padding: 10;");

        // Render image if it exists
        if (post.getImagePath() != null && !post.getImagePath().isEmpty()) {
            ImageView imageView = createImageView(post.getImagePath(), true);
            mediaContainer.getChildren().add(imageView);
        }

        // Render PDF if it exists
        if (post.getPdfPath() != null && !post.getPdfPath().isEmpty()) {
            try {
                ImageView pdfImageView = createPdfImageView(post.getPdfPath());
                if (pdfImageView != null) {
                    mediaContainer.getChildren().add(pdfImageView);
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la conversion du PDF en image: " + e.getMessage());
            }
        }

        // Add media container to the card if it contains any media
        if (!mediaContainer.getChildren().isEmpty()) {
            card.getChildren().add(mediaContainer);
        }

        card.getChildren().addAll(content, createdAt, interactionBox, repliesContainer);
        return card;
    }

    private ImageView createImageView(String imagePath, boolean isPreview) {
        ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toString()));
        if (isPreview) {
            imageView.setFitWidth(200);  // Smaller size for preview
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
        }
        imageView.setOnMouseClicked(e -> openFullSizeImage(imagePath));
        return imageView;
    }

    private ImageView createPdfImageView(String pdfPath) throws IOException {
        File pdfFile = new File(pdfPath);
        if (!pdfFile.exists()) {
            System.err.println("PDF file not found: " + pdfPath);
            return null;
        }

        // Convert the first page of the PDF to an image
        Image pdfImage = convertPdfToImage(pdfFile);
        if (pdfImage == null) {
            return null;
        }

        ImageView pdfImageView = new ImageView(pdfImage);
        pdfImageView.setFitWidth(200);  // Smaller size for preview
        pdfImageView.setPreserveRatio(true);
        pdfImageView.setSmooth(true);
        pdfImageView.setCache(true);
        pdfImageView.setOnMouseClicked(e -> openFullSizePdf(pdfPath));
        return pdfImageView;
    }

    private Image convertPdfToImage(File pdfFile) throws IOException {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            // Render the first page of the PDF
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300); // 300 DPI for good quality
            // Convert BufferedImage to JavaFX Image
            return SwingFXUtils.toFXImage(bufferedImage, null);
        }
    }

    private void openFullSizeImage(String imagePath) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Image Full Size");

        ImageView fullSizeImageView = new ImageView(new Image(new File(imagePath).toURI().toString()));
        fullSizeImageView.setFitWidth(600);  // Larger size for full view
        fullSizeImageView.setPreserveRatio(true);

        VBox vbox = new VBox(fullSizeImageView);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

    private void openFullSizePdf(String pdfPath) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("PDF Full Size");

            // Create a VBox to hold all PDF pages
            VBox pdfPagesContainer = new VBox(10); // 10 is the spacing between pages
            pdfPagesContainer.setStyle("-fx-padding: 10;");

            // Load the PDF document
            File pdfFile = new File(pdfPath);
            try (PDDocument document = PDDocument.load(pdfFile)) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);

                // Render each page of the PDF
                for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
                    BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageIndex, 300); // 300 DPI for good quality
                    Image pdfImage = SwingFXUtils.toFXImage(bufferedImage, null);

                    // Create an ImageView for the page
                    ImageView pageImageView = new ImageView(pdfImage);
                    pageImageView.setFitWidth(600); // Set width for full-size view
                    pageImageView.setPreserveRatio(true);

                    // Add the page to the container
                    pdfPagesContainer.getChildren().add(pageImageView);
                }
            }

            // Wrap the VBox in a ScrollPane
            ScrollPane scrollPane = new ScrollPane(pdfPagesContainer);
            scrollPane.setFitToWidth(true); // Ensure the ScrollPane fits the width of the window

            // Create a scene and show the stage
            Scene scene = new Scene(scrollPane, 620, 800); // Set initial window size
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture du PDF en taille réelle: " + e.getMessage());
        }
    }

    private void loadRepliesForPost(int postId, VBox repliesContainer) {
        // Get the list of replies for this post
        List<Reply> replies = replyService.getRepliesByPostId(postId);

        // Iterate through the replies and add them as labels
        for (Reply reply : replies) {
            Label replyContent = new Label(reply.getContent());
            replyContent.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #f8fafc; -fx-wrap-text: true;");

            // Create buttons for delete and update if the logged-in user is the author
            if (reply.getUserId() == loggedInUserId) {
                Button updateButton = new Button("Modifier");
                Button deleteButton = new Button("Supprimer");

                updateButton.setStyle("-fx-text-fill: white; -fx-background-color: #3b82f6; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6px 12px; -fx-background-radius: 15px; -fx-cursor: hand;");
                deleteButton.setStyle("-fx-text-fill: white; -fx-background-color: #ef4444; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6px 12px; -fx-background-radius: 15px; -fx-cursor: hand;");

                updateButton.setOnAction(e -> openUpdateReplyPopup(reply.getId()));
                deleteButton.setOnAction(e -> deleteReply(reply.getId(), repliesContainer));

                HBox buttonBox = new HBox(10, updateButton, deleteButton);
                repliesContainer.getChildren().addAll(replyContent, buttonBox);
            } else {
                repliesContainer.getChildren().add(replyContent);
            }
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

    private void openUpdateReplyPopup(int replyId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/update_reply.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier la réponse");

            Scene scene = new Scene(loader.load());
            UpdateReplyController controller = loader.getController();
            controller.setReplyId(replyId);  // Pass the replyId to the controller

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture de la fenêtre de modification de la réponse: " + e.getMessage());
        }
    }

    private void deleteReply(int replyId, VBox repliesContainer) {
        // Handle deleting the reply
        boolean success = replyService.deleteReply(replyId);
        if (success) {
            repliesContainer.getChildren().clear();
            loadRepliesForPost(replyId, repliesContainer);  // Reload replies after deletion
        } else {
            System.err.println("Failed to delete the reply.");
        }
    }
}