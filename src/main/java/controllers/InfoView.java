package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import service.LogOutput;

import java.io.IOException;

public class InfoView {

    @FXML private TextFlow contentField;


    public static void launchBookDetails(String title, String text) throws Exception {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(BookDetailsWindow.class.getResource("/view/infoView.fxml"));
        Stage stage=new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(loader.load(), 600, 300));
        stage.setResizable(false);
        stage.getIcons().add(GUI.image);
        stage.setOnCloseRequest(event->{
            try {
                LogOutput.logEvent("Info window closed.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        InfoView infoView = loader.getController();
        infoView.settings(text);
        stage.show();
        LogOutput.logEvent("Details window launched.");
    }

    private void settings(String text){
        Text newText = new Text(text);
        newText.setFont(new Font(19));
        contentField.getChildren().add(newText);
    }
}
