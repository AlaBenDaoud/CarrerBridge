package org.example.auth.services;

import org.example.auth.models.Job;
import org.example.auth.utils.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JobService {

    private DatabaseService databaseService;

    public JobService() {
        this.databaseService = new DatabaseService();
    }

    /**
     * Adds a new job to the database.
     *
     * @param job The job to add.
     * @return True if the job was added successfully, false otherwise.
     */
    public boolean addJob(Job job) {
        String query = "INSERT INTO jobs (title, description, company_id, position, location) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, job.getTitle());
            stmt.setString(2, job.getDescription());
            stmt.setInt(3, job.getCompanyId());
            stmt.setString(4, job.getPosition());
            stmt.setString(5, job.getLocation());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding job: " + e.getMessage());
            return false;
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

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Job job = new Job();
                job.setId(rs.getInt("id"));
                job.setTitle(rs.getString("title"));
                job.setDescription(rs.getString("description"));
                job.setCompanyId(rs.getInt("company_id"));
                job.setPosition(rs.getString("position"));
                job.setLocation(rs.getString("location"));
                job.setPostedDate(rs.getTimestamp("posted_date"));
                jobs.add(job);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving jobs: " + e.getMessage());
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

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                job = new Job();
                job.setId(rs.getInt("id"));
                job.setTitle(rs.getString("title"));
                job.setDescription(rs.getString("description"));
                job.setCompanyId(rs.getInt("company_id"));
                job.setPosition(rs.getString("position"));
                job.setLocation(rs.getString("location"));
                job.setPostedDate(rs.getTimestamp("posted_date"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving job by ID: " + e.getMessage());
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
        String query = "UPDATE jobs SET title = ?, description = ?, company_id = ?, position = ?, location = ? WHERE id = ?";

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, job.getTitle());
            stmt.setString(2, job.getDescription());
            stmt.setInt(3, job.getCompanyId());
            stmt.setString(4, job.getPosition());
            stmt.setString(5, job.getLocation());
            stmt.setInt(6, job.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating job: " + e.getMessage());
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

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting job: " + e.getMessage());
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

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Job job = new Job();
                job.setId(rs.getInt("id"));
                job.setTitle(rs.getString("title"));
                job.setDescription(rs.getString("description"));
                job.setCompanyId(rs.getInt("company_id"));
                job.setPosition(rs.getString("position"));
                job.setLocation(rs.getString("location"));
                job.setPostedDate(rs.getTimestamp("posted_date"));
                jobs.add(job);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving jobs by company ID: " + e.getMessage());
        }

        return jobs;
    }


    /**
     * Retrieves the best-matched jobs based on a list of keywords from the user's CV.
     *
     * @param keywords A list of keywords extracted from the CV.
     * @return A list of best-matched jobs, or an empty list if no matches are found.
     */
    public List<Job> getBestMatchJobsForKeywords(List<String> keywords) {
        List<Job> bestMatchJobs = new ArrayList<>();

        if (keywords.isEmpty()) {
            return bestMatchJobs; // Return an empty list if no keywords are provided
        }

        // Build the SQL query dynamically
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM (");
        query.append("SELECT *, ");
        query.append("(");

        for (String keyword : keywords) {
            query.append("CASE WHEN title LIKE '%").append(keyword).append("%' THEN 1 ELSE 0 END + ");
            query.append("CASE WHEN description LIKE '%").append(keyword).append("%' THEN 1 ELSE 0 END + ");
        }

        query.setLength(query.length() - 3); // Remove the trailing " + "
        query.append(") AS relevance_score ");
        query.append("FROM jobs");
        query.append(") AS scored_jobs ");
        query.append("WHERE relevance_score > 0 "); // Filter out jobs with no relevance
        query.append("ORDER BY relevance_score DESC, posted_date DESC");

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Job job = new Job();
                job.setId(rs.getInt("id"));
                job.setTitle(rs.getString("title"));
                job.setDescription(rs.getString("description"));
                job.setCompanyId(rs.getInt("company_id"));
                job.setPosition(rs.getString("position"));
                job.setLocation(rs.getString("location"));
                job.setPostedDate(rs.getTimestamp("posted_date"));
                bestMatchJobs.add(job);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving best-matched jobs: " + e.getMessage());
        }

        return bestMatchJobs;
    }
}