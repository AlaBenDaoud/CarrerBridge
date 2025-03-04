package org.example.auth.services;

import org.example.auth.models.Company;
import org.example.auth.utils.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyService {


    private DatabaseService databaseService = new DatabaseService();

    public CompanyService() {
        this.databaseService = new DatabaseService();
    }

    // Save a company to the database
    public boolean registerCompany(Company company) {
        String sql = "INSERT INTO company (company_name, location, secteur, email, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL query
            statement.setString(1, company.getCompanyName());
            statement.setString(2, company.getLocation());
            statement.setString(3, company.getSecteur());
            statement.setString(4, company.getEmail());
            statement.setString(5, company.getPassword());

            // Execute the query
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if insertion was successful

        } catch (SQLException e) {
            System.err.println("Error registering company: " + e.getMessage());
            return false; // If an exception occurs, return false
        }
    }

    // Retrieve all companies from the database
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM company";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Map each row to a Company object
                Company company = new Company(
                        resultSet.getInt("id"),
                        resultSet.getString("company_name"),
                        resultSet.getString("location"),
                        resultSet.getString("secteur"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
                companies.add(company);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving companies: " + e.getMessage());
        }

        return companies;
    }

    // Retrieve a company by email
    public Company getCompanyByEmail(String email) {
        String sql = "SELECT * FROM company WHERE email = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Map the row to a Company object
                    return new Company(
                            resultSet.getInt("id"),
                            resultSet.getString("company_name"),
                            resultSet.getString("location"),
                            resultSet.getString("secteur"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving company by email: " + e.getMessage());
        }

        return null; // Return null if no company is found
    }

    // Update a company in the database
    public boolean updateCompany(int id, Company updatedCompany) {
        String sql = "UPDATE company SET company_name = ?, location = ?, secteur = ?, email = ?, password = ? WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL query
            statement.setString(1, updatedCompany.getCompanyName());
            statement.setString(2, updatedCompany.getLocation());
            statement.setString(3, updatedCompany.getSecteur());
            statement.setString(4, updatedCompany.getEmail());
            statement.setString(5, updatedCompany.getPassword());
            statement.setInt(6, id);

            // Execute the query
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if update was successful

        } catch (SQLException e) {
            System.err.println("Error updating company: " + e.getMessage());
            return false;
        }
    }

    // Delete a company by ID
    public boolean deleteCompany(int id) {
        String sql = "DELETE FROM company WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if deletion was successful

        } catch (SQLException e) {
            System.err.println("Error deleting company: " + e.getMessage());
            return false;
        }
    }
    public int getTotalCompaniesCount() {
        String sql = "SELECT COUNT(*) FROM company";  // Requête pour compter toutes les entreprises
        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);  // Retourne le nombre total d'entreprises
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nombre d'entreprises : " + e.getMessage());
        }
        return 0;  // Retourne 0 si une erreur se produit
    }
}