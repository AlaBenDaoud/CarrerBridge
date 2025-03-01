package org.example.auth.models;

import java.time.LocalDateTime;

public class Reclamation {
    private int id;
    private int userId;
    private int companyId; // Changed from receiver (String) to companyId (int)
    private String title;
    private String description;
    private String imagePath;
    private String pdfPath;
    private LocalDateTime date;
    private String statueOfReclamation = "Not Treated"; // Default value

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatueOfReclamation() {
        return statueOfReclamation;
    }

    public void setStatueOfReclamation(String statueOfReclamation) {
        this.statueOfReclamation = statueOfReclamation;
    }
}
