package com.example.biblioter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GUI extends Application implements Initializable {

    public DialogPane messagePanel;
    public TableView<Book>booksTable;
    public TableColumn<Book, Integer>id;
    public TableColumn<Book, String>title;
    public TableColumn<Book, String>author;
    public TableColumn<Book, String>category;
    public TableColumn<Book, String>borrowed;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BiblioTER.class.getResource("gui.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("BiblioTER");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        borrowed.setCellValueFactory(new PropertyValueFactory<>("borrowed"));
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

    //Listeners

    public void onAddBookClick() throws IOException {
        showMessage("AddBook clicked.");
        AddBookForm.launchAddBookForm(this);
    }

    public void onAddUserClick() {
        showMessage("AddUser clicked.");
        Book book = new Book(1, "Przykładowa książka", "Przykładowy autor", "Przykładowa kategoria", "Przykładowy wypożyczający");
        booksTable.getItems().add(book);
    }

    public void onBookClick(MouseEvent mouseEvent) throws Exception {
        if(mouseEvent.getClickCount()==2 && mouseEvent.getButton()== MouseButton.PRIMARY){
            List<Book> books=booksTable.getSelectionModel().getSelectedItems();
            for(Book book:books){
                BookDetailsForm.launchBookDetails(this, book);
            }
        }
        else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem menuItem1 = new MenuItem("Edytuj");
            MenuItem menuItem2 = new MenuItem("Usuń");
            MenuItem menuItem3 = new MenuItem("Oznacz jako dostępną");
            MenuItem menuItem4 = new MenuItem("Dodaj osobę wypożyczającą");
            contextMenu.getItems().addAll(menuItem1, menuItem2, menuItem3, menuItem4);
            booksTable.setContextMenu(contextMenu);
        }
    }
}