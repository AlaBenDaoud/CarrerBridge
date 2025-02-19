package org.example.auth.models;

import java.sql.Timestamp;

public class Applicant {
    private int id;
    private int userId;        // Added userId field to link the applicant to a specific user
    private int jobId;
    private int companyId;     // Added companyId field
    private String comment;     // Changed from 'name' to 'comment'
    private String additionalFile; // Changed from 'email' to 'additional_file'
    private Timestamp appliedDate;
    private String status;      // Added the 'status' field

    // Default constructor with default status value
    public Applicant() {
        this.status = "Pending"; // Set the default status to "Pending"
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }  // Getter for 'userId'
    public void setUserId(int userId) { this.userId = userId; }  // Setter for 'userId'

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getComment() { return comment; }  // Getter for 'comment'
    public void setComment(String comment) { this.comment = comment; }  // Setter for 'comment'

    public String getAdditionalFile() { return additionalFile; }  // Getter for 'additional_file'
    public void setAdditionalFile(String additionalFile) { this.additionalFile = additionalFile; }  // Setter for 'additional_file'

    public Timestamp getAppliedDate() { return appliedDate; }
    public void setAppliedDate(Timestamp appliedDate) { this.appliedDate = appliedDate; }

    public String getStatus() { return status; }  // Getter for 'status'
    public void setStatus(String status) { this.status = status; }  // Setter for 'status'
}
