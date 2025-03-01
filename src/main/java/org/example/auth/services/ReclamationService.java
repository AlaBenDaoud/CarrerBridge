package org.example.auth.services;

import org.example.auth.models.Reclamation;
import org.example.auth.utils.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService {
    private DatabaseService databaseService = new DatabaseService();

    // Add a new reclamation to the database
    public void addReclamation(Reclamation reclamation) throws SQLException {
        String query = "INSERT INTO reclamations (user_id, company_id, title, description, image_path, pdf_path, date, statue_of_reclamation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, reclamation.getUserId());
            pstmt.setInt(2, reclamation.getCompanyId());
            pstmt.setString(3, reclamation.getTitle());
            pstmt.setString(4, reclamation.getDescription());
            pstmt.setString(5, reclamation.getImagePath());
            pstmt.setString(6, reclamation.getPdfPath());
            pstmt.setObject(7, reclamation.getDate());
            pstmt.setString(8, reclamation.getStatueOfReclamation()); // Default: "Not Treated"
            pstmt.executeUpdate();
        }
    }

    // Get all reclamations
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
                reclamation.setCompanyId(rs.getInt("company_id"));
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

    // Get a reclamation by ID
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
                    reclamation.setCompanyId(rs.getInt("company_id"));
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

    // Delete a reclamation by ID
    public void deleteReclamation(int id) throws SQLException {
        String query = "DELETE FROM reclamations WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // Update a reclamation
    public void updateReclamation(Reclamation reclamation) throws SQLException {
        String query = "UPDATE reclamations SET user_id = ?, company_id = ?, title = ?, description = ?, image_path = ?, pdf_path = ?, date = ?, statue_of_reclamation = ? WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, reclamation.getUserId());
            pstmt.setInt(2, reclamation.getCompanyId());
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

    // Update reclamation status only
    public void updateReclamationStatus(int id, String status) throws SQLException {
        String query = "UPDATE reclamations SET statue_of_reclamation = ? WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        }
    }

    // Get reclamations by user_id (employee)
    public List<Reclamation> getReclamationsByUserId(int userId) throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamations WHERE user_id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reclamation reclamation = new Reclamation();
                    reclamation.setId(rs.getInt("id"));
                    reclamation.setUserId(rs.getInt("user_id"));
                    reclamation.setCompanyId(rs.getInt("company_id"));
                    reclamation.setTitle(rs.getString("title"));
                    reclamation.setDescription(rs.getString("description"));
                    reclamation.setImagePath(rs.getString("image_path"));
                    reclamation.setPdfPath(rs.getString("pdf_path"));
                    reclamation.setDate(rs.getTimestamp("date").toLocalDateTime());
                    reclamation.setStatueOfReclamation(rs.getString("statue_of_reclamation"));
                    reclamations.add(reclamation);
                }
            }
        }
        return reclamations;
    }
}
