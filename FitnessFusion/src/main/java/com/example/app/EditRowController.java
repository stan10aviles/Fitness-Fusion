package com.example.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import javafx.scene.control.*;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditRowController extends ProgressTable{

    @FXML
    private TextField dateEdit;
    @FXML
    private TextField trainingTypeEdit;
    @FXML
    private TextField workoutEdit;
    @FXML
    private TextField lengthEdit;
    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;

    private int workoutLogID;


    public void editRow(Date date, String trainingType, String workout, int length) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String dateString = dateFormat.format(date);
        dateEdit.setText(dateString);
        trainingTypeEdit.setText(trainingType);
        workoutEdit.setText(workout);
        lengthEdit.setText(String.valueOf(length));
    }


    @FXML
    void setApplyButton(ActionEvent event) throws SQLException, IOException {
        String newDate = dateEdit.getText();
        String newTrainingType = trainingTypeEdit.getText();
        String newWorkout = workoutEdit.getText();
        Integer newLength = Integer.valueOf(lengthEdit.getText());

        DBUtils obj = new DBUtils();
        obj.updateRow(newDate,newTrainingType,newWorkout,newLength,obtainWorkoutlogID(workoutLogID));
        applyButton.getScene().getWindow().hide();

        Stage stage = HelloApplication.getPrimaryStage();
        Parent trackProgressRoot = FXMLLoader.load(getClass().getResource("trackprogress-view.fxml"));
        Scene trackProgressScene = new Scene(trackProgressRoot);
        stage.setScene(trackProgressScene);
    }

    public int obtainWorkoutlogID(int workoutlogID) {
        this.workoutLogID = workoutlogID;
        return workoutlogID;
    }


    @FXML
    void setCancelButton(ActionEvent event) {
        cancelButton.getScene().getWindow().hide();
    }

    public TextField getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(TextField dateEdit) {
        this.dateEdit = dateEdit;
    }

    public TextField getLengthEdit() {
        return lengthEdit;
    }

    public void setLengthEdit(TextField lengthEdit) {
        this.lengthEdit = lengthEdit;
    }

    public TextField getTrainingTypeEdit() {
        return trainingTypeEdit;
    }

    public void setTrainingTypeEdit(TextField trainingTypeEdit) {
        this.trainingTypeEdit = trainingTypeEdit;
    }

    public TextField getWorkoutEdit() {
        return workoutEdit;
    }

    public void setWorkoutEdit(TextField workoutEdit) {
        this.workoutEdit = workoutEdit;
    }
}
