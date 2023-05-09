package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import service.LogOutput;
import service.database.DBBook;
import service.objects.Book;

import java.io.IOException;
import java.sql.SQLException;

public class AddBookWindow {

    @FXML private AnchorPane addBookFormPane;
    @FXML private TextFlow messageField;
    @FXML private TextField numberField;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField categoryField;
    @FXML private Button addBookButton;

    private GUI parentController;
    private Book defaultBook;
    private DBBook connection;


    //Window controllers

    public static void launchAddBookWindow(GUI parentController, Book defaultBook) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(BookDetailsWindow.class.getResource("/view/addBookForm.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Dodaj książkę");
        stage.setScene(new Scene(loader.load(), 600, 400));
        stage.setResizable(false);
        stage.getIcons().add(GUI.image);
        stage.setOnCloseRequest(event->{
            try {
                LogOutput.logEvent("Add book window closed.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        AddBookWindow addBookWindowController = loader.getController();
        addBookWindowController.setForm(parentController, defaultBook);
        stage.show();
        LogOutput.logEvent("Add book window launched.");
    }

    public void initialize(){
        addBookFormPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onAddBookButtonClicked();
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private void setForm(GUI parentController, Book book) throws SQLException, ClassNotFoundException, IOException {
        this.parentController = parentController;
        this.defaultBook=book;
        this.connection=new DBBook();
        this.numberField.setText(defaultBook.getNumber());
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

    private void editBook() throws IOException, SQLException {
        ObservableList<Book> books=parentController.booksTable.getItems();
        for(Book book:books){
            if(book.getId()==defaultBook.getId()){
                book.setNumber(defaultBook.getNumber());
                book.setTitle(defaultBook.getTitle());
                book.setAuthor(defaultBook.getAuthor());
                book.setCategory(defaultBook.getCategory());
                LogOutput.logEvent("Book "+book.getId()+" edited.");
            }
        }
        parentController.booksTable.setItems(books);
        connection.editBook(defaultBook);
    }


    //Listeners

    @FXML private void onAddBookButtonClicked() throws SQLException, IOException {
        if(!defaultBook.setTitle(titleField.getText())|| !defaultBook.setAuthor(authorField.getText())|| !defaultBook.setCategory(categoryField.getText())){
            LogOutput.logError("Book not added - invalid parameters.");
            Text text=new Text("Nieprawidłowe dane. Tekst powinien zawierać tylko litery, cyfry oraz spacje oraz być długości od 2 do 30 znaków.");
            text.setFill(Color.WHITE);
            messageField.getChildren().add(text);
            return;
        }
        defaultBook.setNumber(numberField.getText());
        if (defaultBook.getId() != 0) {
            editBook();
        } else {
            addBook();
        }
        parentController.booksTable.refresh();
        closeWindow();
    }
}
