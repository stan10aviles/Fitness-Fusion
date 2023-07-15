package com.example.app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication {
    private static Stage primaryStage;

    public static class YourRealApplication extends Application {
        @Override
        public void start(Stage stage) throws IOException {
            primaryStage = stage;
            primaryStage.setResizable(false);
            Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
            Scene scene = new Scene(root, 700, 500);
            stage.setTitle("Fitness Fusion App");
            stage.setScene(scene);
            stage.show();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        Application.launch(YourRealApplication.class, args);
    }
}