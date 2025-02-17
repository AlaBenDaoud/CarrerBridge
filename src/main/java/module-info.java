module org.example.pi {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web; // Ensure this line is present
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens org.example.pi to javafx.fxml;
    opens org.example.pi.controllers to javafx.fxml; // This line is crucial
    opens org.example.pi.models to javafx.base;
    exports org.example.pi.controllers;
    exports org.example.pi.models;
    exports org.example.pi;
}