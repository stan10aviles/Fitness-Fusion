package com.example.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import com.google.gson.Gson;


public class ExercisesAPIClient {
    private static String apiURL;
    private static String apiKey;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = ExercisesAPIClient.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

        apiURL = properties.getProperty("api.url");
        apiKey = properties.getProperty("api.key");
    }

    public static List<Exercise> guidedSearchParameters(String workout, String muscleGroup, String difficulty) throws IOException {
        // Construct the API URL with desired parameters
        String encodedWorkout = URLEncoder.encode(workout, StandardCharsets.UTF_8.toString());
        String encodedMuscleGroup = URLEncoder.encode(muscleGroup, StandardCharsets.UTF_8.toString());
        String encodedDifficulty = URLEncoder.encode(difficulty, StandardCharsets.UTF_8.toString());

        String apiURLWithParams = apiURL + "?type="+ encodedWorkout + "&muscle=" + encodedMuscleGroup + "&difficulty=" + encodedDifficulty;

        // Create URL object and open connection
        URL url = new URL(apiURLWithParams);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set request method and headers
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Api-Key", apiKey);

        InputStream responseStream = connection.getInputStream();
        String jsonResponse = new Scanner(responseStream).useDelimiter("\\A").next();

        Gson gson = new Gson();
        Exercise[] exercises = gson.fromJson(jsonResponse, Exercise[].class);

        List<Exercise> exerciseList = Arrays.asList(exercises);

        connection.disconnect();
        return exerciseList;
    }

    public static List<Exercise> directSearchParameters(String name) throws IOException {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        String apiURLWithParams = apiURL + "?name=" + encodedName;

        // Create URL object and open connection
        URL url = new URL(apiURLWithParams);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Api-Key", apiKey);

        InputStream responseStream = connection.getInputStream();
        String jsonResponse = new Scanner(responseStream).useDelimiter("\\A").next();

        Gson gson = new Gson();
        Exercise[] exercises = gson.fromJson(jsonResponse, Exercise[].class);

        List<Exercise> exerciseList = Arrays.asList(exercises);

        connection.disconnect();
        return exerciseList;
    }
}
