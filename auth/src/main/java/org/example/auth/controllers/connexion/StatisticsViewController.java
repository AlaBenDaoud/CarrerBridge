package org.example.auth.controllers.connexion;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.example.auth.services.CompanyService;
import org.example.auth.services.UserService;

import java.util.Map;

public class StatisticsViewController {

    private CompanyService companyService = new CompanyService();
    private UserService userService = new UserService();

    // Référence au graphique LineChart depuis le FXML
    @FXML
    private LineChart<Number, Number> statisticsChart;

    // Méthode pour initialiser le graphique
    public void initialize() {
        // Récupérer les données depuis les services
        int totalUsers = userService.getTotalUsersCount();  // Récupérer le total des utilisateurs
        int totalCompanies = companyService.getTotalCompaniesCount();  // Récupérer le total des entreprises

        // Créer une série pour les utilisateurs
        XYChart.Series<Number, Number> userSeries = new XYChart.Series<>();
        userSeries.setName("Utilisateurs");  // Nom de la série
        userSeries.getData().add(new XYChart.Data<>(1, totalUsers));  // Ajouter le nombre d'utilisateurs (absolu)

        // Créer une série pour les entreprises
        XYChart.Series<Number, Number> companySeries = new XYChart.Series<>();
        companySeries.setName("Entreprises");  // Nom de la série
        companySeries.getData().add(new XYChart.Data<>(1, totalCompanies));  // Ajouter le nombre d'entreprises (absolu)

        // Ajouter les séries au graphique
        statisticsChart.getData().addAll(userSeries, companySeries);
    }
}
