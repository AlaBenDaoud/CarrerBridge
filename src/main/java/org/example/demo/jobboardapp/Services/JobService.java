package org.example.demo.jobboardapp.Services;

import org.example.demo.jobboardapp.Models.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.demo.jobboardapp.Utils.DatabaseService;

public class JobService {

    /**
     * Adds a new job to the database.
     *
     * @param job The job to add.
     */
    public void addJob(Job job) {
        String query = "INSERT INTO jobs (title, description, company_id, company_name, location) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, job.getTitle());
            stmt.setString(2, job.getDescription());
            stmt.setInt(3, job.getCompanyId()); // Set company_id
            stmt.setString(4, job.getCompanyName()); // Set company_name
            stmt.setString(5, job.getLocation());
            stmt.executeUpdate();
            System.out.println("Job added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all jobs from the database.
     *
     * @return A list of all jobs.
     */
    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        String query = "SELECT * FROM jobs";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Job job = new Job();
                job.setId(rs.getInt("id"));
                job.setTitle(rs.getString("title"));
                job.setDescription(rs.getString("description"));
                job.setCompanyId(rs.getInt("company_id")); // Fetch company_id
                job.setCompanyName(rs.getString("company_name")); // Fetch company_name
                job.setLocation(rs.getString("location"));
                job.setPostedDate(rs.getTimestamp("posted_date")); // Fetch posted_date
                jobs.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobs;
    }

    /**
     * Retrieves a job by its ID.
     *
     * @param id The ID of the job to retrieve.
     * @return The job with the specified ID, or null if not found.
     */
    public Job getJobById(int id) {
        Job job = null;
        String query = "SELECT * FROM jobs WHERE id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                job = new Job();
                job.setId(rs.getInt("id"));
                job.setTitle(rs.getString("title"));
                job.setDescription(rs.getString("description"));
                job.setCompanyId(rs.getInt("company_id")); // Fetch company_id
                job.setCompanyName(rs.getString("company_name")); // Fetch company_name
                job.setLocation(rs.getString("location"));
                job.setPostedDate(rs.getTimestamp("posted_date")); // Fetch posted_date
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return job;
    }

    /**
     * Updates an existing job in the database.
     *
     * @param job The job to update.
     * @return True if the job was updated successfully, false otherwise.
     */
    public boolean updateJob(Job job) {
        String query = "UPDATE jobs SET title = ?, description = ?, company_id = ?, company_name = ?, location = ? WHERE id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, job.getTitle());
            stmt.setString(2, job.getDescription());
            stmt.setInt(3, job.getCompanyId()); // Set company_id
            stmt.setString(4, job.getCompanyName()); // Set company_name
            stmt.setString(5, job.getLocation());
            stmt.setInt(6, job.getId()); // Set id for WHERE clause

            int rowsAffected = stmt.executeUpdate();

            // Return true if at least one row was updated
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a job by its ID.
     *
     * @param id The ID of the job to delete.
     * @return True if the job was deleted successfully, false otherwise.
     */
    public boolean deleteJob(int id) {
        String query = "DELETE FROM jobs WHERE id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            // Return true if at least one row was deleted
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all jobs associated with a specific company ID.
     *
     * @param companyId The ID of the company.
     * @return A list of jobs associated with the company.
     */
    public List<Job> getJobsByCompanyId(int companyId) {
        List<Job> jobs = new ArrayList<>();
        String query = "SELECT * FROM jobs WHERE company_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Job job = new Job();
                job.setId(rs.getInt("id"));
                job.setTitle(rs.getString("title"));
                job.setDescription(rs.getString("description"));
                job.setCompanyId(rs.getInt("company_id")); // Fetch company_id
                job.setCompanyName(rs.getString("company_name")); // Fetch company_name
                job.setLocation(rs.getString("location"));
                job.setPostedDate(rs.getTimestamp("posted_date")); // Fetch posted_date
                jobs.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobs;
    }
}