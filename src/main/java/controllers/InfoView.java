package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import service.LogOutput;
import service.database.DBBook;
import service.database.DBBorrows;
import service.database.DBReader;

import java.io.IOException;
import java.sql.SQLException;

public class InfoView {


    //Attributes

    @FXML
    private TextFlow contentField;

    GUI parentController;
    public DBBook connection;
    public DBBorrows borrowsConnection;
    public DBReader readersConnection;
    private String content;

    public static void launchBookDetails(GUI parentController,String title, String text) throws Exception {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(BookDetailsWindow.class.getResource("/view/infoView.fxml"));
        Stage stage=new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(loader.load(), 600, 300));
        InfoView infoView = loader.getController();
        infoView.settings(parentController, title, text);
        infoView.showContent();
        stage.show();
        LogOutput.logEvent("Details window launched.");
    }

    private void settings(GUI parentController,String title, String text) throws SQLException, IOException, ClassNotFoundException {
        this.parentController=parentController;
        this.connection=new DBBook();
        this.borrowsConnection=new DBBorrows();
        this.readersConnection=new DBReader();
        this.content=text;
    }

    private void showContent() {
        Text text = new Text(content);
        text.setFont(new Font(19));
        contentField.getChildren().add(text);
    }


}
