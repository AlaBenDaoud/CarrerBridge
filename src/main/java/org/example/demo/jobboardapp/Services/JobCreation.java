package org.example.demo.jobboardapp.Services;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.example.demo.jobboardapp.Utils.DatabaseService;

public class JobCreation {

    private static final String CREATE_JOBS_TABLE_SQL = "CREATE TABLE IF NOT EXISTS jobs ("
            + "id INT AUTO_INCREMENT PRIMARY KEY, "
            + "title VARCHAR(255) NOT NULL, "
            + "description TEXT NOT NULL, "
            + "company_id INT NOT NULL, "  // Company ID (manually provided)
            + "company_name VARCHAR(255) NOT NULL, "  // Company name (manually provided)
            + "location VARCHAR(255) NOT NULL, "
            + "posted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            + ")";

    /**
     * Initializes the database by creating the 'jobs' table if it doesn't exist.
     */
    public static void initializeTables() {
        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement()) {

            // Create the 'jobs' table
            stmt.execute(CREATE_JOBS_TABLE_SQL);
            System.out.println("Table 'jobs' created or already exists.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}