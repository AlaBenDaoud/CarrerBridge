package org.example.demo.jobboardapp.Services;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.example.demo.jobboardapp.Utils.DatabaseService;

public class ApplicantCreation {

    /**
     * SQL statement to create the 'applicants' table if it doesn't exist.
     */
    private static final String CREATE_APPLICANTS_TABLE_SQL = "CREATE TABLE IF NOT EXISTS applicants ("
            + "id INT AUTO_INCREMENT PRIMARY KEY, "
            + "job_id INT NOT NULL, "
            + "company_id INT NOT NULL, " // Added company_id column (derived from jobs table)
            + "name VARCHAR(255) NOT NULL, "
            + "email VARCHAR(255) NOT NULL, "
            + "resume TEXT NOT NULL, "
            + "applied_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE" // Foreign key to jobs table
            + ")";

    /**
     * Initializes the database by creating the 'applicants' table if it doesn't exist.
     */
    public static void initializeApplicantsTable() {
        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement()) {
            // Execute the SQL statement to create the table
            stmt.execute(CREATE_APPLICANTS_TABLE_SQL);
            System.out.println("Table 'applicants' created or already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}