module com.example.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires java.mail;


    opens com.example.app to javafx.fxml, com.google.gson;
    exports com.example.app;
}