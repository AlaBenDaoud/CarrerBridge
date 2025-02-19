package org.example.auth.models;

import java.sql.Timestamp;

public class Job {
    private int id;
    private String title;
    private String description;
    private int companyId; // Company ID field (manually provided)
    private String position; // Changed from companyName to position
    private String location;
    private Timestamp postedDate;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getPosition() { // Changed from getCompanyName to getPosition
        return position;
    }

    public void setPosition(String position) { // Changed from setCompanyName to setPosition
        this.position = position;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Timestamp postedDate) {
        this.postedDate = postedDate;
    }
}
