package com.example.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class MenuController {

    @FXML
    private Button trackProgressButton;

    @FXML
    private Button discoverWorkoutsButton;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private Button logOutButton;

    @FXML
    void initialize() {
        populateLineChart();
    }

    public void populateLineChart() {
        DBUtils dbUtils = new DBUtils();
        try {
            LineChart<String, Number> workoutLineChart = dbUtils.getLineGraphData();
            lineChart.setData(workoutLineChart.getData());
        } catch (SQLException e) {
            System.out.println("Error retrieving workout data: " + e.getMessage());
        }
    }

    public static void goToMenu() throws IOException {
        Stage stage = HelloApplication.getPrimaryStage();
        Parent menuRoot = FXMLLoader.load(MenuController.class.getResource("menu-view.fxml"));
        Scene menuScene = new Scene(menuRoot);
        stage.setScene(menuScene);
    }

    @FXML
    void goToTrackProgress(ActionEvent event) throws IOException {

        Stage stage = HelloApplication.getPrimaryStage();
        Parent trackProgressRoot = FXMLLoader.load(getClass().getResource("trackprogress-view.fxml"));
        Scene trackProgressScene = new Scene(trackProgressRoot);
        stage.setScene(trackProgressScene);

    }

    @FXML
    void goToDiscoverWorkouts(ActionEvent event) throws IOException {

        Stage stage = HelloApplication.getPrimaryStage();
        Parent discoverWorkoutsRoot = FXMLLoader.load(getClass().getResource("explore-workouts-view.fxml"));
        Scene discoverWorkoutsScene = new Scene(discoverWorkoutsRoot);
        stage.setScene(discoverWorkoutsScene);
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        Stage stage = HelloApplication.getPrimaryStage();
        Parent loginRoot = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene loginScene = new Scene(loginRoot);
        stage.setScene(loginScene);
    }

    @FXML
    void goToAIAssistant(ActionEvent event) throws IOException {
        Stage stage = HelloApplication.getPrimaryStage();
        Parent AIAssistantRoot = FXMLLoader.load(getClass().getResource("AIAssistant-view.fxml"));
        Scene AIAssistantScene = new Scene(AIAssistantRoot);
        stage.setScene(AIAssistantScene);
    }


}
