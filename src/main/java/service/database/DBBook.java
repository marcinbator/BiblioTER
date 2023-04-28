package service.database;

import service.objects.Book;
import service.LogOutput;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBBook extends DBConnect {


    //Constructor

    public DBBook() throws ClassNotFoundException, SQLException, IOException {
        this.connect();
    }


    //Utilities

    private void downloadBook(Book book, ResultSet result) throws SQLException, IOException {
        book.setId(result.getInt("id"));
        book.setTitle(result.getString("title"));
        book.setAuthor(result.getString("author"));
        book.setCategory(result.getString("category"));
        book.setBorrowed(result.getInt("borrowed"));
        book.setAccessible(result.getBoolean("accessibility"));
    }

    private void uploadBook(PreparedStatement statement, Book book) throws SQLException, IOException {
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setString(3, book.getCategory());
        statement.setInt(4, book.getBorrowed());
        statement.setBoolean(5,book.isAccessible());
        LogOutput.logEvent("Book "+book.getId() +" uploaded to database.");
    }

    private Book getBook(Book book, PreparedStatement statement) throws SQLException, IOException {
        ResultSet result = statement.executeQuery();
        while(result.next()){
            downloadBook(book, result);
        }
        LogOutput.logEvent("Book "+book.getId() +" downloaded from database.");
        return book;
    }


    //Database operations

    public Book getBook(int id) throws SQLException, IOException {
        Book book=new Book();
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM bookstable WHERE id=?");
        statement.setInt(1,id);
        return getBook(book, statement);
    }
    public Book getBook(String title) throws SQLException, IOException {
        Book book=new Book();
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM bookstable WHERE title=?");
        statement.setString(1,title);
        return getBook(book, statement);
    }

    public List<Book> getBooks() throws SQLException, IOException {
        List<Book> books = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM bookstable");
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
        PreparedStatement statement = connection.prepareStatement("INSERT INTO bookstable (title, author, category, borrowed, accessibility) VALUES(?, ?, ?, ?, ?)");
        uploadBook(statement, book);
        statement.executeUpdate();
        LogOutput.logEvent("Book "+book.getId() +" added to database.");
    }
    public void editBook(Book book) throws SQLException, IOException {
        PreparedStatement statement = connection.prepareStatement("UPDATE bookstable SET title=?, author=?, category=?, borrowed=?, accessibility=? WHERE id=?");
        uploadBook(statement, book);
        statement.setString(6, Integer.toString(book.getId()));
        statement.executeUpdate();
        LogOutput.logEvent("Book "+book.getId() +" edited in database.");
    }
    public void deleteBook(Book book) throws SQLException, IOException {
        PreparedStatement statement=connection.prepareStatement("DELETE FROM bookstable WHERE id=?");
        statement.setString(1,Integer.toString(book.getId()));
        statement.executeUpdate();
        LogOutput.logEvent("Book "+book.getId() +" deleted from database.");
    }
}
