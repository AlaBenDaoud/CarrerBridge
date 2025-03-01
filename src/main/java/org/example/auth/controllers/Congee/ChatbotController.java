package org.example.auth.controllers.Congee;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ResourceBundle;

public class ChatbotController implements Initializable {

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private VBox messageContainer;

    @FXML
    private TextField inputField;

    @FXML
    private Button sendButton;

    @FXML
    private Label statusLabel;

    @FXML
    private ProgressIndicator loadingIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initial setup message
        addMessage("bot", "Hello! I'm your AI assistant. How can I help you today?");

        // Auto-scroll to bottom when new messages are added
        messageContainer.heightProperty().addListener((obs, old, newVal) ->
                chatScrollPane.setVvalue(1.0));

        // Enable send button only when input is not empty
        inputField.textProperty().addListener((obs, old, newVal) ->
                sendButton.setDisable(newVal.trim().isEmpty()));

        // Send message on Enter key
        inputField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER") && !inputField.getText().trim().isEmpty()) {
                handleSendMessage();
            }
        });

        loadingIndicator.setVisible(false);
        statusLabel.setVisible(false);
        sendButton.setDisable(true);
    }

    @FXML
    private void handleSendMessage() {
        String userMessage = inputField.getText().trim();
        if (userMessage.isEmpty()) {
            return;
        }

        // Add user message
        addMessage("user", userMessage);
        inputField.clear();

        // Show loading indicator
        loadingIndicator.setVisible(true);
        statusLabel.setText("Bot is typing...");
        statusLabel.setVisible(true);
        sendButton.setDisable(true);
        inputField.setDisable(true);

        new Thread(() -> {
            String botResponse = generateBotResponse(userMessage);
            Platform.runLater(() -> {
                addMessage("bot", botResponse);
                loadingIndicator.setVisible(false);
                statusLabel.setVisible(false);
                sendButton.setDisable(false);
                inputField.setDisable(false);
                inputField.requestFocus();
            });
        }).start();
    }

    private void addMessage(String sender, String message) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.getStyleClass().add("message");
        messageLabel.getStyleClass().add(sender + "-message");

        messageContainer.getChildren().add(messageLabel);
    }

    private String generateBotResponse(String prompt) {
        try {
            // Using BlenderBot model which is optimized for dialogue
            String apiUrl = "https://api-inference.huggingface.co/models/facebook/blenderbot-400M-distill";
            String apiKey = "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"; // Replace with your actual key

            JSONObject requestBody = new JSONObject();
            requestBody.put("inputs", prompt);
            requestBody.put("parameters", new JSONObject()
                    .put("max_length", 50)
                    .put("temperature", 0.8)
                    .put("top_k", 50)
                    .put("do_sample", true));

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .timeout(Duration.ofSeconds(10))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONArray jsonArray = new JSONArray(response.body());
                if (jsonArray.length() > 0) {
                    JSONObject result = jsonArray.getJSONObject(0);
                    String generatedText = result.optString("generated_text", "").trim();
                    return generatedText.isEmpty() ?
                            "I'm not sure how to respond to that." :
                            generatedText;
                }
            }

            switch (response.statusCode()) {
                case 503:
                    return "I'm currently busy. Please try again in a moment.";
                case 429:
                    return "Too many requests. Please wait a bit before trying again.";
                default:
                    return "I'm having trouble understanding. Could you rephrase that?";
            }

        } catch (IOException e) {
            System.err.println("Network error: " + e.getMessage());
            return "I'm having connection issues. Please check your internet connection.";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Our conversation was interrupted. Please try again.";
        } catch (JSONException e) {
            System.err.println("JSON parsing error: " + e.getMessage());
            return "I'm having trouble processing your message.";
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return "Something unexpected happened. Please try again.";
        }
    }
}