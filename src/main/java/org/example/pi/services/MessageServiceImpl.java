package org.example.pi.services;

import org.example.pi.models.Message;

import java.util.ArrayList;
import java.util.List;

import java.sql.*;


public class MessageServiceImpl implements MessageService {

    private DatabaseService databaseService = new DatabaseService();

    private static final String INSERT_MESSAGE_SQL = "INSERT INTO messages (sender, receiver, content) VALUES (?, ?, ?)";
    private static final String SELECT_MESSAGES_SQL = "SELECT * FROM messages WHERE receiver = ?";
    private static final String CREATE_USERS_TABLE_SQL = "CREATE TABLE IF NOT EXISTS users ("
            + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
            + "username VARCHAR(255) NOT NULL, "
            + "role VARCHAR(50) NOT NULL"
            + ");";
    private static final String CREATE_MESSAGES_TABLE_SQL = "CREATE TABLE IF NOT EXISTS messages ("
            + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
            + "sender VARCHAR(255) NOT NULL, "
            + "receiver VARCHAR(255) NOT NULL, "
            + "content TEXT NOT NULL, "
            + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + "is_read BOOLEAN DEFAULT FALSE"
            + ");";

    public MessageServiceImpl() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = databaseService.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_USERS_TABLE_SQL);
            stmt.execute(CREATE_MESSAGES_TABLE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(Message message) {
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_MESSAGE_SQL)) {
            pstmt.setString(1, message.getSender());
            pstmt.setString(2, message.getReceiver());
            pstmt.setString(3, message.getContent());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> getMessages(String userId) {
        List<Message> messages = new ArrayList<>();
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_MESSAGES_SQL)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong("id");
                String sender = rs.getString("sender");
                String receiver = rs.getString("receiver");
                String content = rs.getString("content");
                messages.add(new Message(id, sender, receiver, content));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public void markAsRead(Long messageId) {
        // Implémentez la méthode pour marquer un message comme lu, si nécessaire
    }
}