package org.example.demo.jobboardapp.Models;

import java.sql.Timestamp;

public class Applicant {
    private int id;
    private int jobId;
    private int companyId; // Added companyId field
    private String name;
    private String email;
    private String resume;
    private Timestamp appliedDate;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }

    public Timestamp getAppliedDate() { return appliedDate; }
    public void setAppliedDate(Timestamp appliedDate) { this.appliedDate = appliedDate; }
}