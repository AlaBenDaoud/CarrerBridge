package org.example.auth.services;

import org.example.auth.models.Employee;
import org.example.auth.utils.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {

    private DatabaseService databaseService;

    public EmployeeService() {
        this.databaseService = new DatabaseService();
    }

    // Method to check if a user is an employee
    public boolean isUserEmployee(int userId) {
        String sql = "SELECT * FROM employees WHERE user_id = ?";
        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Returns true if the user ID exists in the employees table
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to add a new employee to the database
    public boolean addEmployee(Employee employee) {
        String insertEmployeeQuery = "INSERT INTO employees (company_id, user_id, job_id) VALUES (?, ?, ?)";

        try (Connection connection = databaseService.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(insertEmployeeQuery);
            statement.setInt(1, employee.getCompanyId());
            statement.setInt(2, employee.getUserId());
            statement.setInt(3, employee.getJobId()); // Insert jobId
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get all employees for a given company ID
    public List<Employee> getEmployeesByCompanyId(int companyId) {
        String sql = "SELECT * FROM employees WHERE company_id = ?";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, companyId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getInt("company_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("job_id") // Retrieve jobId
                );
                employees.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    // Method to delete an employee and their application by employee ID
    public boolean deleteEmployeeAndApplication(int employeeId) {
        String deleteEmployeeQuery = "DELETE FROM employees WHERE id = ?";
        String deleteApplicationQuery = "DELETE FROM applicants WHERE user_id = ? AND job_id = ?";

        try (Connection connection = databaseService.getConnection()) {
            // Start a transaction
            connection.setAutoCommit(false);

            // Retrieve the employee details to get user_id and job_id
            Employee employee = getEmployeeById(employeeId);
            if (employee == null) {
                return false; // Employee not found
            }

            // Delete the application associated with the employee's user_id and job_id
            PreparedStatement deleteApplicationStmt = connection.prepareStatement(deleteApplicationQuery);
            deleteApplicationStmt.setInt(1, employee.getUserId());
            deleteApplicationStmt.setInt(2, employee.getJobId());
            int applicationRowsAffected = deleteApplicationStmt.executeUpdate();

            // Delete the employee
            PreparedStatement deleteEmployeeStmt = connection.prepareStatement(deleteEmployeeQuery);
            deleteEmployeeStmt.setInt(1, employeeId);
            int employeeRowsAffected = deleteEmployeeStmt.executeUpdate();

            // Commit the transaction if both deletions were successful
            if (applicationRowsAffected > 0 && employeeRowsAffected > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Employee getEmployeeById(int employeeId) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, employeeId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Employee(
                        resultSet.getInt("id"),
                        resultSet.getInt("company_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("job_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no employee found with the given ID
    }

    // Method to get all employees for a given user ID
    public List<Employee> getEmployeesByUserId(int userId) {
        String sql = "SELECT * FROM employees WHERE user_id = ?";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getInt("company_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("job_id")
                );
                employees.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

}