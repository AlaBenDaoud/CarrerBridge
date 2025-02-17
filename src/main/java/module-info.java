module org.example.demo {
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
    opens org.example.demo.jobboardapp.Controller to javafx.fxml;
    opens org.example.demo.jobboardapp.Models to javafx.base; // Add this line

    opens org.example.demo to javafx.fxml;
    exports org.example.demo.jobboardapp;
}