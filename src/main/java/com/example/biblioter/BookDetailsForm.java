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
    @FXML
    TextFlow textPanel;
    @FXML
    private Button addUserButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button setAvailableButton;

    GUI parentController;
    Book book;
    DBConnect connecttion;

    void setBook(Book book) {
        this.book = book;
    }

    void setParentController(GUI parentController) throws SQLException, ClassNotFoundException {
        this.parentController=parentController;
        this.connecttion=new DBConnect();
    }

    void showBook(Book book){
        Text id,title,author,category,borrowed;
        id=new Text("ID książki: "+book.id +"\n");
        title=new Text("Tytuł: "+book.title+"\n");
        author=new Text("Autor: "+book.author+"\n");
        category=new Text("Kategoria: "+book.category+"\n");
        borrowed=new Text("Pożyczył: "+book.borrowed+"\n");
        textPanel.getChildren().add(id);
        textPanel.getChildren().add(title);
        textPanel.getChildren().add(author);
        textPanel.getChildren().add(category);
        textPanel.getChildren().add(borrowed);
    }

    public static void launchBookDetails(GUI parentController, Book book) throws Exception {
        FXMLLoader loader=new FXMLLoader(BookDetailsForm.class.getResource("bookDetails.fxml"));
        Stage stage=new Stage();
        stage.setTitle(book.title);
        stage.setScene(new Scene(loader.load(), 600, 300));
        BookDetailsForm bookDetailsForm=loader.getController();
        bookDetailsForm.setParentController(parentController);
        bookDetailsForm.setBook(book);
        bookDetailsForm.showBook(book);
        stage.show();
    }

    void closeForm(){
        Stage stage = (Stage) editButton.getScene().getWindow();
        stage.close();
    }

    public void onEditBookClick() throws IOException, SQLException, ClassNotFoundException {
        AddBookForm.launchAddBookForm(parentController, book);
        closeForm();
    }

    public void onDeleteBookClick() throws SQLException {
        connecttion.deleteBook(book);
        parentController.booksTable.getItems().remove(book);
        closeForm();
    }
}
