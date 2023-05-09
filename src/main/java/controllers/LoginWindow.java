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

public class LoginWindow {

    @FXML private AnchorPane loginWindowPane;
    @FXML private Text messagePanel;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button loginButton;

    public static User user;


    public static void launchWindow(Stage oldStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginWindow.class.getResource("/view/loginForm.fxml"));
        Stage stage=new Stage();
        stage.setTitle("Logowanie");
        stage.setScene(new Scene(loader.load(), 600, 400));
        stage.setResizable(false);
        stage.getIcons().add(image);
        BiblioTER.setCloseAction(stage);
        stage.show();
        LogOutput.logEvent("Login window launched.");
        if(oldStage!=null){
            oldStage.close();
        }
    }

    public void initialize(){
        loginWindowPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onLoginButtonClick();
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML private void onLoginButtonClick() throws IOException, SQLException, ClassNotFoundException {
        String username=usernameField.getText();
        String password=passwordField.getText();
        user=new User();
        user.setUserName(username);
        user.setPassword(password);
        DBUser connection=new DBUser();
        if(connection.authenticate(user)){
            user.setId(connection.getUserId(user));
            Stage oldStage = (Stage) loginButton.getScene().getWindow();
            GUI.launchWindow(oldStage);
            LogOutput.logEvent("Login successfull.");
        }
        else{
            messagePanel.setText("Nie udało się zalogować - sprawdź dane.");
            LogOutput.logError("Login failed - incorrect parameters.");
        }
    }

    @FXML private void onRegisterLinkClick() throws IOException {
        Stage oldStage = (Stage) loginButton.getScene().getWindow();
        RegisterWindow.launchWindow(oldStage);
    }
}
