package com.example.app;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class AIAssistantController {

    @FXML
    private Button mainMenuButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button sendMessageButton;

    @FXML
    private TextField textFieldBox;

    @FXML
    void returnToMainMenu(ActionEvent event) throws IOException {
        MenuController.goToMenu();
    }

    @FXML
    void sendMessage(ActionEvent event) {
        String userInput = textFieldBox.getText();
        textFieldBox.clear();
        Text text = new Text();
        text.setWrappingWidth(530);
        text.setText("Fuse is thinking...");
        scrollPane.setContent(text);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String response = OpenAIAPI.chatGPT(userInput);
                displayResponse(response);
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }

    private void displayResponse(String response) {
        Text text = new Text();
        text.setWrappingWidth(530);

        Platform.runLater(() -> {
            scrollPane.setContent(text);
        });

        for (int i = 0; i < response.length(); i++) {
            final int index = i;
            Platform.runLater(() -> {
                text.setText(response.substring(0, index + 1));
            });
            try {
                Thread.sleep(25); // Adjust the delay as desired
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

