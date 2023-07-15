package com.example.app;
import com.example.app.email.Email;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class HelloController {

    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField userNameTextField;
    @FXML
    private Text userNameExists;
    @FXML
    private Text accountCreated;

    @FXML
    private Text loginEmpty;

    private String userName;

    private static String loggedInUsername;


    public static void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }

    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    @FXML
    private void registerLink(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Hyperlink) event.getSource()).getScene().getWindow();
            Parent registerRoot = FXMLLoader.load(getClass().getResource("register-view.fxml"));
            Scene registerScene = new Scene(registerRoot);
            stage.setScene(registerScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loginLink(ActionEvent event) throws IOException {
        Stage stage = HelloApplication.getPrimaryStage();
        Parent loginRoot = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene loginScene = new Scene(loginRoot);
        stage.setScene(loginScene);
    }

    @FXML
    void registerOnClick(ActionEvent event) throws SQLException {
        String email = emailTextField.getText();
        String userName = userNameTextField.getText();
        String password = passwordTextField.getText();

        if (userName.equals("") || email.equals("") || password.equals("")) {
            userNameExists.setText("Please fill out all fields.");
            accountCreated.setText("");
            return;
        }

        DBUtils db = new DBUtils();
        boolean isUserNameExists = db.checkUserName(userName);
        if (isUserNameExists) {
            userNameExists.setText("User Name Already Exists!");
            accountCreated.setText("");


        } else {
            db.insertData(email,userName,password);
            accountCreated.setText("Account Created!");
            userNameExists.setText("");
            Email.sendRegistrationEmail(emailTextField.getText());
        }


    }

    @FXML
    void loginOnClick(ActionEvent event) throws SQLException, IOException {

        String userName = userNameTextField.getText();
        String password = passwordTextField.getText();

        if (userName.equals("") || password.equals("")) {
            loginEmpty.setText("Please fill out all fields.");
            return;
        }

        DBUtils obj = new DBUtils();
        boolean isCorrectLogin = obj.loginValidation(userName,password);

        if (isCorrectLogin) {
            HelloController object = new HelloController();
            object.setLoggedInUsername(userName);
            MenuController menu = new MenuController();
            menu.goToMenu();
        } else {
            loginEmpty.setText("Incorrect User Name or Password");
        }

    }

    @FXML
    void goToForgotPassword(ActionEvent event) throws IOException {
        Stage stage = HelloApplication.getPrimaryStage();
        Parent forgotPasswordRoot = FXMLLoader.load(MenuController.class.getResource("forgotpassword-view.fxml"));
        Scene forgotPasswordScene = new Scene(forgotPasswordRoot);
        stage.setScene(forgotPasswordScene);
    }

}
