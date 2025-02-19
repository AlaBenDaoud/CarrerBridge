package org.example.auth.services;

import org.example.auth.models.Applicant;
import org.example.auth.models.Job;
import org.example.auth.utils.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicantService {

    private final DatabaseService databaseService;
    private final JobService jobService;

    public ApplicantService() {
        this.databaseService = new DatabaseService();
        this.jobService = new JobService();
    }

    /**
     * Adds a new applicant to the database.
     *
     * @param applicant The applicant to add.
     * @return True if the applicant was added successfully, false otherwise.
     */
    public boolean addApplicant(Applicant applicant) {
        // Fetch the job to get the companyId
        Job job = jobService.getJobById(applicant.getJobId());
        if (job == null) {
            System.err.println("Job not found for ID: " + applicant.getJobId());
            return false;
        }

        // Set the companyId from the job
        applicant.setCompanyId(job.getCompanyId());

        String query = "INSERT INTO applicants (user_id, job_id, company_id, comment, additional_file, applied_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, applicant.getUserId());  // Set userId when adding
            stmt.setInt(2, applicant.getJobId());
            stmt.setInt(3, applicant.getCompanyId());
            stmt.setString(4, applicant.getComment());
            stmt.setString(5, applicant.getAdditionalFile());
            stmt.setTimestamp(6, applicant.getAppliedDate());
            stmt.setString(7, applicant.getStatus());  // Set status field

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding applicant: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all applicants associated with a specific company ID.
     *
     * @param companyId The ID of the company.
     * @return A list of applicants associated with the company.
     */
    public List<Applicant> getApplicationsByCompanyId(int companyId) {
        List<Applicant> applicants = new ArrayList<>();
        String query = "SELECT * FROM applicants WHERE company_id = ?";

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Applicant applicant = new Applicant();
                applicant.setId(rs.getInt("id"));
                applicant.setUserId(rs.getInt("user_id"));
                applicant.setJobId(rs.getInt("job_id"));
                applicant.setCompanyId(rs.getInt("company_id"));
                applicant.setComment(rs.getString("comment"));
                applicant.setAdditionalFile(rs.getString("additional_file"));
                applicant.setAppliedDate(rs.getTimestamp("applied_date"));
                applicant.setStatus(rs.getString("status"));  // Get the status field
                applicants.add(applicant);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving applicants by company ID: " + e.getMessage());
        }

        return applicants;
    }

    /**
     * Retrieves an applicant by their ID.
     *
     * @param id The ID of the applicant.
     * @return The applicant with the specified ID, or null if not found.
     */
    public Applicant getApplicantById(int id) {
        String query = "SELECT * FROM applicants WHERE id = ?";

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Applicant applicant = new Applicant();
                applicant.setId(rs.getInt("id"));
                applicant.setUserId(rs.getInt("user_id"));
                applicant.setJobId(rs.getInt("job_id"));
                applicant.setCompanyId(rs.getInt("company_id"));
                applicant.setComment(rs.getString("comment"));
                applicant.setAdditionalFile(rs.getString("additional_file"));
                applicant.setAppliedDate(rs.getTimestamp("applied_date"));
                applicant.setStatus(rs.getString("status"));  // Get the status field
                return applicant;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving applicant by ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Deletes an applicant by their ID.
     *
     * @param id The ID of the applicant to delete.
     * @return True if the applicant was deleted successfully, false otherwise.
     */
    public boolean deleteApplicantById(int id) {
        String query = "DELETE FROM applicants WHERE id = ?";

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting applicant: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing applicant in the database.
     *
     * @param applicant The applicant to update.
     * @return True if the applicant was updated successfully, false otherwise.
     */
    public boolean updateApplicant(Applicant applicant) {
        String query = "UPDATE applicants SET user_id = ?, job_id = ?, comment = ?, additional_file = ?, applied_date = ?, status = ? WHERE id = ?";

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, applicant.getUserId());  // Update userId
            stmt.setInt(2, applicant.getJobId());
            stmt.setString(3, applicant.getComment());
            stmt.setString(4, applicant.getAdditionalFile());
            stmt.setTimestamp(5, applicant.getAppliedDate());
            stmt.setString(6, applicant.getStatus());  // Update the status field
            stmt.setInt(7, applicant.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating applicant: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all applications for a specific applicant by their ID.
     *
     * @param applicantId The ID of the applicant.
     * @return A list of applications for the applicant.
     */
    public List<Applicant> getApplicationsByApplicantId(int applicantId) {
        List<Applicant> applications = new ArrayList<>();
        String query = "SELECT * FROM applicants WHERE user_id = ?";

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, applicantId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Applicant applicant = new Applicant();
                applicant.setId(rs.getInt("id"));
                applicant.setUserId(rs.getInt("user_id"));
                applicant.setJobId(rs.getInt("job_id"));
                applicant.setCompanyId(rs.getInt("company_id"));
                applicant.setComment(rs.getString("comment"));
                applicant.setAdditionalFile(rs.getString("additional_file"));
                applicant.setAppliedDate(rs.getTimestamp("applied_date"));
                applicant.setStatus(rs.getString("status"));  // Get the status field
                applications.add(applicant);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving applications by applicant ID: " + e.getMessage());
        }

        return applications;
    }
}
