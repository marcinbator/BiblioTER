package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.LogOutput;
import service.database.DBBook;
import service.database.DBBorrows;
import service.database.DBReader;
import service.database.DBUser;
import service.objects.Book;
import service.objects.Reader;
import service.objects.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.time.temporal.ChronoUnit.DAYS;

public class GUI implements Initializable {


    //Attributes

    @FXML
    public TableView<Book>booksTable;
    @FXML
    private Text greeting;
    @FXML
    private TableColumn<Book, Integer>bookId;
    @FXML
    private TableColumn<Book, String>number;
    @FXML
    private TableColumn<Book, String>title;
    @FXML
    private TableColumn<Book, String>author;
    @FXML
    private TableColumn<Book, String>category;
    @FXML
    private TableColumn<Book, String>accessible;
    @FXML
    public TableView<Reader> readersTable;
    @FXML
    private TableColumn<Reader, Integer>readerId;
    @FXML
    private TableColumn<Reader, String>name;
    @FXML
    private TableColumn<Reader, String>surname;
    @FXML
    private TableColumn<Reader, String>phone;

    public DBBook bookConnection;
    public DBReader readerConnection;
    public DBBorrows borrowsConnection;
    public User user;
    public static Image image = new Image(Objects.requireNonNull(GUI.class.getResource("/img/logo2.png")).toString());


    //Constructors


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //init books table
        bookId.setCellValueFactory(new PropertyValueFactory<>("id"));
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        accessible.setCellValueFactory(new PropertyValueFactory<>("accessible"));
        try {
            this.bookConnection =new DBBook();
            this.borrowsConnection=new DBBorrows();
            ObservableList<Book> books= bookConnection.getBooks();
            booksTable.setItems(books);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        //init readers table
        readerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        try {
            this.readerConnection=new DBReader();
            List<Reader> readers=readerConnection.getReaders();
            for(Reader reader:readers){
                readersTable.getItems().add(reader);
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            LogOutput.logEvent("GUI initialized.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        booksTable.getSortOrder().add(title);
        readersTable.getSortOrder().add(surname);
        user=LoginWindow.user;
        if(user!=null){
            greeting.setText("Cześć, "+user.getUserName()+"!");
        }
    }


    //Operations

    private ContextMenu launchBookContextMenu(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem contextEdit = new MenuItem("Edytuj");
        MenuItem contextDelete = new MenuItem("Usuń");
        MenuItem contextAccessibility = new MenuItem("");
        MenuItem contextClone=new MenuItem("Sklonuj");
        List<Book> books=booksTable.getSelectionModel().getSelectedItems();
        if(books.get(0).isAccessible()){
            contextAccessibility.setText("Wypożycz");
            contextAccessibility.setOnAction(event->{
                try {
                    BorrowBookView.launchBorrowBookView(this, null, books.get(0));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        else{
            contextAccessibility.setText("Oznacz jako dostępną");
            contextAccessibility.setOnAction(event -> {
                try {
                    onSetAccessibleButtonClick(books.get(0));
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        contextEdit.setOnAction(event -> {
            try {
                onEditBookClick(books.get(0));
            } catch (IOException | SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        contextDelete.setOnAction(event -> {
            try {
                onDeleteBookClick(books.get(0));
            } catch (IOException | SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        contextClone.setOnAction(event->{
            try {
                onCloneBookClick(books.get(0));
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        contextMenu.getItems().addAll(contextEdit, contextDelete, contextAccessibility, contextClone);
        return contextMenu;
    }

    private ContextMenu launchReaderContextMenu(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem contextBooks = new MenuItem("Pokaż wypożyczenia");
        MenuItem contextEdit = new MenuItem("Edytuj");
        MenuItem contextDelete = new MenuItem("Usuń");
        List<Reader> readers=readersTable.getSelectionModel().getSelectedItems();
        contextBooks.setOnAction(event->{
            try {
                onShowBorrowsByReaderClick(readers.get(0));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        contextEdit.setOnAction(event -> {
            try {
                onEditReaderClick(readers.get(0));
            } catch (IOException | SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        contextDelete.setOnAction(event -> {
            try {
                onDeleteReaderClick(readers.get(0));
            } catch (IOException | SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        contextMenu.getItems().addAll(contextBooks,contextEdit, contextDelete);
        return contextMenu;
    }


    //Listeners

    @FXML
    private void onAddBookClick() throws IOException, SQLException, ClassNotFoundException {
        Book book=new Book();
        AddBookWindow.launchAddBookWindow(this, book);
        LogOutput.logEvent("Book "+book.getTitle()+" added properly.");
    }

    @FXML
    private void onAddReaderClick() throws IOException, SQLException, ClassNotFoundException {
        Reader reader=new Reader();
        AddReaderWindow.launchAddReaderWindow(this, reader);
        LogOutput.logEvent("Reader "+reader.getId()+" added properly.");
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
            booksTable.setContextMenu(launchBookContextMenu());
        }
    }

    @FXML
    private void onReaderClick() {
        readersTable.setContextMenu(launchReaderContextMenu());
    }

    private void onDeleteBookClick(Book book) throws IOException, SQLException, ClassNotFoundException {
        if (bookConnection.isClosed()) {
            bookConnection = new DBBook();
        }
        bookConnection.deleteBook(book);
        booksTable.getItems().remove(book);
        LogOutput.logEvent("Book "+book.getId()+" deleted properly.");
    }

    private void onDeleteReaderClick(Reader reader) throws SQLException, IOException, ClassNotFoundException {
        if (readerConnection.isClosed()) {
            readerConnection = new DBReader();
        }
        readerConnection.deleteReader(reader);
        readersTable.getItems().remove(reader);
        booksTable.setItems( bookConnection.getBooks());
        LogOutput.logEvent("Reader "+reader.getId()+" deleted properly.");
    }

    private void onShowBorrowsByReaderClick(Reader reader) throws Exception {
        List<Book> books=borrowsConnection.getBooksByReader(reader);
        String title="Wypożyczenia czytelnika "+reader.getName();
        StringBuilder text= new StringBuilder();
        for(Book book:books){
            LocalDate date=borrowsConnection.getBorrowDate(reader, book);
            text.append(book.getId()).append(", ");
            text.append(book.getNumber()).append(", ");
            text.append(book.getTitle()).append(", ");
            text.append(book.getAuthor()).append(", ");
            text.append(book.getCategory()).append(", ");
            text.append(date).append(" (");
            int diff= (int) DAYS.between(LocalDate.now(), date);
            text.append(diff).append(" dni");
            if(diff>30){
                text.append("!");
            }
            text.append(")\n\n");
        }
        InfoView.launchBookDetails(this, title, text.toString());
    }

    private void onEditBookClick(Book book) throws IOException, SQLException, ClassNotFoundException {
        System.out.println(book.isAccessible());
        AddBookWindow.launchAddBookWindow(this, book);
    }

    private void onEditReaderClick(Reader reader) throws SQLException, IOException, ClassNotFoundException {
        AddReaderWindow.launchAddReaderWindow(this, reader);
    }

    private void onSetAccessibleButtonClick(Book book)throws IOException, SQLException, ClassNotFoundException{
        book.setAccessible(true);
        bookConnection.editBook(book);
        borrowsConnection.deactivateAllBorrows(book);
        booksTable.refresh();
    }

    private void onCloneBookClick(Book book) throws SQLException, IOException {
        book.setAccessible(true);
        bookConnection.addBook(book);
        booksTable.getItems().add(book);
        booksTable.refresh();
        LogOutput.logEvent("Book "+book.getId()+" cloned properly.");
    }


    public void onLogoutClick() throws IOException {
        Stage oldStage = (Stage) greeting.getScene().getWindow();
        LoginWindow.launchWindow(oldStage);
    }

    public void onDeleteAccountClick() throws SQLException, IOException, ClassNotFoundException {
        DBUser connection=new DBUser();
        connection.deleteUser(user);
        onLogoutClick();
    }
}