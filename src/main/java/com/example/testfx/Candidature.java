package com.example.testfx;

public class Candidature {
    private String candidat;
    private String offre;
    private String datePostulation;
    private int userId;

    public Candidature(String candidat, String offre, String datePostulation) {
        this.candidat = candidat;
        this.offre = offre;
        this.datePostulation = datePostulation;
        this.userId = userId;
    }

    public String getCandidat() {
        return candidat;
    }

    public String getOffre() {
        return offre;
    }

    public String getDatePostulation() {
        return datePostulation;
    }
    public int getUserId() {
        return userId; // Getter pour l'ID de l'utilisateur
    }
}
