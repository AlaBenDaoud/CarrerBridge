package org.example.auth.models;

public class Company {
    // Attributes
    private int id;
    private String companyName;
    private String location;
    private String secteur;
    private String email;
    private String password;

    // Constructors
    public Company() {
        // Default constructor
    }

    public Company(String companyName, String location, String secteur, String email, String password) {
        this.companyName = companyName;
        this.location = location;
        this.secteur = secteur;
        this.email = email;
        this.password = password;
    }

    public Company(int id, String companyName, String location, String secteur, String email, String password) {
        this.id = id;
        this.companyName = companyName;
        this.location = location;
        this.secteur = secteur;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Override toString() for debugging purposes
    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", location='" + location + '\'' +
                ", secteur='" + secteur + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
