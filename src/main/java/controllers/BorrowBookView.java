package controllers;

import javafx.css.converter.StringConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import service.LogOutput;
import service.database.DBBook;
import service.database.DBBorrows;
import service.database.DBReader;
import service.objects.Book;
import service.objects.Reader;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.sql.SQLException;

public class BorrowBookView {


    //Attributes
    @FXML
    private TextFlow bookDetails;

    @FXML
    private Button borrowButton;

    @FXML
    private ChoiceBox<Reader> selectBorrower;

    public GUI parentController;
    public Book book;
    public DBBook bookConnection;
    public DBReader readerConnection;
    public DBBorrows borrowsConnection;
    public Reader borrower;


    //WindowControllers

    public static void launchBorrowBookView(GUI parentController, Book book) throws Exception {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(BookDetailsWindow.class.getResource("/view/borrowBookView.fxml"));
        Stage stage=new Stage();
        stage.setTitle("Wypożycz "+book.getTitle());
        stage.setScene(new Scene(loader.load(), 600, 300));
        BorrowBookView borrowBookView =loader.getController();
        borrowBookView.settings(book, parentController);
        borrowBookView.showBook(book);
        borrowBookView.setReaderList();
        stage.show();
        LogOutput.logEvent("Borrow creator window launched.");
    }

    private void settings(Book book,GUI parentController) throws SQLException, ClassNotFoundException, IOException {
        this.book = book;
        this.parentController=parentController;
        this.bookConnection=new DBBook();
        this.readerConnection=new DBReader();
        this.borrowsConnection=new DBBorrows();
    }

    private void closeWindow() throws IOException {
        Stage stage = (Stage) borrowButton.getScene().getWindow();
        stage.close();
        LogOutput.logEvent("Borrow creator window closed.");
    }

    private void showBook(Book book) throws IOException {
        showBookDetails(book, bookDetails);
        return;
    }

    public static void showBookDetails(Book book, TextFlow bookDetails) throws IOException {
        bookDetails.getChildren().clear();
        Text id,title,author,category, accessibility;
        id=new Text("ID książki: "+book.getId() +"\n");
        title=new Text("Tytuł: "+book.getTitle()+"\n");
        author=new Text("Autor: "+book.getAuthor()+"\n");
        category=new Text("Kategoria: "+book.getCategory()+"\n");
        accessibility=new Text("Dostępność: "+book.getAccessible()+"\n");
        bookDetails.getChildren().add(id);
        bookDetails.getChildren().add(title);
        bookDetails.getChildren().add(author);
        bookDetails.getChildren().add(category);
        bookDetails.getChildren().add(accessibility);
        LogOutput.logEvent("Book "+book.getId()+" shown.");
    }

    private void setReaderList() throws SQLException, IOException {
        List<Reader> readers=readerConnection.getReaders();
        for(Reader reader:readers){
            selectBorrower.getItems().add(reader);
        }
    }


    private void onBorrowerSelect(){
        this.borrower=selectBorrower.getSelectionModel().getSelectedItem();

    }
}
