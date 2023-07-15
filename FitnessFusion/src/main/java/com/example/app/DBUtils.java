package com.example.app;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

public class DBUtils {

    private String DB_URL;
    private String USERNAME;
    private String PASSWORD;
    private Connection connection;

    public DBUtils() {
        loadConfig();
        // Other initialization code
    }

    private void loadConfig() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        DB_URL = properties.getProperty("db.url");
        USERNAME = properties.getProperty("db.username");
        PASSWORD = properties.getProperty("db.password");
    }


    public void establishConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    public void insertData(String email, String userName, String password) throws SQLException {
        String insertSQL = "INSERT INTO users (email, username, password) VALUES (?, ?, ?)";
        establishConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            // Set values for the prepared statement
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, password);

            // Execute the insert statement
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully");
        }
    }

    public boolean checkUserName(String userName) throws SQLException {
        establishConnection();
        String selectSQL = "SELECT username FROM users WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
        preparedStatement.setString(1,userName);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean loginValidation(String userName, String password) throws SQLException {
        establishConnection();
        String selectSQL = "SELECT username,password FROM users WHERE username = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
        preparedStatement.setString(1,userName);
        preparedStatement.setString(2,password);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return true;
        } else {
            return false;
        }
    }

    public void insertWorkoutData(Date date, String trainingTypeValue, String workoutTypeValue, int lengthValue) throws SQLException {
        String insertSQL = "INSERT INTO workout_logs (user_id, workout_date, training_type, workout, duration) VALUES (?, ?, ?, ?, ?)";
        establishConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, String.valueOf(getUserID()));
            preparedStatement.setString(2, String.valueOf(Date.valueOf(String.valueOf(date))));
            preparedStatement.setString(3, trainingTypeValue);
            preparedStatement.setString(4, workoutTypeValue);
            preparedStatement.setInt(5, lengthValue);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Workout data inserted successfully. Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            System.out.println("Error inserting workout data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getUserID() throws SQLException {
        establishConnection();
        String selectSQL = "SELECT user_id from users where username= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
        preparedStatement.setString(1,HelloController.getLoggedInUsername());

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int userId = resultSet.getInt("user_id");
            return userId;
        } else {
            throw new SQLException("User not found");
        }
    }
    public void addToTable(TrackProgressController controller) throws SQLException {
        establishConnection();
        String selectSQL = "SELECT workout_logs_id, user_id, workout_date, training_type, workout, duration FROM workout_logs WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
        preparedStatement.setInt(1,getUserID());
        ResultSet resultSet = preparedStatement.executeQuery();

        controller.progress.clear();

        while (resultSet.next()) {
            controller.progress.add(new ProgressTable(resultSet.getInt("user_id"),
                                                resultSet.getDate("workout_date"),
                                                resultSet.getString("training_type"),
                                                resultSet.getString("workout"),
                                                resultSet.getInt("duration"),
                                                resultSet.getInt("workout_logs_id")));
            controller.getTrackProgressTable().setItems(controller.progress);
        }

    }

    public void deleteRow(Integer workoutLogID) throws SQLException {
        establishConnection();
        String deleteSQL = "DELETE FROM workout_logs WHERE workout_logs_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
        preparedStatement.setInt(1,workoutLogID);
        preparedStatement.executeUpdate();
    }

    public void updateRow(String newDate, String newTrainingType, String newWorkout, int newLength, Integer workoutLogID) throws SQLException {
        establishConnection();

        String updateSQL = "UPDATE workout_logs SET workout_date = ?, training_type = ?, workout = ?, duration = ? WHERE workout_logs_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
        preparedStatement.setString(1,newDate);
        preparedStatement.setString(2,newTrainingType);
        preparedStatement.setString(3, newWorkout);
        preparedStatement.setInt(4, newLength);
        preparedStatement.setInt(5,workoutLogID);
        preparedStatement.executeUpdate();
    }

    public boolean checkEmail(String email) throws SQLException {
        establishConnection();
        String selectSQL = "SELECT email from users where email= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
        preparedStatement.setString(1,email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return true;
        } else {
            return false;
        }
    }

   public LineChart<String, Number> getLineGraphData() throws SQLException {
       establishConnection();
       String selectSQL = "SELECT workout_date, training_type, duration FROM workout_logs WHERE user_id = ?";
       PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
       preparedStatement.setInt(1, getUserID());
       ResultSet resultSet = preparedStatement.executeQuery();

       ArrayList<Date> dates = new ArrayList<>();
       ArrayList<String> trainingTypes = new ArrayList<>();
       ArrayList<Integer> durations = new ArrayList<>();
       while (resultSet.next()) {
           dates.add(resultSet.getDate(1));
           trainingTypes.add(resultSet.getString(2));
           durations.add(resultSet.getInt(3));
       }

       return createLineChart(dates, trainingTypes, durations);
   }

    private LineChart<String, Number> createLineChart(ArrayList<Date> dates, ArrayList<String> trainingTypes,
                                                      ArrayList<Integer> durations) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

        // Group duration by training type
        ArrayList<String> uniqueTrainingTypes = new ArrayList<>();
        for (String trainingType : trainingTypes) {
            if (!uniqueTrainingTypes.contains(trainingType)) {
                uniqueTrainingTypes.add(trainingType);
            }
        }

        // Create series for each training type
        for (String trainingType : uniqueTrainingTypes) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(trainingType);

            for (int i = 0; i < dates.size(); i++) {
                if (trainingTypes.get(i).equals(trainingType)) {
                    series.getData().add(new XYChart.Data<>(dates.get(i).toString(), durations.get(i)));
                }
            }

            lineChart.getData().add(series);
        }

        return lineChart;
    }
}
