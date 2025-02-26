package org.example.pi.services;

import org.example.pi.models.Message;

import java.util.List;

public interface MessageService {
    void sendMessage(Message message);
    List<Message> getMessages(String userId);
    void markAsRead(Long messageId);
}
