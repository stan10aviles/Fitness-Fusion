package com.example.app;

import com.example.app.email.Email;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ForgotPasswordController {

    @FXML
    private TextField emailTextField;

    @FXML
    private Text instructionsSentPopUp;
    @FXML
    private Text emailNotRegisteredText;
    @FXML
    void registerLink(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Hyperlink) event.getSource()).getScene().getWindow();
        Parent registerRoot = FXMLLoader.load(getClass().getResource("register-view.fxml"));
        Scene registerScene = new Scene(registerRoot);
        stage.setScene(registerScene);

    }


    @FXML
    void returnToLogin(ActionEvent event) throws IOException {
        Stage stage = HelloApplication.getPrimaryStage();
        Parent loginRoot = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene loginScene = new Scene(loginRoot);
        stage.setScene(loginScene);
    }

    @FXML
    void sendPasswordResetEmail(ActionEvent event) throws SQLException {
        DBUtils obj = new DBUtils();
        boolean emailIsRegistered = obj.checkEmail(emailTextField.getText());

        if (emailIsRegistered) {
            Email.sendForgotPasswordEmail(emailTextField.getText());
            instructionsSentPopUp.setText("Instructions have been sent to email.");
            emailNotRegisteredText.setText("");
        } else {
            emailNotRegisteredText.setText("Email is not registered.");
            instructionsSentPopUp.setText("");
        }
    }
}
