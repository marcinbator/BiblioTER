package controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.LogOutput;
import service.database.DBUser;
import service.objects.User;

import java.io.IOException;
import java.sql.SQLException;

import static controllers.GUI.image;

public class LoginWindow extends Application {


    //Attributes

    @FXML
    private Button loginButton;
    @FXML
    private Text messagePanel;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField usernameField;
    public static User user;


    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/loginForm.fxml"));
        stage.setTitle("Logowanie");
        stage.setScene(new Scene(loader.load(), 600, 400));
        stage.setResizable(false);
        stage.getIcons().add(image);
        stage.show();
        LogOutput.logEvent("Login window launched.");
    }

    public void initialize(){
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onLoginButtonClick();
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void launchWindow(Stage oldStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginWindow.class.getResource("/view/loginForm.fxml"));
        Stage stage=new Stage();
        stage.setTitle("Logowanie");
        stage.setScene(new Scene(loader.load(), 600, 400));
        stage.setResizable(false);
        stage.getIcons().add(image);
        stage.show();
        LogOutput.logEvent("Login window launched.");
        oldStage.close();
    }

    @FXML
    private void onLoginButtonClick() throws IOException, SQLException, ClassNotFoundException {
        String username=usernameField.getText();
        String password=passwordField.getText();
        user=new User();
        user.setUserName(username);
        user.setPassword(password);
        DBUser connection=new DBUser();
        if(connection.authenticate(user)){
            user.setId(connection.getUserId(user));
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/gui.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            Stage stage=new Stage();
            stage.setTitle("BiblioTER");
            stage.getIcons().add(GUI.image);
            stage.setScene(scene);
            LogOutput.logEvent("GUI established.");
            stage.setOnCloseRequest(event->{
                try {
                    LogOutput.logEvent("BiblioTER closed.");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            stage.show();
            Stage oldStage = (Stage) loginButton.getScene().getWindow();
            oldStage.close();
            LogOutput.logEvent("Login successfull.");
        }
        else{
            messagePanel.setText("Nie udało się zalogować - sprawdź dane.");
            LogOutput.logError("Login failed - incorrect parameters.");
        }
    }

    @FXML
    private void onRegisterLinkClick() throws IOException {
        Stage oldStage = (Stage) loginButton.getScene().getWindow();
        RegisterWindow.launchWindow(oldStage);
    }
}
