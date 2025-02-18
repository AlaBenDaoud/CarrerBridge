package com.example.testfx;
import java.util.List;
import java.util.ArrayList;

public class CVMatcher {
    public List<Offre> matchCVWithOffres(String cvText, List<Offre> offres) {
        List<Offre> matchingOffres = new ArrayList<>();

        for (Offre offre : offres) {
            // Vérifier si les compétences de l'offre sont présentes dans le CV
            if (containsKeywords(cvText, offre.getCompetences())) {
                matchingOffres.add(offre);
            }
        }

        return matchingOffres;
    }

    private boolean containsKeywords(String text, String keywords) {
        String[] keywordList = keywords.toLowerCase().split(","); // Séparer les mots-clés
        text = text.toLowerCase();

        for (String keyword : keywordList) {
            if (text.contains(keyword.trim())) {
                return true; // Retourne vrai si un mot-clé est trouvé
            }
        }

        return false;
    }
}
