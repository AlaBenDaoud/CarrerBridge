package org.example.auth.controllers.connexion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.auth.models.User;
import org.example.auth.services.UserService;

import java.io.IOException;
import java.util.List;

public class UserListController {

    @FXML
    private ListView<String> userListView;

    private UserService userService;

    public UserListController() {
        userService = new UserService();
    }

    // Method to populate the ListView with user names and emails
    public void initialize() {
        List<User> users = userService.getAllUsers();
        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                userListView.getItems().add(user.getName() + " (" + user.getEmail() + ")");
            }
        } else {
            userListView.getItems().add("No users available.");
        }
    }

    // Handle the back button action
    @FXML
    private void handleBackButton() {
        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) userListView.getScene().getWindow();

            // Charger le fichier FXML du tableau de bord
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/AdminDashboard.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre actuelle
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AdminDashboard.fxml");
        }
    }
}
