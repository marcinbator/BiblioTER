package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import service.database.DBBorrows;
import service.objects.Book;
import service.database.DBBook;
import service.LogOutput;

import java.io.IOException;
import java.sql.SQLException;

public class AddBookWindow {


    //Attributes

    @FXML
    private Button addBookButton;
    @FXML
    private TextField authorField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField titleField;
    @FXML
    private TextFlow messageField;

    private GUI parentController;
    private Book defaultBook;
    private DBBook connection;


    //WindowControllers

    public static void launchAddBookWindow(GUI parentController, Book defaultBook) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(BookDetailsWindow.class.getResource("/view/addBookForm.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Dodaj książkę");
        stage.setScene(new Scene(loader.load(), 700, 500));
        AddBookWindow addBookWindowController = loader.getController();
        addBookWindowController.setForm(parentController, defaultBook);
        addBookWindowController.setDefaults();
        stage.show();
        LogOutput.logEvent("Add book window launched.");
    }

    private void setForm(GUI parentController, Book book) throws SQLException, ClassNotFoundException, IOException {
        this.parentController = parentController;
        this.defaultBook=book;
        this.connection=new DBBook();
    }

    private void setDefaults(){
        this.titleField.setText(defaultBook.getTitle());
        this.authorField.setText(defaultBook.getAuthor());
        this.categoryField.setText(defaultBook.getCategory());
    }

    private void closeWindow() throws SQLException, IOException {
        Stage stage = (Stage) addBookButton.getScene().getWindow();
        connection.close();
        stage.close();
        LogOutput.logEvent("Add book window closed.");
    }


    //Operations

    private void addBook() throws SQLException, IOException {
        parentController.booksTable.getItems().add(defaultBook);
        connection.addBook(defaultBook);
        int id=connection.getBook(defaultBook.getTitle()).getId();
        defaultBook.setId(id);
    }

    private void editBook() throws SQLException, IOException {
        ObservableList<Book> books=parentController.booksTable.getItems();
        for(Book book:books){
            if(book.getId()==defaultBook.getId()){
                book.setTitle(defaultBook.getTitle());
                book.setAuthor(defaultBook.getAuthor());
                book.setCategory(defaultBook.getCategory());
                book.setAccessible(defaultBook.isAccessible());
                LogOutput.logEvent("Book "+book.getId()+" edited.");
            }
        }
        parentController.booksTable.setItems(books);
        parentController.booksTable.refresh();
        connection.editBook(defaultBook);
    }


    //Listeners

    @FXML
    private void onAddBookButtonClicked() throws SQLException, IOException {
        if(!defaultBook.setTitle(titleField.getText())|| !defaultBook.setAuthor(authorField.getText())|| !defaultBook.setCategory(categoryField.getText())){
            LogOutput.logError("Book not added - invalid parameters.");
            messageField.getChildren().add(new Text("Nieprawidłowe dane. Tekst powinien zawierać tylko litery, cyfry oraz spacje oraz być długości od 2 do 30 znaków."));
            return;
        }
        defaultBook.setAccessible(true);
        if (defaultBook.getId() != 0) {
            editBook();
        } else {
            addBook();
        }
        closeWindow();
    }
}
