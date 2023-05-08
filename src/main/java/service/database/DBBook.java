package service.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import service.objects.Book;
import service.LogOutput;
import service.objects.User;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBBook extends DBConnect {


    //Constructor

    public DBBook() throws ClassNotFoundException, SQLException, IOException {
        this.connect();
    }


    //Utilities

    private void downloadBook(Book book, ResultSet result) throws SQLException, IOException {
        book.setId(result.getInt("id"));
        book.setNumber(result.getString("number"));
        book.setTitle(result.getString("title"));
        book.setAuthor(result.getString("author"));
        book.setCategory(result.getString("category"));
        book.setAccessible(result.getBoolean("accessibility"));
    }

    private void uploadBook(PreparedStatement statement, Book book) throws SQLException{
        statement.setString(1, book.getNumber());
        statement.setString(2, book.getTitle());
        statement.setString(3, book.getAuthor());
        statement.setString(4, book.getCategory());
        statement.setBoolean(5,book.isAccessible());
        statement.setInt(6, user.getId());
    }

    private Book getBook(Book book, PreparedStatement statement) throws SQLException, IOException {
        ResultSet result = statement.executeQuery();
        while(result.next()){
            downloadBook(book, result);
        }
        LogOutput.logEvent("Book "+book.getId()+" downloaded from database.");
        return book;
    }


    //Database operations

    public Book getBook(int id) throws SQLException, IOException {
        Book book=new Book();
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM bookstable WHERE id=? AND userId=?");
        statement.setInt(1,id);
        statement.setInt(2, user.getId());
        return getBook(book, statement);
    }
    public Book getBook(String title) throws SQLException, IOException {
        Book book=new Book();
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM bookstable WHERE title=? AND userId=?");
        statement.setString(1,title);
        statement.setInt(2, user.getId());
        return getBook(book, statement);
    }

    public ObservableList<Book> getBooks() throws SQLException, IOException {
        ObservableList<Book> books = FXCollections.observableArrayList();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM bookstable WHERE userId=?");
        statement.setInt(1, user.getId());
        ResultSet result=statement.executeQuery();
        while(result.next()){
            Book book=new Book();
            downloadBook(book, result);
            books.add(book);
            booksAmount++;
        }
        LogOutput.logEvent("Books downloaded from database.");
        return books;
    }

    public void addBook(Book book) throws SQLException, IOException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO bookstable (number,title, author, category, accessibility, userId) VALUES(?,?, ?,  ?, ?, ?)");
        uploadBook(statement, book);
        statement.executeUpdate();
        LogOutput.logEvent("Book "+book.getId() +" added to database.");
    }
    public void editBook(Book book) throws SQLException, IOException {
        PreparedStatement statement = connection.prepareStatement("UPDATE bookstable SET number=?, title=?, author=?, category=?, accessibility=? WHERE userId=? AND id=? ");
        uploadBook(statement, book);
        statement.setString(7, Integer.toString(book.getId()));
        statement.executeUpdate();
        LogOutput.logEvent("Book "+book.getId() +" edited in database.");
    }
    public void deleteBook(Book book) throws SQLException, IOException {
        PreparedStatement statement=connection.prepareStatement("DELETE FROM bookstable WHERE id=? AND userId=?");
        statement.setString(1,Integer.toString(book.getId()));
        statement.setInt(2, user.getId());
        statement.executeUpdate();
        LogOutput.logEvent("Book "+book.getId() +" deleted from database.");
    }
}
