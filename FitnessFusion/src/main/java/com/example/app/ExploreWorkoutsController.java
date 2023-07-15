package com.example.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.*;


public class ExploreWorkoutsController {

    @FXML
    private Button directSearchButton;
    @FXML
    private Button guidedsearchButton;
    @FXML
    private Button mainMenuButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField workoutNameInput;
    @FXML
    private ChoiceBox<String> workoutTypeSelect;
    @FXML
    private ChoiceBox<String> muscleGroupSelect;
    @FXML
    private ChoiceBox<String> difficultyLevelSelect;
    @FXML
    public void initialize() {
        workoutTypeSelect.getItems().addAll("cardio" ,"olympic weightlifting", "plyometrics" ,"powerlifting", "strength","stretching", "strongman");
        muscleGroupSelect.getItems().addAll("abdominals", "abductors", "adductors", "biceps", "calves", "chest", "forearms", "glutes", "hamstrings", "lats", "lower back", "middle back", "neck", "quadriceps", "traps", "triceps");
        difficultyLevelSelect.getItems().addAll("beginner", "intermediate", "expert");
    }
    @FXML
    void directSearch(ActionEvent event) throws IOException {
        List<Exercise> exercises = ExercisesAPIClient.directSearchParameters(workoutNameInput.getText());

        VBox vbox = new VBox(); // Container for the Text nodes
        vbox.setSpacing(10); // Set spacing between exercise details
        // Create and add Text nodes for each exercise

        if (exercises.size() == 0) {
            Text exerciseText = new Text();
            exerciseText.setText("No results found.");
            exerciseText.setWrappingWidth(420);
            vbox.getChildren().add(exerciseText);
            scrollPane.setContent(vbox);
        }

        for (Exercise exercise : exercises) {
            Text exerciseText = new Text();
            exerciseText.setText("Exercise Name: " + exercise.getName() + "\n" +
                    "Exercise Type: " + exercise.getType() + "\n" +
                    "Muscle Group: " + exercise.getMuscle() + "\n" +
                    "Equipment: " + exercise.getEquipment() + "\n" +
                    "Difficulty Level: " + exercise.getDifficulty() + "\n" +
                    "Instructions: " + exercise.getInstructions());

            exerciseText.setWrappingWidth(420);
            vbox.getChildren().add(exerciseText);
        }

        scrollPane.setContent(vbox);
    }

    @FXML
    void guidedSearch(ActionEvent event) throws IOException {

        List<Exercise> exercises = ExercisesAPIClient.guidedSearchParameters(workoutTypeSelect.getValue(), muscleGroupSelect.getValue(),difficultyLevelSelect.getValue());

        VBox vbox = new VBox(); // Container for the Text nodes
        vbox.setSpacing(10); // Set spacing between exercise details

        if (exercises.size() == 0) {
            Text exerciseText = new Text();
            exerciseText.setText("No results found.");
            exerciseText.setWrappingWidth(420);
            vbox.getChildren().add(exerciseText);
            scrollPane.setContent(vbox);
        }

        for (Exercise exercise : exercises) {
            Text exerciseText = new Text();
            exerciseText.setText("Exercise Name: " + exercise.getName() + "\n" +
                    "Exercise Type: " + exercise.getType() + "\n" +
                    "Muscle Group: " + exercise.getMuscle() + "\n" +
                    "Equipment: " + exercise.getEquipment() + "\n" +
                    "Difficulty Level: " + exercise.getDifficulty() + "\n" +
                    "Instructions: " + exercise.getInstructions());

                exerciseText.setWrappingWidth(420);
                vbox.getChildren().add(exerciseText);
        }

        scrollPane.setContent(vbox);
    }

    @FXML
    public void returnToMainMenu(ActionEvent event) throws IOException {
        MenuController.goToMenu();

    }


}
