package com.example.biblioter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class BookDetailsForm {


    //Attributes

    @FXML
    private TextFlow textPanel;
    @FXML
    private Button editButton;
    @FXML
    private Button setAvailableButton;

    public GUI parentController;
    public Book book;
    public DBConnect connection;


    //WindowControllers

    public static void launchBookDetails(GUI parentController, Book book) throws Exception {
        FXMLLoader loader=new FXMLLoader(BookDetailsForm.class.getResource("bookDetails.fxml"));
        Stage stage=new Stage();
        stage.setTitle(book.title);
        stage.setScene(new Scene(loader.load(), 600, 300));
        BookDetailsForm bookDetailsForm=loader.getController();
        bookDetailsForm.settings(book, parentController);
        bookDetailsForm.showBook(book);
        stage.show();
    }

    private void settings(Book book,GUI parentController) throws SQLException, ClassNotFoundException {
        this.book = book;
        if (book.isAccessible()) {
            this.setAvailableButton.setText("Oznacz jako wypożyczoną");
        } else {
            this.setAvailableButton.setText("Oznacz jako dostępną");
        }
        this.parentController=parentController;
        this.connection=new DBConnect();
    }

    void closeForm(){
        Stage stage = (Stage) editButton.getScene().getWindow();
        stage.close();
    }


    //Operations

    private void showBook(Book book){
        textPanel.getChildren().clear();
        Text id,title,author,category,borrowed, accessibility;
        id=new Text("ID książki: "+book.getId() +"\n");
        title=new Text("Tytuł: "+book.getTitle()+"\n");
        author=new Text("Autor: "+book.getAuthor()+"\n");
        category=new Text("Kategoria: "+book.getCategory()+"\n");
        borrowed=new Text("Pożyczył: "+book.getBorrowed()+"\n");
        accessibility=new Text("Dostępność: "+book.isAccessible()+"\n");
        textPanel.getChildren().add(id);
        textPanel.getChildren().add(title);
        textPanel.getChildren().add(author);
        textPanel.getChildren().add(category);
        textPanel.getChildren().add(accessibility);
        textPanel.getChildren().add(borrowed);
    }


    //Listeners
    @FXML
    private void onEditBookClick() throws IOException, SQLException, ClassNotFoundException {
        AddBookForm.launchAddBookForm(parentController, book);
        closeForm();
    }

    @FXML
    private void onDeleteBookClick() throws SQLException {
        connection.deleteBook(book);
        parentController.booksTable.getItems().remove(book);
        closeForm();
    }

    @FXML
    private void onSetAccessibleButtonClick()throws SQLException {
        if(book.accessible){
            book.setAccessible(false);
            setAvailableButton.setText("Oznacz jako wypożyczoną");
        }
        else{
            book.setAccessible(true);
            book.setBorrowed("");
            setAvailableButton.setText("Oznacz jako dostępną");
        }
        connection.editBook(book);
        parentController.booksTable.refresh();
        showBook(book);
    }
}
