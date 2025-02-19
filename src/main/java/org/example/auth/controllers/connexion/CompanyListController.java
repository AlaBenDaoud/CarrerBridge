package org.example.auth.controllers.connexion;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.example.auth.models.Company;
import org.example.auth.services.CompanyService;

import java.util.List;

public class CompanyListController {

    @FXML
    private ListView<String> companyListView;

    private CompanyService companyService;

    public CompanyListController() {
        companyService = new CompanyService();
    }

    // Method to populate the ListView with company names and emails
    public void initialize() {
        List<Company> companies = companyService.getAllCompanies();
        if (companies != null && !companies.isEmpty()) {
            for (Company company : companies) {
                companyListView.getItems().add(company.getCompanyName() + " (" + company.getEmail() + ")");
            }
        } else {
            companyListView.getItems().add("No companies available.");
        }
    }

    // Handle the back button action to go back to the Admin Dashboard
    @FXML
    private void handleBackButton() {
        // Logic to navigate back to the Admin Dashboard
        System.out.println("Going back to the Admin Dashboard...");
        // You can implement a similar navigation logic as done in the Admin Dashboard Controller
    }
}
