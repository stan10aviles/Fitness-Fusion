package com.example.app.email;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Email {

    String senderEmail;
    String password;
    static Properties props = new Properties();

    private static String EMAIL;
    private static String PASSWORD;

    private static void loadConfig() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = Email.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        // Retrieve email and password from properties
        EMAIL = properties.getProperty("email");
        PASSWORD = properties.getProperty("password");
    }

    public static void sendRegistrationEmail(String userEmail) {
        loadConfig();
       props.put("mail.smtp.host","smtp.gmail.com");
       props.put("mail.smtp.socketFactory.port", "465");
       props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
       props.put("mail.smtp.auth","true");
       props.put("mail.smtp.port","465");

       Session session = Session.getDefaultInstance(props,
               new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                   return new PasswordAuthentication(EMAIL, PASSWORD);
               }
                }


               );

       try{
           Message message = new MimeMessage(session);
           message.setFrom(new InternetAddress(EMAIL));
           message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
           message.setSubject("Welcome to Fitness Fusion!");
           message.setText("This email confirms your registration! Thank you for your support!");
           Transport.send(message);
       }catch(Exception e) {
           e.printStackTrace();
       }
    }

    public static void sendForgotPasswordEmail(String userEmail) {
        loadConfig();
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.port","465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL, PASSWORD);
                    }
                }


        );

        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject("Fitness Fusion Forgot Password");
            message.setText("There are currently no instructions for a password reset. You really forgot your password already?!");
            Transport.send(message);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
