package com.example.testfx;

public class Offre {
    private int id;
    private String titre;
    private String competences;
    private int experience;
    private String localisation;

    // Constructeur
    public Offre(int id, String titre, String competences, int experience, String localisation) {
        this.id = id;
        this.titre = titre;
        this.competences = competences;
        this.experience = experience;
        this.localisation = localisation;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCompetences() {
        return competences;
    }

    public void setCompetences(String competences) {
        this.competences = competences;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }


}
