package com.example.app;

import javafx.scene.control.Button;

import java.sql.Date;

public class ProgressTable {

    public Integer userid;
    Date date;
    String trainType;
    String workout;
    Integer duration;
    static Button editButton;

    static Button deleteButton;

    Integer workoutLogsID;

    public ProgressTable(Integer userid, Date date, String trainType, String workout, Integer duration, Integer workoutLogsID) {
        this.userid = userid;
        this.date = date;
        this.trainType = trainType;
        this.workout = workout;
        this.duration = duration;
        this.editButton = new Button("Edit");
        this.deleteButton = new Button("Delete");
        this.workoutLogsID = workoutLogsID;
    }
    public ProgressTable() {

    }

    public Integer getWorkoutLogsID() {
        return workoutLogsID;
    }

    public void setWorkoutLogsID(int workoutLogsID) {
        this.workoutLogsID = workoutLogsID;
    }


    public static Button getEditButton() {
        return editButton;
    }

    public void setEditButton(Button editButton) {
        this.editButton = editButton;
    }

    public static Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getWorkout() {
        return workout;
    }

    public void setWorkout(String workout) {
        this.workout = workout;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "ProgressTable{" +
                "userid=" + userid +
                ", date=" + date +
                ", trainType='" + trainType + '\'' +
                ", workout='" + workout + '\'' +
                ", duration=" + duration + '\'' +
                ", workoutID=" + workoutLogsID +
                '}';
    }
}
