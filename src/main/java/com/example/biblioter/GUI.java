package com.example.biblioter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class GUI extends Application implements Initializable {

    public DialogPane messagePanel;
    public TableView<Book>booksTable;
    public TableColumn<Book, Integer>id;
    public TableColumn<Book, String>title;
    public TableColumn<Book, String>author;
    public TableColumn<Book, String>category;
    public TableColumn<Book, String>borrowed;
    DBConnect connection;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
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
        try {
            this.connection=new DBConnect();
            List<Book> books=connection.getRecords();
            for(Book book:books){
                booksTable.getItems().add(book);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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

    public void onAddBookClick() throws IOException, SQLException, ClassNotFoundException {
        Book book=new Book(0, "", "", "", "");
        showMessage("AddBook clicked.");
        AddBookForm.launchAddBookForm(this, book);
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
            booksTable.setContextMenu(launchContextMenu());
        }
    }

    private ContextMenu launchContextMenu(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Edytuj");
        MenuItem menuItem2 = new MenuItem("Usuń");
        MenuItem menuItem3 = new MenuItem("Oznacz jako dostępną");
        MenuItem menuItem4 = new MenuItem("Dodaj osobę wypożyczającą");
        List<Book> books=booksTable.getSelectionModel().getSelectedItems();
        menuItem1.setOnAction(event -> {
            try {
                onEditBookClick(books.get(0));
            } catch (IOException | SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        menuItem2.setOnAction(event -> {
            try {
                onDeleteBookClick(books.get(0));
            } catch (IOException | SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        contextMenu.getItems().addAll(menuItem1, menuItem2, menuItem3, menuItem4);
        return contextMenu;
    }

    private void onDeleteBookClick(Book book) throws IOException, SQLException, ClassNotFoundException {
        if (connection.isClosed()) {
            connection = new DBConnect();
        }
        connection.deleteBook(book);
        booksTable.getItems().remove(book);
    }

    private void onEditBookClick(Book book) throws IOException, SQLException, ClassNotFoundException {
        AddBookForm.launchAddBookForm(this, book);
    }

}