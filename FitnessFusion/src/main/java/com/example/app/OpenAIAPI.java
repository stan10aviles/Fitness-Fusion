package com.example.app;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class OpenAIAPI {

    private static String url;
    private static String apiKey;
    private static String model;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = OpenAIAPI.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        url = properties.getProperty("api.openai.url");
        apiKey = properties.getProperty("api.openai.key");
        model = properties.getProperty("api.openai.model");
    }

    public static String chatGPT(String message) {

        try {
            //Create the HTTP Post Request
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");

            //Build the request body
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Get the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the response
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
            JsonArray choices = jsonResponse.getAsJsonArray("choices");
            if (choices.size() > 0) {
                JsonObject choice = choices.get(0).getAsJsonObject();
                JsonObject messageObj = choice.getAsJsonObject("message");
                String content = messageObj.get("content").getAsString();
                return content;
            }

            // Handle the case when the response doesn't contain the expected content
            throw new RuntimeException("Unable to extract response content");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

