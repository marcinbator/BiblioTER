package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import service.LogOutput;
import service.database.DBBook;
import service.database.DBBorrows;
import service.objects.Book;

import java.io.IOException;
import java.sql.SQLException;

public class BookDetailsWindow {


    //Attributes

    @FXML
    private TextFlow textPanel;
    @FXML
    private Button editButton;
    @FXML
    private Button setAvailableButton;
    @FXML
    private Button borrowButton;

    public GUI parentController;
    public Book book;
    public DBBook connection;
    public DBBorrows borrowsConnection;


    //WindowControllers

    public static void launchBookDetails(GUI parentController, Book book) throws Exception {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(BookDetailsWindow.class.getResource("/view/bookDetails.fxml"));
        Stage stage=new Stage();
        stage.setTitle(book.getTitle());
        stage.setScene(new Scene(loader.load(), 600, 300));
        BookDetailsWindow bookDetailsWindow =loader.getController();
        bookDetailsWindow.settings(book, parentController);
        bookDetailsWindow.showBook(book);
        stage.show();
        LogOutput.logEvent("Book details window launched.");
    }

    private void settings(Book book,GUI parentController) throws SQLException, ClassNotFoundException, IOException {
        this.book = book;
        this.parentController=parentController;
        this.connection=new DBBook();
        this.borrowsConnection=new DBBorrows();
        borrowButton.setVisible(book.isAccessible());
        setAvailableButton.setVisible(!book.isAccessible());
    }

    public void closeWindow() throws IOException {
        Stage stage = (Stage) editButton.getScene().getWindow();
        stage.close();
        LogOutput.logEvent("Book details window closed.");
    }


    //Operations

    private void showBook(Book book) throws IOException {
        BorrowBookView.showBookDetails(book, textPanel);
    }


    //Listeners
    @FXML
    private void onEditBookClick() throws IOException, SQLException, ClassNotFoundException {
        AddBookWindow.launchAddBookWindow(parentController, book);
        closeWindow();
    }

    @FXML
    private void onDeleteBookClick() throws SQLException, IOException {
        connection.deleteBook(book);
        parentController.booksTable.getItems().remove(book);
        closeWindow();
    }

    @FXML
    private void onSetAccessibleButtonClick() throws SQLException, IOException {
        book.setAccessible(true);
        borrowsConnection.deactivateAllBorrows(book);
        connection.editBook(book);
        parentController.booksTable.refresh();
        borrowButton.setVisible(true);
        setAvailableButton.setVisible(false);
        showBook(book);
    }

    public void onBorrowHistoryClick() {

    }

    public void onAddBorrowButtonClick() throws Exception {
        BorrowBookView.launchBorrowBookView(parentController,this, book);
        setAvailableButton.setVisible(true);
    }
}
