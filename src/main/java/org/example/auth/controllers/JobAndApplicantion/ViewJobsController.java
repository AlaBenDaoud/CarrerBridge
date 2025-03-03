package org.example.auth.controllers.JobAndApplicantion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.auth.models.Job;
import org.example.auth.models.User;
import org.example.auth.services.JobService;
import org.example.auth.services.UserService;
import org.example.auth.utils.AlertUtils;
import org.example.auth.controllers.connexion.AuthUserController;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewJobsController {

    @FXML
    private VBox jobsContainer;

    @FXML
    private Button backButton;

    @FXML
    private Label jobCountLabel;

    @FXML
    private Label matchRateLabel;

    @FXML
    private TextField searchField;

    private final JobService jobService = new JobService();
    private final UserService userService = new UserService();
    private Map<Integer, Integer> jobMatchPercentages = new HashMap<>();
    private List<String> cvKeywords = new ArrayList<>();
    private List<Job> allJobs = new ArrayList<>(); // Store all jobs for filtering

    // Suppress PDFBox font warnings
    static {
        java.util.logging.Logger.getLogger("org.apache.pdfbox.pdmodel.font.PDSimpleFont")
                .setLevel(java.util.logging.Level.SEVERE);
    }

    @FXML
    public void initialize() {
        extractCVKeywords();
        loadJobs(); // Load jobs into the card layout

        // Set up search field listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterJobs(newValue);
        });
    }

    @FXML
    private Button bestMatchButton;

    /**
     * Extracts keywords from the user's CV for matching.
     */
    private void extractCVKeywords() {
        try {
            // Get the logged-in user's ID
            int userId = AuthUserController.getLoggedInUserId();

            // Retrieve the user's information (including CV path)
            User user = userService.getUserById(userId);

            if (user != null && user.getCv() != null && !user.getCv().isEmpty()) {
                // Extract keywords from the user's CV
                String cvPath = user.getCv();
                String cvContent = extractTextFromPdf(cvPath);

                if (cvContent != null) {
                    // Extract relevant keywords from the CV
                    cvKeywords = extractKeywordsFromCV(cvContent);
                }
            }
        } catch (Exception e) {
            System.err.println("Error extracting CV keywords: " + e.getMessage());
            e.printStackTrace();
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
     * Calculates match percentage between a job and the user's CV keywords.
     *
     * @param job The job to evaluate.
     * @return The match percentage (0-100).
     */
    private int calculateMatchPercentage(Job job) {
        if (cvKeywords.isEmpty()) {
            return 0;
        }

        int matches = 0;
        String jobText = (job.getTitle() + " " + job.getDescription() + " " + job.getPosition()).toLowerCase();

        for (String keyword : cvKeywords) {
            if (jobText.contains(keyword.toLowerCase())) {
                matches++;
            }
        }

        return (int) Math.round((double) matches / cvKeywords.size() * 100);
    }

    /**
     * Loads jobs from the database and displays them as cards.
     */
    private void loadJobs() {
        try {
            allJobs = jobService.getAllJobs(); // Fetch jobs from the database

            // Set job count label
            jobCountLabel.setText(String.valueOf(allJobs.size()));

            // Calculate match percentages and average match rate
            displayJobs(allJobs);

        } catch (Exception e) {
            AlertUtils.showError("Failed to load jobs. Please try again.");
            e.printStackTrace();
        }
    }

    /**
     * Displays the provided list of jobs.
     *
     * @param jobs The list of jobs to display
     */
    private void displayJobs(List<Job> jobs) {
        jobsContainer.getChildren().clear();

        int totalMatchPercentage = 0;

        for (Job job : jobs) {
            int matchPercentage = calculateMatchPercentage(job);
            jobMatchPercentages.put(job.getId(), matchPercentage);
            totalMatchPercentage += matchPercentage;

            jobsContainer.getChildren().add(createJobCard(job, matchPercentage));
        }

        // Update job count for filtered view
        jobCountLabel.setText(String.valueOf(jobs.size()));

        // Update match rate label with average
        int averageMatchRate = jobs.isEmpty() ? 0 : totalMatchPercentage / jobs.size();
        matchRateLabel.setText(averageMatchRate + "%");
    }

    /**
     * Filters jobs based on search text.
     *
     * @param searchText The text to filter jobs by
     */
    private void filterJobs(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            displayJobs(allJobs);
            return;
        }

        String searchLower = searchText.toLowerCase().trim();

        List<Job> filteredJobs = allJobs.stream()
                .filter(job ->
                        job.getTitle().toLowerCase().contains(searchLower) ||
                                job.getDescription().toLowerCase().contains(searchLower) ||
                                job.getPosition().toLowerCase().contains(searchLower) ||
                                job.getLocation().toLowerCase().contains(searchLower))
                .collect(Collectors.toList());

        displayJobs(filteredJobs);
    }

    /**
     * Creates a job card for the given job.
     *
     * @param job The job to display.
     * @param matchPercentage The match percentage for this job.
     * @return A VBox representing the job card.
     */
    private VBox createJobCard(Job job, int matchPercentage) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setStyle("-fx-padding: 15; -fx-background-color: rgba(30, 41, 59, 0.7); -fx-border-radius: 10; -fx-border-color: rgba(59, 130, 246, 0.2); -fx-background-radius: 10;");
        card.setPrefWidth(760);

        // Job Title with Match Percentage
        HBox titleBox = new HBox();
        titleBox.setSpacing(10);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label titleLabel = new Label(job.getTitle());
        titleLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");

        Label matchLabel = new Label(matchPercentage + "% Match");
        matchLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-padding: 4 8; -fx-background-radius: 4; -fx-background-color: " + getMatchColorStyle(matchPercentage) + "; -fx-text-fill: white;");

        titleBox.getChildren().addAll(titleLabel, matchLabel);

        // Job Position
        Label positionLabel = new Label("Position: " + job.getPosition());
        positionLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Job Location
        Label locationLabel = new Label("Location: " + job.getLocation());
        locationLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Posted Date
        Label postedDateLabel = new Label("Posted on: " + job.getPostedDate());
        postedDateLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #94a3b8;");

        // View Details Button
        Button viewButton = new Button("View Details");
        viewButton.setStyle("-fx-text-fill: white; -fx-background-color: #3b82f6; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");
        viewButton.setOnAction(event -> openJobDetails(job.getId(), event));

        // Add all elements to the card
        card.getChildren().addAll(titleBox, positionLabel, locationLabel, postedDateLabel, viewButton);
        return card;
    }

    /**
     * Gets an appropriate color for the match percentage.
     *
     * @param matchPercentage The match percentage.
     * @return CSS color string.
     */
    private String getMatchColorStyle(int matchPercentage) {
        if (matchPercentage >= 75) {
            return "#10b981"; // Green for high match
        } else if (matchPercentage >= 50) {
            return "#f59e0b"; // Orange for medium match
        } else {
            return "#6b7280"; // Gray for low match
        }
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
            AlertUtils.showError("Failed to open job details. Please try again.");
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Back" button click event.
     *
     * @param event The action event.
     */
    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/userdash.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            AlertUtils.showError("Failed to navigate back. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBestMatchButton(ActionEvent event) {
        try {
            // Load the pop-up FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/best_match_popup.fxml"));
            Parent root = loader.load();

            // Load the best match job details
            BestMatchPopupController controller = loader.getController();
            controller.loadBestMatchJobs();

            // Create a new stage for the pop-up
            Stage popupStage = new Stage();
            popupStage.setTitle("Best Match Job");
            popupStage.setScene(new Scene(root, 400, 300));
            popupStage.show();
        } catch (IOException e) {
            AlertUtils.showError("Failed to open best match pop-up. Please try again.");
            e.printStackTrace();
        }
    }
}