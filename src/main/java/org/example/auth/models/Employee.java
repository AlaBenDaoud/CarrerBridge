package org.example.auth.models;

public class Employee {
    private int id;
    private int companyId;
    private int userId;
    private int jobId;  // Add jobId field

    // Constructor
    public Employee(int id, int companyId, int userId, int jobId) {
        this.id = id;
        this.companyId = companyId;
        this.userId = userId;
        this.jobId = jobId;  // Initialize jobId
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", companyId=" + companyId + ", userId=" + userId + ", jobId=" + jobId + "}";
    }
}
