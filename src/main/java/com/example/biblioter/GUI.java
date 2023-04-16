package com.example.biblioter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class GUI extends Application{
    public DialogPane messagePanel;
    public TableView<Book> booksTable;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BiblioTER.class.getResource("gui.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("BiblioTER");
        stage.setScene(scene);
        stage.show();
    }
    private void showMessage(String text){
        messagePanel.setContentText(text);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> messagePanel.setContentText(""));
            }
        }, 2000);
    }
    public void onAddBookClick() {
        showMessage("AddBook clicked.");
        Calendar c = Calendar.getInstance();
        Book book = new Book(1, "Przykładowa książka", "Przykładowy autor", "Przykładowa kategoria", "Przykładowy wypożyczający", c.getTime());
        booksTable.getItems().add(book);
        booksTable.refresh();
    }

    public void onAddUserClick() {
        showMessage("AddUser clicked.");
    }
}