package org.example.auth.services;

import org.example.auth.utils.DatabaseService;
import org.example.auth.models.OnlineJob;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OnlineJobService {

    private static final Logger logger = Logger.getLogger(OnlineJobService.class.getName());
    private final DatabaseService databaseService = new DatabaseService();

    public OnlineJobService() {
        // No need to create the table here
    }

    public void addOnlineJob(OnlineJob onlineJob) {
        String query = "INSERT INTO online_jobs (leave_request_id, title, post, start_date, end_date, is_confirmed) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, onlineJob.getLeaveRequestId());
            stmt.setString(2, onlineJob.getTitle());
            stmt.setString(3, onlineJob.getPost());
            stmt.setDate(4, Date.valueOf(onlineJob.getStartDate()));
            stmt.setDate(5, Date.valueOf(onlineJob.getEndDate()));
            stmt.setBoolean(6, onlineJob.isConfirmed());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to add online job", e);
        }
    }

    public List<OnlineJob> getAllOnlineJobs() {
        List<OnlineJob> onlineJobs = new ArrayList<>();
        String query = "SELECT * FROM online_jobs";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                OnlineJob onlineJob = new OnlineJob();
                onlineJob.setId(rs.getInt("id"));
                onlineJob.setLeaveRequestId(rs.getInt("leave_request_id"));
                onlineJob.setTitle(rs.getString("title"));
                onlineJob.setPost(rs.getString("post"));
                onlineJob.setStartDate(rs.getDate("start_date").toLocalDate());
                onlineJob.setEndDate(rs.getDate("end_date").toLocalDate());
                onlineJob.setConfirmed(rs.getBoolean("is_confirmed"));
                onlineJobs.add(onlineJob);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to fetch online jobs", e);
        }
        return onlineJobs;
    }

    public OnlineJob getOnlineJobById(int id) {
        String query = "SELECT * FROM online_jobs WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OnlineJob onlineJob = new OnlineJob();
                    onlineJob.setId(rs.getInt("id"));
                    onlineJob.setLeaveRequestId(rs.getInt("leave_request_id"));
                    onlineJob.setTitle(rs.getString("title"));
                    onlineJob.setPost(rs.getString("post"));
                    onlineJob.setStartDate(rs.getDate("start_date").toLocalDate());
                    onlineJob.setEndDate(rs.getDate("end_date").toLocalDate());
                    onlineJob.setConfirmed(rs.getBoolean("is_confirmed"));
                    return onlineJob;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to fetch online job by id", e);
        }
        return null;
    }

    public void updateOnlineJob(OnlineJob onlineJob) {
        String query = "UPDATE online_jobs SET title = ?, post = ?, start_date = ?, end_date = ?, is_confirmed = ? WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, onlineJob.getTitle());
            stmt.setString(2, onlineJob.getPost());
            stmt.setDate(3, Date.valueOf(onlineJob.getStartDate()));
            stmt.setDate(4, Date.valueOf(onlineJob.getEndDate()));
            stmt.setBoolean(5, onlineJob.isConfirmed());
            stmt.setInt(6, onlineJob.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to update online job", e);
        }
    }

    public void deleteOnlineJob(int id) {
        String query = "DELETE FROM online_jobs WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to delete online job", e);
        }
    }

    public OnlineJob getOnlineJobByLeaveRequestId(int leaveRequestId) {
        String query = "SELECT * FROM online_jobs WHERE leave_request_id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, leaveRequestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OnlineJob onlineJob = new OnlineJob();
                    onlineJob.setId(rs.getInt("id"));
                    onlineJob.setLeaveRequestId(rs.getInt("leave_request_id"));
                    onlineJob.setTitle(rs.getString("title"));
                    onlineJob.setPost(rs.getString("post"));
                    onlineJob.setStartDate(rs.getDate("start_date").toLocalDate());
                    onlineJob.setEndDate(rs.getDate("end_date").toLocalDate());
                    onlineJob.setConfirmed(rs.getBoolean("is_confirmed"));
                    return onlineJob;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to fetch online job by leave request ID", e);
        }
        return null;
    }
}