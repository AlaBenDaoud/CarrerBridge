package org.example.auth.controllers.JobAndApplicantion;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.example.auth.controllers.connexion.AuthUserController;
import org.example.auth.models.Post;
import org.example.auth.services.PostService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class PostFormController {

    @FXML
    private TextArea postContent;

    @FXML
    private Button handleUploadImage, handleUploadPdf, handleSubmitPost, handleCancel, handleGenerateText;

    private String selectedImagePath = null;
    private String selectedPdfPath = null;

    private final PostService postService = new PostService(); // Instance of the service

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(getWindow());
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            showAlert("Image Selected", "File: " + selectedImagePath, Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void handleUploadPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(getWindow());
        if (selectedFile != null) {
            selectedPdfPath = selectedFile.getAbsolutePath();
            showAlert("PDF Selected", "File: " + selectedPdfPath, Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void handleSubmitPost() {
        String content = postContent.getText();
        int loggedInUserId = AuthUserController.getLoggedInUserId(); // Get the logged-in user ID

        if (loggedInUserId == -1) {
            showAlert("Error", "No user is logged in!", Alert.AlertType.ERROR);
            return;
        }

        if (content.isEmpty()) {
            showAlert("Warning", "Post content cannot be empty.", Alert.AlertType.WARNING);
            return;
        }

        // Create the post
        Post post = new Post();
        post.setUserId(loggedInUserId);
        post.setContent(content);
        post.setImagePath(selectedImagePath);
        post.setPdfPath(selectedPdfPath);

        // Save to the database
        boolean success = postService.addPost(post);

        if (success) {
            showAlert("Success", "Your post has been successfully published!", Alert.AlertType.INFORMATION);
            clearForm();
        } else {
            showAlert("Error", "An error occurred while publishing the post.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        clearForm();
        showAlert("Cancelled", "The post has been cancelled.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleGenerateText() {
        String prompt = postContent.getText(); // Use the current text as a prompt
        if (prompt.isEmpty()) {
            showAlert("Warning", "Please enter a prompt to generate text.", Alert.AlertType.WARNING);
            return;
        }

        // Generate text using Hugging Face API in a background thread
        new Thread(() -> {
            String generatedText = generateTextFromPrompt(prompt);
            Platform.runLater(() -> {
                if (generatedText != null) {
                    postContent.setText(generatedText); // Display the generated text
                } else {
                    showAlert("Error", "Unable to generate text. Please try again.", Alert.AlertType.ERROR);
                }
            });
        }).start();
    }

    private String generateTextFromPrompt(String prompt) {
        try {
            String apiUrl = "https://api-inference.huggingface.co/models/tiiuae/falcon-7b-instruct";
            String apiKey = "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"; // Replace with your actual key

            JSONObject requestBody = new JSONObject();
            requestBody.put("inputs", prompt);
            requestBody.put("parameters", new JSONObject()
                    .put("max_new_tokens", 100)  // Ensures better text completion
                    .put("temperature", 0.5) // Controls randomness
                    .put("top_p", 0.9) // Filters low-probability words
                    .put("stop_sequences", new JSONArray().put("\n"))); // Ensures structured response

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .timeout(Duration.ofSeconds(15))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Debugging: Print response
            System.out.println("API Response: " + response.body());

            // Handle API errors and detect service unavailability
            if (response.body().contains("<html") || response.body().contains("Service Unavailable")) {
                return "Error: AI model is currently unavailable. Please try again later.";
            }

            if (response.statusCode() == 200) {
                JSONArray jsonArray = new JSONArray(response.body());
                if (jsonArray.length() > 0) {
                    JSONObject firstResult = jsonArray.getJSONObject(0);
                    String fullText = firstResult.optString("generated_text", "").trim();

                    // Remove the prompt from generated text
                    if (fullText.startsWith(prompt)) {
                        fullText = fullText.substring(prompt.length()).trim();
                    }

                    return fullText.isEmpty() ? "No meaningful text generated." : fullText;
                }
            } else {
                System.err.println("API Error: " + response.statusCode());
            }
        } catch (IOException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return "Error generating text. Please try again later.";
    }


    /**
     * Clears the form after submission or cancellation.
     */
    private void clearForm() {
        postContent.clear();
        selectedImagePath = null;
        selectedPdfPath = null;
    }

    /**
     * Displays an alert dialog box.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Gets the parent window for file chooser dialogs.
     */
    private Window getWindow() {
        return handleUploadImage.getScene().getWindow();
    }
}
