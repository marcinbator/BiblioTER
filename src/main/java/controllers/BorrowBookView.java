package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import service.LogOutput;
import service.database.DBBook;
import service.database.DBBorrows;
import service.database.DBReader;
import service.objects.Book;
import service.objects.BorrowRecord;
import service.objects.Reader;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class BorrowBookView {

    @FXML private AnchorPane borrowBookPane;
    @FXML private TextFlow bookDetails;
    @FXML private Button borrowButton;
    @FXML private ChoiceBox<String> selectBorrower;

    private GUI grandParentController;
    private Book book;
    private DBBook bookConnection;
    private DBReader readerConnection;
    private DBBorrows borrowsConnection;
    private Reader borrower;
    private List<Reader> readersList;


    //WindowControllers

    public static void launchBorrowBookView(GUI grandParentController, BookDetailsWindow parentController, Book book) throws Exception {
        if(parentController!=null){
            parentController.closeWindow();
        }
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(BookDetailsWindow.class.getResource("/view/borrowBookView.fxml"));
        Stage stage=new Stage();
        stage.setTitle("Wypożycz "+book.getTitle());
        stage.setScene(new Scene(loader.load(), 600, 400));
        stage.setResizable(false);
        stage.getIcons().add(GUI.image);
        stage.setOnCloseRequest(event->{
            try {
                LogOutput.logEvent("Borrow window closed.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        BorrowBookView borrowBookView =loader.getController();
        borrowBookView.settings(book, grandParentController);
        BookDetailsWindow.showBookDetails(book, borrowBookView.bookDetails);
        stage.show();
        LogOutput.logEvent("Borrow creator window launched.");
    }

    public void initialize() throws SQLException, IOException {
        borrowBookPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onBorrowButtonClick();
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void settings(Book book,GUI grandParentController) throws SQLException, ClassNotFoundException, IOException {
        this.book = book;
        this.grandParentController=grandParentController;
        this.bookConnection=new DBBook();
        this.readerConnection=new DBReader();
        this.borrowsConnection=new DBBorrows();
        selectBorrower.setOnAction(event->{
            onBorrowerSelect();
        });
        readersList=readerConnection.getReaders();
        for(Reader reader:readersList){
            selectBorrower.getItems().add(reader.getId()+" "+reader.getName()+" "+reader.getSurname());
        }
    }

    private void closeWindow() throws IOException {
        Stage stage = (Stage) borrowButton.getScene().getWindow();
        stage.close();
        LogOutput.logEvent("Borrow creator window closed.");
    }


    //Operations

    private void onBorrowerSelect(){
        String[]readerData=selectBorrower.getSelectionModel().getSelectedItem().split(" ");
        for(Reader reader:readersList){
            if(reader.getId() == Integer.parseInt(readerData[0])){
                borrower=reader;
            }
        }
    }

    @FXML private void onBorrowButtonClick() throws IOException, SQLException {
        if(borrower==null){
            LogOutput.logError("Borrower not selected.");
            this.closeWindow();
            return;
        }
        BorrowRecord borrow=new BorrowRecord(0,book, borrower, true, LocalDate.now());
        borrowsConnection.addBorrow(borrow);
        book.setAccessible(false);
        ObservableList<Book> books=grandParentController.booksTable.getItems();
        for(Book listBook:books){
            if(listBook.getId()==book.getId()){
                listBook.setAccessible(false);
            }
        }
        bookConnection.editBook(book);
        grandParentController.booksTable.setItems(books);
        grandParentController.booksTable.refresh();
        this.closeWindow();
        LogOutput.logEvent("Borrow added.");
    }
}
