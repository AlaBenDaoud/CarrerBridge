package org.example.demo.jobboardapp.Services;

import org.example.demo.jobboardapp.Models.Applicant;
import org.example.demo.jobboardapp.Models.Job;
import org.example.demo.jobboardapp.Utils.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicantService {

    private final JobService jobService = new JobService();

    /**
     * Adds a new applicant to the database.
     *
     * @param applicant The applicant to add.
     */
    public void addApplicant(Applicant applicant) {
        // Fetch the job to get the companyId
        Job job = jobService.getJobById(applicant.getJobId());
        if (job == null) {
            System.err.println("Job not found for ID: " + applicant.getJobId());
            return;
        }

        // Set the companyId from the job
        applicant.setCompanyId(job.getCompanyId());

        String query = "INSERT INTO applicants (job_id, company_id, name, email, resume, applied_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, applicant.getJobId());
            stmt.setInt(2, applicant.getCompanyId()); // Include company_id in the query
            stmt.setString(3, applicant.getName());
            stmt.setString(4, applicant.getEmail());
            stmt.setString(5, applicant.getResume());
            stmt.setTimestamp(6, applicant.getAppliedDate());

            stmt.executeUpdate();
            System.out.println("Applicant added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
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

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Applicant applicant = new Applicant();
                applicant.setId(rs.getInt("id"));
                applicant.setJobId(rs.getInt("job_id"));
                applicant.setCompanyId(rs.getInt("company_id"));
                applicant.setName(rs.getString("name"));
                applicant.setEmail(rs.getString("email"));
                applicant.setResume(rs.getString("resume"));
                applicant.setAppliedDate(rs.getTimestamp("applied_date"));
                applicants.add(applicant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return applicants;
    }

    public Applicant getApplicantById(int id) {
        String query = "SELECT * FROM applicants WHERE id = ?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Applicant applicant = new Applicant();
                applicant.setId(rs.getInt("id"));
                applicant.setJobId(rs.getInt("job_id"));
                applicant.setCompanyId(rs.getInt("company_id"));
                applicant.setName(rs.getString("name"));
                applicant.setEmail(rs.getString("email"));
                applicant.setResume(rs.getString("resume"));
                applicant.setAppliedDate(rs.getTimestamp("applied_date"));
                return applicant;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}