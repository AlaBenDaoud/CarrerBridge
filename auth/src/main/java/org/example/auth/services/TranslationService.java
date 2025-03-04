package org.example.auth.services;
import java.util.Locale;
import java.util.ResourceBundle;

public class TranslationService {
    private ResourceBundle bundle;

    public TranslationService(String language) {
        Locale locale = new Locale(language);
        this.bundle = ResourceBundle.getBundle("messages", locale);
    }

    public String getTranslation(String key, String defaultValue) {
        return bundle.containsKey(key) ? bundle.getString(key) : defaultValue;
    }
}
