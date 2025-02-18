package com.example.testfx;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class OffreDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/careerbridge";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public List<Offre> getOffresFromDatabase() {
        List<Offre> offres = new ArrayList<>();
        String query = "SELECT id, titre, competences, experience, localisation FROM offres";

        // Utilisation d'un try-with-resources pour gérer automatiquement la fermeture des ressources
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Parcourir les résultats de la requête
            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String competences = rs.getString("competences");
                int experience = rs.getInt("experience");
                String localisation = rs.getString("localisation");

                // Créer un objet Offre et l'ajouter à la liste
                Offre offre = new Offre(id, titre, competences, experience, localisation);
                offres.add(offre);
            }
        } catch (SQLException e) {
            // Gestion des erreurs SQL
            System.err.println("Erreur lors de la récupération des offres depuis la base de données :");
            e.printStackTrace();
        }

        return offres;
    }
}
