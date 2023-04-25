package com.example.biblioter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
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
import java.sql.SQLException;
import java.util.*;

public class GUI extends Application implements Initializable {


    //Attributes

    @FXML
    private DialogPane messagePanel;
    @FXML
    private TableColumn<Book, Integer>id;
    @FXML
    private TableColumn<Book, String>title;
    @FXML
    private TableColumn<Book, String>author;
    @FXML
    private TableColumn<Book, String>category;
    @FXML
    private TableColumn<Book, String>borrowed;
    @FXML
    private TableColumn<Book, String>accessible;

    @FXML
    public TableView<Book>booksTable;

    DBConnect connection;


    //Constructors

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
        accessible.setCellValueFactory(new PropertyValueFactory<>("accessible"));
        try {
            this.connection=new DBConnect();
            List<Book> books=connection.getBooks();
            for(Book book:books){
                booksTable.getItems().add(book);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    //Operations

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

    private ContextMenu launchContextMenu(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Edytuj");
        MenuItem menuItem2 = new MenuItem("Usuń");
        MenuItem menuItem3 = new MenuItem("");
        List<Book> books=booksTable.getSelectionModel().getSelectedItems();
        if(books.get(0).isAccessible()){
            menuItem3.setText("Oznacz jako wypożyczoną");
        }else{
            menuItem3.setText("Oznacz jako dostępną");
        }
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
        menuItem3.setOnAction(event -> {
            try {
                if(onSetAccessibleButtonClick(books.get(0))){
                    menuItem3.setText("Oznacz jako wypożyczoną");
                    System.out.println("Dodaj wypożyczającego!");
                }
                else{
                    menuItem3.setText("Oznacz jako dostępną");
                }
            } catch (IOException | SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        contextMenu.getItems().addAll(menuItem1, menuItem2, menuItem3);
        return contextMenu;
    }


    //Listeners

    @FXML
    private void onAddBookClick() throws IOException, SQLException, ClassNotFoundException {
        Book book=new Book();
        showMessage("AddBook clicked.");
        AddBookWindow.launchAddBookForm(this, book);
    }

    @FXML
    private void onAddUserClick() {
        showMessage("AddUser clicked.");
        Book book = new Book(1, "Przykładowa książka", "Przykładowy autor", "Przykładowa kategoria", true, "Przykładowy wypożyczający");
        booksTable.getItems().add(book);
    }

    @FXML
    private void onBookClick(MouseEvent mouseEvent) throws Exception {
        if(mouseEvent.getClickCount()==2 && mouseEvent.getButton()== MouseButton.PRIMARY){
            List<Book> books=booksTable.getSelectionModel().getSelectedItems();
            for(Book book:books){
                BookDetailsWindow.launchBookDetails(this, book);
            }
        }
        else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            booksTable.setContextMenu(launchContextMenu());
        }
    }

    private void onDeleteBookClick(Book book) throws IOException, SQLException, ClassNotFoundException {
        if (connection.isClosed()) {
            connection = new DBConnect();
        }
        connection.deleteBook(book);
        booksTable.getItems().remove(book);
    }

    private void onEditBookClick(Book book) throws IOException, SQLException, ClassNotFoundException {
        AddBookWindow.launchAddBookForm(this, book);
    }

    private boolean onSetAccessibleButtonClick(Book book)throws IOException, SQLException, ClassNotFoundException{
        if(book.accessible){
            book.setAccessible(false);
            connection.editBook(book);
            booksTable.refresh();
            return false;
        }
        else{
            book.setAccessible(true);
            book.setBorrowed("");
            connection.editBook(book);
            booksTable.refresh();
            return true;
        }
    }
}