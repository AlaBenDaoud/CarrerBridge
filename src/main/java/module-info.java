module org.example.auth {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;
    requires org.apache.pdfbox; // Add this line for PDFBox
    requires javafx.swing;
    // Open the package for reflection by javafx.fxml
    opens org.example.auth.controllers to javafx.fxml;
    opens org.example.auth to javafx.fxml;
    opens org.example.auth.controllers.connexion to javafx.fxml;  // Open the package to javafx.fxml
    opens org.example.auth.controllers.JobAndApplicantion to javafx.fxml;  // Open the package to javafx.fxml
    opens org.example.auth.models to javafx.base;


    exports org.example.auth.controllers; // Export the controllers package to javafx.fxml
    exports org.example.auth.controllers.connexion;  // Export the connexion package to javafx.fxml
    exports org.example.auth.controllers.JobAndApplicantion;  // Export the connexion package to javafx.fxml
    exports org.example.auth.models;
    exports org.example.auth;
}
