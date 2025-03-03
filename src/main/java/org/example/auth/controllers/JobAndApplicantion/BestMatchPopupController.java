package org.example.auth.controllers.JobAndApplicantion;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.example.auth.models.Job;
import org.example.auth.models.User;
import org.example.auth.services.JobService;
import org.example.auth.services.UserService;
import org.example.auth.controllers.connexion.AuthUserController;
import javafx.scene.layout.HBox;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BestMatchPopupController {

    @FXML private VBox jobsContainer;
    @FXML private Label resultCountLabel;
    @FXML private StackPane loadingIndicator;
    @FXML private VBox errorContainer;
    @FXML private Label errorMessageLabel;

    private final JobService jobService = new JobService();
    private final UserService userService = new UserService();
    private static final Logger logger = Logger.getLogger(BestMatchPopupController.class.getName());

    // Suppress PDFBox font warnings
    static {
        Logger.getLogger("org.apache.pdfbox.pdmodel.font.PDSimpleFont")
                .setLevel(Level.SEVERE);
    }

    @FXML
    public void initialize() {
        // Initial setup
        loadBestMatchJobs();
    }

    public void loadBestMatchJobs() {
        showLoading(true);
        clearContent();

        CompletableFuture.runAsync(() -> {
            try {
                int userId = AuthUserController.getLoggedInUserId();
                User user = userService.getUserById(userId);

                if (user == null || user.getCv() == null || user.getCv().isEmpty()) {
                    Platform.runLater(() -> showError("No CV found. Please upload your CV first."));
                    return;
                }

                String cvContent = extractTextFromPdf(user.getCv());
                if (cvContent == null) {
                    Platform.runLater(() -> showError("Unable to read CV. Please ensure it's a valid PDF."));
                    return;
                }

                List<String> cvKeywords = extractKeywordsFromCV(cvContent);
                if (cvKeywords.isEmpty()) {
                    Platform.runLater(() -> showError("No relevant keywords found in your CV."));
                    return;
                }

                List<Job> bestMatchJobs = jobService.getBestMatchJobsForKeywords(cvKeywords);

                Platform.runLater(() -> {
                    if (bestMatchJobs != null && !bestMatchJobs.isEmpty()) {
                        updateResultCount(bestMatchJobs.size());
                        bestMatchJobs.forEach(this::displayJob);
                    } else {
                        showError("No matching jobs found for your profile.");
                    }
                });

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error loading best match jobs", e);
                Platform.runLater(() -> showError("An error occurred while loading jobs."));
            } finally {
                Platform.runLater(() -> showLoading(false));
            }
        });
    }

    private void displayJob(Job job) {
        VBox jobBox = new VBox(10);
        jobBox.setStyle("""
            -fx-padding: 15;
            -fx-background-color: #334155;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-effect: dropshadow(gaussian, #00000040, 10, 0, 0, 2);
            -fx-border-color: #475569;
            -fx-border-width: 1;
        """);

        // Add fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), jobBox);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // Job Title Section
        HBox titleBox = new HBox(10);
        titleBox.setStyle("-fx-alignment: center-left;");

        Label titleLabel = new Label(job.getTitle());
        titleLabel.setStyle("""
            -fx-font-family: 'Segoe UI';
            -fx-font-size: 18px;
            -fx-font-weight: bold;
            -fx-text-fill: #f8fafc;
        """);
        titleBox.getChildren().add(titleLabel);

        // Job Details
        VBox detailsBox = new VBox(5);
        detailsBox.setStyle("-fx-padding: 10 0;");

        Label positionLabel = createDetailLabel("📌 " + job.getPosition());
        Label locationLabel = createDetailLabel("📍 " + job.getLocation());
        Label postedDateLabel = createDetailLabel("🕒 Posted: " + job.getPostedDate().toString());

        detailsBox.getChildren().addAll(positionLabel, locationLabel, postedDateLabel);

        // Action Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setStyle("-fx-padding: 10 0 0 0;");

        Button viewButton = createActionButton("View Details", "#3b82f6");
        viewButton.setOnAction(event -> openJobDetails(job.getId(), event));

        Button applyButton = createActionButton("Quick Apply", "#10b981");
        applyButton.setOnAction(event -> handleQuickApply(job.getId()));

        buttonBox.getChildren().addAll(viewButton, applyButton);

        jobBox.getChildren().addAll(titleBox, detailsBox, buttonBox);
        jobsContainer.getChildren().add(jobBox);
    }

    private Label createDetailLabel(String text) {
        Label label = new Label(text);
        label.setStyle("""
            -fx-font-family: 'Segoe UI';
            -fx-font-size: 14px;
            -fx-text-fill: #94a3b8;
            -fx-padding: 2 0;
        """);
        return label;
    }

    private Button createActionButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(String.format("""
            -fx-background-color: %s;
            -fx-text-fill: white;
            -fx-font-family: 'Segoe UI';
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-padding: 8 16;
            -fx-background-radius: 20;
            -fx-cursor: hand;
        """, color));

        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-opacity: 0.9;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-opacity: 0.9;", "")));

        return button;
    }

    private void openJobDetails(int jobId, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/job_detail.fxml"));
            Parent root = loader.load();

            JobDetailController controller = loader.getController();
            controller.loadJobDetails(jobId);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to open job details", e);
            showError("Failed to open job details.");
        }
    }

    private void handleQuickApply(int jobId) {
        // Implement quick apply functionality
        // This is a placeholder for the quick apply feature
        showMessage("Quick apply feature coming soon!");
    }

    private String extractTextFromPdf(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            logger.warning("File does not exist or is not valid: " + filePath);
            return null;
        }

        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading CV PDF", e);
            return null;
        }
    }

    private List<String> extractKeywordsFromCV(String cvContent) {
        // Enhanced keyword list
        return List.of(
                "full stack", "developer", "javascript", "typescript", "node.js",
                "react", "angular", "vue.js", "python", "java", "spring",
                "docker", "kubernetes", "aws", "azure", "devops",
                "ci/cd", "agile", "scrum", "sql", "nosql",
                "mongodb", "postgresql", "rest api", "microservices"
        );
    }

    private void showLoading(boolean show) {
        loadingIndicator.setVisible(show);
        loadingIndicator.setManaged(show);
    }

    private void showError(String message) {
        errorMessageLabel.setText(message);
        errorContainer.setVisible(true);
        errorContainer.setManaged(true);
    }

    private void showMessage(String message) {
        // Implement a toast or notification system
        System.out.println(message); // Placeholder
    }

    private void clearContent() {
        jobsContainer.getChildren().clear();
        errorContainer.setVisible(false);
        errorContainer.setManaged(false);
    }

    private void updateResultCount(int count) {
        resultCountLabel.setText(String.format("Found %d matching jobs", count));
    }
}