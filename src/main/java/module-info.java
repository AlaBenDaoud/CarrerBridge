module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires org.apache.pdfbox; // Add this line
    requires java.desktop;      // Add this line
    requires javafx.swing; // Add this line

    opens org.example.demo to javafx.fxml;
    exports org.example.demo;

    opens org.example.demo.controller to javafx.fxml;
    exports org.example.demo.controller;

    exports org.example.demo.service;
    exports org.example.demo.model;
}