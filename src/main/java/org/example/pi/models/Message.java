package org.example.pi.models;


import java.time.LocalDateTime;

public class Message {
    private Long id;
    private String sender; // ID de l'expéditeur
    private String receiver; // ID du destinataire
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;

    public Message(Long id, String sender, String receiver, String content) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}