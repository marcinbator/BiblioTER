package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.BiblioTER;
import service.LogOutput;
import service.database.DBUser;
import service.objects.User;

import java.io.IOException;
import java.sql.SQLException;

import static controllers.GUI.image;

public class RegisterWindow {

    @FXML AnchorPane registerFormPane;
    @FXML private Text messagePanel;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField password2Field;
    @FXML private Button registerButton;


    public static void launchWindow(Stage oldStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginWindow.class.getResource("/view/registerForm.fxml"));
        Stage stage=new Stage();
        stage.setTitle("Rejestracja");
        stage.setScene(new Scene(loader.load(), 600, 400));
        stage.setResizable(false);
        stage.getIcons().add(image);
        BiblioTER.setCloseAction(stage);
        stage.show();
        LogOutput.logEvent("Register window launched.");
        oldStage.close();
    }

    public void initialize(){
        registerFormPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onRegisterButtonClick();
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML private void onRegisterButtonClick() throws IOException, SQLException, ClassNotFoundException {
        String username=usernameField.getText();
        String password1=passwordField.getText();
        String password2=password2Field.getText();
        if(password1.equals(password2)){
            User user=new User();
            if(!user.setUserName(username)||!user.setPassword(password1)){
                messagePanel.setText("Nieprawidłowe dane - nazwa od 5 do 30 znaków, bez polskich znaków i spacji, hasło od 4 do 30 znaków.");
                return;
            }
            DBUser connection=new DBUser();
            if(!connection.ifUserExists(user)){
                connection.registerUser(user);
                LogOutput.logEvent("User "+username+" registered successfully.");
                onLoginLinkClick();
            }
            else{
                messagePanel.setText("Użytkownik o podanej nazwie już istnieje.");
                LogOutput.logError("User already exists.");
            }
        }
        else{
            messagePanel.setText("Hasła nie są zgodne.");
            LogOutput.logError("Passwords doesn't match.");
        }
    }

    @FXML private void onLoginLinkClick() throws IOException {
        Stage oldStage = (Stage) registerButton.getScene().getWindow();
        LoginWindow.launchWindow(oldStage);
    }
}
