package service;

import controllers.LoginWindow;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class BiblioTER extends Application {

    public void start(Stage stage) throws IOException {
        LoginWindow.launchWindow(null);
    }

    public static void setCloseAction(Stage stage){
        stage.setOnCloseRequest(event->{
            try {
                LogOutput.logEvent("BiblioTER closed.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) throws IOException{
        LogOutput.clearLog();
        launch();
        LogOutput.logEvent("BiblioTER launched.");
    }
}