package org.example.pi.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.example.pi.models.Reclamation;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService {
    private DatabaseService databaseService = new DatabaseService();

    // Constructor to check and create table if not exists
    public ReclamationService() {
        createTableIfNotExists();
    }

    // Method to create the reclamations table if it doesn't exist
    private void createTableIfNotExists() {
        String query = "CREATE TABLE IF NOT EXISTS reclamations (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "receiver VARCHAR(255) NOT NULL, " +
                "title VARCHAR(255) NOT NULL, " +
                "description TEXT NOT NULL, " +
                "image_path VARCHAR(255), " +
                "pdf_path VARCHAR(255), " +
                "date TIMESTAMP NOT NULL, " +
                "statue_of_reclamation VARCHAR(50) DEFAULT 'Not Treated'" +
                ")";
        try (Connection conn = databaseService.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a new reclamation to the database
    public void addReclamation(Reclamation reclamation) throws SQLException {
        String query = "INSERT INTO reclamations (user_id, receiver, title, description, image_path, pdf_path, date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, reclamation.getUserId());
            pstmt.setString(2, reclamation.getReceiver());
            pstmt.setString(3, reclamation.getTitle());
            pstmt.setString(4, reclamation.getDescription());
            pstmt.setString(5, reclamation.getImagePath());
            pstmt.setString(6, reclamation.getPdfPath());
            pstmt.setObject(7, reclamation.getDate());
            pstmt.executeUpdate();
        }
    }
    // Method to get all reclamations
    public List<Reclamation> getAllReclamations() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamations";
        try (Connection conn = databaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(rs.getInt("id"));
                reclamation.setUserId(rs.getInt("user_id"));
                reclamation.setReceiver(rs.getString("receiver"));
                reclamation.setTitle(rs.getString("title"));
                reclamation.setDescription(rs.getString("description"));
                reclamation.setImagePath(rs.getString("image_path"));
                reclamation.setPdfPath(rs.getString("pdf_path"));
                reclamation.setDate(rs.getTimestamp("date").toLocalDateTime());
                reclamation.setStatueOfReclamation(rs.getString("statue_of_reclamation"));
                reclamations.add(reclamation);
            }
        }
        return reclamations;
    }

    public Reclamation getReclamationById(int id) throws SQLException {
        String query = "SELECT * FROM reclamations WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Reclamation reclamation = new Reclamation();
                    reclamation.setId(rs.getInt("id"));
                    reclamation.setUserId(rs.getInt("user_id"));
                    reclamation.setReceiver(rs.getString("receiver"));
                    reclamation.setTitle(rs.getString("title"));
                    reclamation.setDescription(rs.getString("description"));
                    reclamation.setImagePath(rs.getString("image_path"));
                    reclamation.setPdfPath(rs.getString("pdf_path"));
                    reclamation.setDate(rs.getTimestamp("date").toLocalDateTime());
                    reclamation.setStatueOfReclamation(rs.getString("statue_of_reclamation"));
                    return reclamation;
                }
            }
        }
        return null;
    }

    // Method to delete a reclamation by ID
    public void deleteReclamation(int id) throws SQLException {
        String query = "DELETE FROM reclamations WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    // Method to update a reclamation
    public void updateReclamation(Reclamation reclamation) throws SQLException {
        String query = "UPDATE reclamations SET user_id = ?, receiver = ?, title = ?, description = ?, image_path = ?, pdf_path = ?, date = ?, statue_of_reclamation = ? WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, reclamation.getUserId());
            pstmt.setString(2, reclamation.getReceiver());
            pstmt.setString(3, reclamation.getTitle());
            pstmt.setString(4, reclamation.getDescription());
            pstmt.setString(5, reclamation.getImagePath());
            pstmt.setString(6, reclamation.getPdfPath());
            pstmt.setObject(7, reclamation.getDate());
            pstmt.setString(8, reclamation.getStatueOfReclamation());
            pstmt.setInt(9, reclamation.getId());
            pstmt.executeUpdate();
        }
    }
}