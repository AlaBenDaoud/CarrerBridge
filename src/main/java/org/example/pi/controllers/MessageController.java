package org.example.pi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.example.pi.models.Message;
import org.example.pi.services.MessageService;
import org.example.pi.services.MessageServiceImpl;

import java.util.List;

public class MessageController {
    @FXML
    private TextArea messageInput;
    @FXML
    private ListView<Message> messageListView;

    private MessageService messageService;

    public MessageController() {
        this.messageService = new MessageServiceImpl();
    }

    @FXML
    public void sendMessage() {
        String content = messageInput.getText();
        Message message = new Message(null, "currentUserId", "receiverId", content); // Remplacez par les ID appropriés
        messageService.sendMessage(message);
        loadMessages();
        messageInput.clear();
    }

    public void loadMessages() {
        List<Message> messages = messageService.getMessages("receiverId"); // Remplacez par l'ID du destinataire
        messageListView.getItems().setAll(messages);
    }
}