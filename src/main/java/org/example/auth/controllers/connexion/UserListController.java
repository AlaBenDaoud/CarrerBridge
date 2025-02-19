package org.example.auth.controllers.connexion;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.example.auth.models.User;
import org.example.auth.services.UserService;

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
        // Logic to go back to the Admin Dashboard
    }
}
