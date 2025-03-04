package org.example.pi.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.example.pi.models.Reclamation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class ReclamationService {
    private DatabaseService databaseService = new DatabaseService();

    private static final List<String> BAD_WORDS = Arrays.asList(" badword1 ", "badword2", "badword3");


    private String sanitizeText(String text) {
        String textLower = text;
        for (String badWord : BAD_WORDS) {
            textLower = textLower.replaceAll("(?i)\\b" + Pattern.quote(badWord) + "\\b", "****");
        }
        return textLower;
    }





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
        reclamation.setDescription(sanitizeText(reclamation.getDescription()));


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

            String companyEmail = getCompanyEmail(reclamation.getCompanyId());

            // Envoyer l'email de notification
            sendEmail(companyEmail,
                    "Nouvelle Réclamation Soumise",
                    "Un utilisateur a soumis une nouvelle réclamation avec le titre : " + reclamation.getTitle() + "\nDescription : " + reclamation.getDescription());
        }

        // Ajoute cette méthode pour envoyer un email
        public void sendEmail(String toEmail, String subject, String body) {
            final String fromEmail = "alabendawed@gmail.com"; // Remplace avec ton email
            final String password = "nwrcacslrklwfanu"; // Remplace avec ton mot de passe

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject(subject);
                message.setText(body);
                Transport.send(message);

                System.out.println("E-mail envoyé avec succès !");
            } catch (MessagingException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de l'envoi de l'e-mail.");
            }
        }

        public String getCompanyEmail(int companyId) throws SQLException {
            String query = "SELECT email FROM companies WHERE id = ?";
            try (Connection conn = databaseService.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, companyId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("email");
                    }
                }
            }
            return null;
        }

   /* private static final List<String> BAD_WORDS = Arrays.asList(
            "insulte1", "insulte2", "motgrossier", "exemple" // Ajoute les mots interdits ici
    );*/

        // Vérifier si la description contient un mot interdit
        private boolean containsBadWords(String text) {
            String textLower = text.toLowerCase();
            for (String badWord : BAD_WORDS) {
                if (Pattern.compile("\\b" + badWord + "\\b", Pattern.CASE_INSENSITIVE).matcher(textLower).find()) {
                    return true;
                }
            }
            return false;
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
    public void updateReclamationStatus(Reclamation reclamation) throws SQLException {
        String query = "UPDATE reclamations SET statue_of_reclamation = ? WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, reclamation.getStatueOfReclamation());
            pstmt.setInt(2, reclamation.getId());
            pstmt.executeUpdate();
        }
    }
}