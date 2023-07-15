package com.example.app;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.Date;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TrackProgressController {
    @FXML
    private DatePicker date;
    @FXML
    private ChoiceBox<String> trainingType;
    @FXML
    private ChoiceBox<String> workout;
    @FXML
    private TextField length;
    @FXML
    private Button logWorkoutButton;

    @FXML
    private Text emptyFieldsText;

    @FXML
    private Text workoutLoggedText;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private TableView<ProgressTable> trackProgressTable;
    @FXML
    private TableColumn<ProgressTable, Integer> uidCol;
    @FXML
    private TableColumn<ProgressTable, Date> dateCol;
    @FXML
    private TableColumn<ProgressTable, String> trainTypeCol;
    @FXML
    private TableColumn<ProgressTable, String> workoutCol;
    @FXML
    private TableColumn<ProgressTable, Integer> lengthCol;
    @FXML
    private TableColumn<ProgressTable, Button> editCol;
    @FXML
    private TableColumn<ProgressTable, Integer> workoutLogsIDCol;

    ObservableList<ProgressTable> progress = FXCollections.observableArrayList();

    private String[] trainingTypeList = {"Cardio", "Strength Training"};
    private String[] cardioList = {"Walk", "Run", "Bike", "Swim", "PickleBall"};
    private String[] strengthTrainingList = {"Biceps", "Triceps", "Chest", "Abdominal", "Calves", "Hamstrings", "Glutes", "Quads"};

    public void setCellTable() {
        uidCol.setCellValueFactory(new PropertyValueFactory<>("userid"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        trainTypeCol.setCellValueFactory(new PropertyValueFactory<>("trainType"));
        workoutCol.setCellValueFactory(new PropertyValueFactory<>("workout"));
        lengthCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        editCol.setCellValueFactory(new PropertyValueFactory<>("editButton"));
        workoutLogsIDCol.setCellValueFactory(new PropertyValueFactory<>("workoutLogsID"));
        editCol.setCellFactory(param -> new TableCell<ProgressTable, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button editButton = new Button("Edit");
                    Button deleteButton = new Button("Delete");

                    // Create an HBox container to hold the buttons
                    HBox buttonsContainer = new HBox(editButton, deleteButton);
                    buttonsContainer.setSpacing(25);

                    setGraphic(buttonsContainer);

                    editButton.setOnAction(event -> {



                        try {

                            ProgressTable rowData = getTableView().getItems().get(getIndex());

                            String workout = rowData.getWorkout();
                            String trainingType = rowData.getTrainType();
                            int length = rowData.getDuration();
                            Date date = rowData.getDate();
                            int workoutLogID = rowData.getWorkoutLogsID();



                            FXMLLoader loader = new FXMLLoader(getClass().getResource("editrow-view.fxml"));
                            Parent editRowRoot = loader.load();

                            EditRowController editRowController = loader.getController();
                            editRowController.editRow(date,trainingType,workout,length);
                            editRowController.obtainWorkoutlogID(workoutLogID);

                            Scene scene = new Scene(editRowRoot, 720, 200);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.show();


                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    });

                    // Set the action event for the delete button
                    deleteButton.setOnAction(event -> {

                        ProgressTable workoutLogID = getTableView().getItems().get(getIndex());


                        DBUtils obj = new DBUtils();
                        try {
                            obj.deleteRow(workoutLogID.getWorkoutLogsID());
                            initialize();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }


                    });

                }
            }
        });


    }


    public void initialize() throws SQLException {
        setupTrainingTypeItems();
        setCellTable();
        DBUtils obj = new DBUtils();
        //obj.establishConnection();
        obj.addToTable(this);
    }

    public void setupTrainingTypeItems() {
        trainingTypeLoad();
        trainingType.setOnAction(this::trainingTypeSelection);
    }

    public void trainingTypeLoad() {
        trainingType.getItems().clear();
        trainingType.getItems().addAll(trainingTypeList);
    }

    public void trainingTypeSelection(ActionEvent event) {
        String selection = trainingType.getValue();
        workout.getItems().clear();


        if (selection == null) {
            trainingType.setValue(null);
        } else if (selection.equals("Cardio")) {
            workout.getItems().addAll(cardioList);
        } else {
            workout.getItems().addAll(strengthTrainingList);
        }
    }

    @FXML
    void logWorkout(ActionEvent event) throws SQLException {
        LocalDate selectedDate = date.getValue();
        String trainingTypeValue = trainingType.getValue();
        String workoutTypeValue = workout.getValue();
        String lengthText = length.getText();

        if (selectedDate == null || trainingTypeValue == null || workoutTypeValue == null || lengthText.equals("")) {

            emptyFieldsText.setText("Please complete all fields.");
            workoutLoggedText.setText("");
            return;
        }

        Date sqlDate = Date.valueOf(selectedDate);
        int lengthValue = Integer.parseInt(length.getText());
        DBUtils obj = new DBUtils();
        obj.insertWorkoutData(sqlDate, trainingTypeValue, workoutTypeValue, lengthValue);
        workoutLoggedText.setText("Workout Logged!");
        emptyFieldsText.setText("");
        date.setValue(null);
        trainingType.setValue(null);
        workout.setValue(null);
        length.setText("");
        obj.addToTable(this);


    }

    @FXML
    void clearMessage(MouseEvent event) {
        workoutLoggedText.setText("");
    }

    public TableView<ProgressTable> getTrackProgressTable() {
        return trackProgressTable;
    }

    @FXML
    void returnToMainMenu(ActionEvent event) throws IOException {
        MenuController.goToMenu();
    }


}
