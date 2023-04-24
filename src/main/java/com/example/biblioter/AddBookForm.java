package com.example.biblioter;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AddBookForm {
    @FXML
    private Button addBookButton;
    @FXML
    private TextField authorField;
    @FXML
    private TextField borrowedField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField titleField;

    private GUI parentController;
    private Book defaultBook;
    private DBConnect connection;

    void setForm(GUI parentController, Book book) throws SQLException, ClassNotFoundException {
        this.parentController = parentController;
        this.defaultBook=book;
        this.connection=new DBConnect();
    }
    public static void launchAddBookForm(GUI parentController, Book defaultBook) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(AddBookForm.class.getResource("addBookForm.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Dodaj książkę");
        stage.setScene(new Scene(loader.load(), 700, 500));
        AddBookForm addBookFormController = loader.getController();
        addBookFormController.setForm(parentController, defaultBook);
        addBookFormController.setDefaults();
        stage.show();
    }

    void setDefaults(){
        this.titleField.setText(defaultBook.getTitle());
        this.authorField.setText(defaultBook.getAuthor());
        this.categoryField.setText(defaultBook.getCategory());
        this.borrowedField.setText(defaultBook.getBorrowed());
    }

    void closeForm(){
        Stage stage = (Stage) addBookButton.getScene().getWindow();
        stage.close();
    }

    void addBook() throws SQLException {
        defaultBook.setId(DBConnect.booksAmount+1);
        DBConnect.booksAmount++;
        parentController.booksTable.getItems().add(defaultBook);
        connection.addBook(defaultBook);
    }

    void editBook() throws SQLException {
        ObservableList<Book> books=parentController.booksTable.getItems();
        for(Book book:books){
            if(book.getId()==defaultBook.getId()){
                book.setTitle(defaultBook.title);
                book.setAuthor(defaultBook.author);
                book.setCategory(defaultBook.category);
                book.setBorrowed(defaultBook.borrowed);
            }
        }
        parentController.booksTable.setItems(books);
        parentController.booksTable.refresh();
        connection.editBook(defaultBook);
    }

    public void onAddBookButtonClicked() throws SQLException {
        defaultBook.setTitle(titleField.getText());
        defaultBook.setAuthor(authorField.getText());
        defaultBook.setCategory(categoryField.getText());
        defaultBook.setBorrowed(borrowedField.getText());
        if (defaultBook.getId() != 0) {
            editBook();
        } else {
            addBook();
        }
        closeForm();
    }
}
