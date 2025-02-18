package com.example.testfx;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class ModifierOffreController {

    @FXML
    private TextField titreField;

    @FXML
    private TextField competencesField;

    @FXML
    private TextField experienceField;

    @FXML
    private TextField localisationField;

    private Offre offre;

    public void setOffre(Offre offre) {
        this.offre = offre;
        titreField.setText(offre.getTitre());
        competencesField.setText(offre.getCompetences());
        experienceField.setText(String.valueOf(offre.getExperience()));
        localisationField.setText(offre.getLocalisation());
    }

    @FXML
    private void enregistrerModifications() {
        offre.setTitre(titreField.getText());
        offre.setCompetences(competencesField.getText());
        offre.setExperience(Integer.parseInt(experienceField.getText()));
        offre.setLocalisation(localisationField.getText());

        // Mettre à jour l'offre dans la base de données
        updateOffreInDatabase(offre);

        // Fermer la fenêtre de modification
        titreField.getScene().getWindow().hide();
    }


    private void updateOffreInDatabase(Offre offre) {
        String query = "UPDATE offres SET titre = ?, competences = ?, experience = ?, localisation = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/careerbridge", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, offre.getTitre());
            stmt.setString(2, offre.getCompetences());
            stmt.setInt(3, offre.getExperience());
            stmt.setString(4, offre.getLocalisation());
            stmt.setInt(5, offre.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
