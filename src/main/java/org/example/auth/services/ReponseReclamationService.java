package org.example.auth.services;

import org.example.auth.utils.DatabaseService;
import org.example.auth.models.ReponseReclamation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseReclamationService {
    private DatabaseService databaseService = new DatabaseService();

    public ReponseReclamationService() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String query = "CREATE TABLE IF NOT EXISTS reponse_reclamations (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "id_rec INT NOT NULL, " +
                "id_user INT NOT NULL, " +
                "id_receiver INT NOT NULL, " +
                "reponse TEXT NOT NULL, " +
                "pdf_path VARCHAR(255), " +
                "date TIMESTAMP NOT NULL, " +
                "statue_of_reponse_reclamation VARCHAR(50) DEFAULT 'Not Treated', " +
                "FOREIGN KEY (id_rec) REFERENCES reclamations(id) " +
                "ON DELETE CASCADE ON UPDATE CASCADE" +
                ")";
        try (Connection conn = databaseService.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ReponseReclamation> getAllReponseReclamations() throws SQLException {
        List<ReponseReclamation> reponseReclamations = new ArrayList<>();
        String query = "SELECT * FROM reponse_reclamations";
        try (Connection conn = databaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ReponseReclamation reponseReclamation = new ReponseReclamation();
                reponseReclamation.setId(rs.getInt("id"));
                reponseReclamation.setIdRec(rs.getInt("id_rec"));
                reponseReclamation.setIdUser(rs.getInt("id_user"));
                reponseReclamation.setIdReceiver(rs.getInt("id_receiver"));
                reponseReclamation.setReponse(rs.getString("reponse"));
                reponseReclamation.setPdfPath(rs.getString("pdf_path"));
                reponseReclamation.setDate(rs.getTimestamp("date").toLocalDateTime());
                reponseReclamation.setStatueOfReponseReclamation(rs.getString("statue_of_reponse_reclamation"));
                reponseReclamations.add(reponseReclamation);
            }
        }
        return reponseReclamations;
    }

    public void deleteReponseReclamation(int id) throws SQLException {
        String query = "DELETE FROM reponse_reclamations WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public void addReponseReclamation(ReponseReclamation reponseReclamation) throws SQLException {
        String query = "INSERT INTO reponse_reclamations (id_rec, id_user, id_receiver, reponse, pdf_path, date, statue_of_reponse_reclamation) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, reponseReclamation.getIdRec());
            pstmt.setInt(2, reponseReclamation.getIdUser());
            pstmt.setInt(3, reponseReclamation.getIdReceiver());
            pstmt.setString(4, reponseReclamation.getReponse());
            pstmt.setString(5, reponseReclamation.getPdfPath());
            pstmt.setObject(6, reponseReclamation.getDate());
            pstmt.setString(7, reponseReclamation.getStatueOfReponseReclamation());

            // Debug: Print the query and parameters
            System.out.println("Executing query: " + query);
            System.out.println("Parameters: " + reponseReclamation);

            pstmt.executeUpdate();
        }
    }

    // Method to update a response
    public void updateReponseReclamation(ReponseReclamation reponseReclamation) throws SQLException {
        String query = "UPDATE reponse_reclamations SET id_rec = ?, id_user = ?, id_receiver = ?, reponse = ?, pdf_path = ?, date = ?, statue_of_reponse_reclamation = ? WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, reponseReclamation.getIdRec());
            pstmt.setInt(2, reponseReclamation.getIdUser());
            pstmt.setInt(3, reponseReclamation.getIdReceiver());
            pstmt.setString(4, reponseReclamation.getReponse());
            pstmt.setString(5, reponseReclamation.getPdfPath());
            pstmt.setObject(6, reponseReclamation.getDate());
            pstmt.setString(7, reponseReclamation.getStatueOfReponseReclamation());
            pstmt.setInt(8, reponseReclamation.getId());
            pstmt.executeUpdate();
        }
    }

}