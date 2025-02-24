package org.example.auth.controllers.JobAndApplicantion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.example.auth.models.Job;
import org.example.auth.models.User;
import org.example.auth.services.JobService;
import org.example.auth.services.UserService;
import org.example.auth.controllers.connexion.AuthUserController;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BestMatchPopupController {

    @FXML
    private VBox jobsContainer;

    private final JobService jobService = new JobService();
    private final UserService userService = new UserService();

    // Suppress PDFBox font warnings
    static {
        java.util.logging.Logger.getLogger("org.apache.pdfbox.pdmodel.font.PDSimpleFont")
                .setLevel(java.util.logging.Level.SEVERE);
    }

    /**
     * Loads all best-matched job details into the pop-up based on the user's CV.
     */
    public void loadBestMatchJobs() {
        // Get the logged-in user's ID
        int userId = AuthUserController.getLoggedInUserId();

        // Retrieve the user's information (including CV path)
        User user = userService.getUserById(userId);

        if (user == null || user.getCv() == null || user.getCv().isEmpty()) {
            System.err.println("Error: No CV path found for the user.");
            displayMessage("No CV Found");
            return;
        }

        // Extract keywords from the user's CV
        String cvPath = user.getCv();
        System.out.println("CV Path: " + cvPath);

        String cvContent = extractTextFromPdf(cvPath);

        if (cvContent == null) {
            System.err.println("Error: Failed to extract text from CV at path: " + cvPath);
            displayMessage("Unable to read CV");
            return;
        }

        // Extract relevant keywords from the CV
        List<String> cvKeywords = extractKeywordsFromCV(cvContent);

        if (cvKeywords.isEmpty()) {
            System.out.println("No relevant keywords found in the CV.");
            displayMessage("No Relevant Jobs Found");
            return;
        }

        // Find the best-matched jobs by comparing keywords with job titles and descriptions
        List<Job> bestMatchJobs = jobService.getBestMatchJobsForKeywords(cvKeywords);

        if (bestMatchJobs != null && !bestMatchJobs.isEmpty()) {
            // Populate the UI with the list of best-matched jobs
            for (Job job : bestMatchJobs) {
                displayJob(job);
            }
        } else {
            // Handle the case where no best-matched jobs are found
            System.out.println("No matching jobs found for the user's CV.");
            displayMessage("No Jobs Found");
        }
    }

    /**
     * Extracts text from a given PDF file.
     *
     * @param filePath The path to the PDF file.
     * @return The extracted text, or null if extraction fails.
     */
    private String extractTextFromPdf(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.err.println("Error: The file does not exist or is not a valid file: " + filePath);
            return null;
        }

        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (IOException e) {
            System.err.println("Error reading CV PDF: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extracts relevant keywords from the CV content.
     *
     * @param cvContent The text extracted from the CV.
     * @return A list of relevant keywords.
     */
    private List<String> extractKeywordsFromCV(String cvContent) {
        // Define keywords related to development roles
        return List.of(
                "full stack", "developer", "javascript", "typescript", "node.js",
                "react", "tailwind css", "mongodb", "firebase", "supabase",
                "web development", "frontend", "backend"
        );
    }

    /**
     * Displays a single job in the UI.
     *
     * @param job The job to display.
     */
    private void displayJob(Job job) {
        VBox jobBox = new VBox(5);
        jobBox.setStyle("-fx-padding: 10; -fx-background-color: #475569; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label titleLabel = new Label(job.getTitle());
        titleLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");

        Label positionLabel = new Label(job.getPosition());
        positionLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        Label locationLabel = new Label(job.getLocation());
        locationLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        Label postedDateLabel = new Label("Posted: " + job.getPostedDate().toString());
        postedDateLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #94a3b8;");

        // View Details Button
        Button viewButton = new Button("View Details");
        viewButton.setStyle("-fx-text-fill: white; -fx-background-color: #3b82f6; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");
        viewButton.setOnAction(event -> openJobDetails(job.getId(), event));

        jobBox.getChildren().addAll(titleLabel, positionLabel, locationLabel, postedDateLabel, viewButton);
        jobsContainer.getChildren().add(jobBox);
    }

    /**
     * Opens the job details view for the selected job.
     *
     * @param jobId  The ID of the job to view.
     * @param event  The action event.
     */
    private void openJobDetails(int jobId, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/job_detail.fxml"));
            Parent root = loader.load();

            // Pass job ID to the JobDetailController
            JobDetailController controller = loader.getController();
            controller.loadJobDetails(jobId);

            // Load new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to open job details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays a message in the UI when no jobs or CV is found.
     *
     * @param message The message to display.
     */
    private void displayMessage(String message) {
        jobsContainer.getChildren().clear();
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 16px; -fx-text-fill: #f8fafc;");
        jobsContainer.getChildren().add(messageLabel);
    }
}