package org.example.auth.services;

import org.example.auth.utils.DatabaseService;
import org.example.auth.models.LeaveRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaveRequestService {
    private DatabaseService databaseService = new DatabaseService();

    // Constructor
    public LeaveRequestService() {
        // No need to create the table here
    }

    // Add a new leave request
    public void addLeaveRequest(LeaveRequest leaveRequest) {
        String query = "INSERT INTO leave_requests (employee_id, company_id, start_date, end_date, description, leave_type, pdf_path) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, leaveRequest.getEmployeeId());
            stmt.setInt(2, leaveRequest.getCompanyId());
            stmt.setDate(3, Date.valueOf(leaveRequest.getStartDate()));
            stmt.setDate(4, Date.valueOf(leaveRequest.getEndDate()));
            stmt.setString(5, leaveRequest.getDescription());
            stmt.setString(6, leaveRequest.getLeaveType());
            stmt.setString(7, leaveRequest.getPdfPath());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch all leave requests from the database
    public List<LeaveRequest> getAllLeaveRequests() {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        String query = "SELECT * FROM leave_requests";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                LeaveRequest leaveRequest = new LeaveRequest();
                leaveRequest.setId(rs.getInt("id"));
                leaveRequest.setEmployeeId(rs.getInt("employee_id"));
                leaveRequest.setCompanyId(rs.getInt("company_id"));
                leaveRequest.setStartDate(rs.getDate("start_date").toLocalDate());
                leaveRequest.setEndDate(rs.getDate("end_date").toLocalDate());
                leaveRequest.setDescription(rs.getString("description"));
                leaveRequest.setLeaveType(rs.getString("leave_type"));
                leaveRequest.setPdfPath(rs.getString("pdf_path"));
                leaveRequest.setConfirmed(rs.getBoolean("is_confirmed"));
                leaveRequests.add(leaveRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaveRequests;
    }

    // Fetch leave requests for a specific employee ID
    public List<LeaveRequest> getLeaveRequestsByEmployeeId(int employeeId) {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        String query = "SELECT * FROM leave_requests WHERE employee_id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LeaveRequest leaveRequest = new LeaveRequest();
                    leaveRequest.setId(rs.getInt("id"));
                    leaveRequest.setEmployeeId(rs.getInt("employee_id"));
                    leaveRequest.setCompanyId(rs.getInt("company_id"));
                    leaveRequest.setStartDate(rs.getDate("start_date").toLocalDate());
                    leaveRequest.setEndDate(rs.getDate("end_date").toLocalDate());
                    leaveRequest.setDescription(rs.getString("description"));
                    leaveRequest.setLeaveType(rs.getString("leave_type"));
                    leaveRequest.setPdfPath(rs.getString("pdf_path"));
                    leaveRequest.setConfirmed(rs.getBoolean("is_confirmed"));
                    leaveRequests.add(leaveRequest);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaveRequests;
    }

    // Update a leave request
    public void updateLeaveRequest(LeaveRequest request) {
        String query = "UPDATE leave_requests SET start_date = ?, end_date = ?, description = ?, leave_type = ?, pdf_path = ?, is_confirmed = ?, company_id = ? WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(request.getStartDate()));
            stmt.setDate(2, Date.valueOf(request.getEndDate()));
            stmt.setString(3, request.getDescription());
            stmt.setString(4, request.getLeaveType());
            stmt.setString(5, request.getPdfPath());
            stmt.setBoolean(6, request.isConfirmed());
            stmt.setInt(7, request.getCompanyId());
            stmt.setInt(8, request.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a leave request
    public void deleteLeaveRequest(int id) {
        String query = "DELETE FROM leave_requests WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}