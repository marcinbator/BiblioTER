package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnect {


    //Attributes

    public Connection connection;
    public static int booksAmount=0;


    //Constructor

    public DBConnect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String username = "root";
        String password = "94541409MaR!";
        String url = "jdbc:mysql://localhost:3306/javadatabase";
        connection = DriverManager.getConnection(url, username, password);
    }


    //Connection operations

    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }
    public void close() throws SQLException {
        connection.close();
    }


    //Database utilities

    private void downloadBook(Book book, ResultSet result) throws SQLException {
        book.setId(result.getInt("id"));
        book.setTitle(result.getString("title"));
        book.setAuthor(result.getString("author"));
        book.setCategory(result.getString("category"));
        book.setBorrowed(result.getString("borrowed"));
        book.setAccessible(result.getBoolean("accessibility"));
    }

    private void uploadBook(PreparedStatement statement, Book book) throws SQLException {
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setString(3, book.getCategory());
        statement.setString(4, book.getBorrowed());
        statement.setBoolean(5,book.isAccessible());
    }

    private Book getBook(Book book, PreparedStatement statement) throws SQLException {
        ResultSet result = statement.executeQuery();
        while(result.next()){
            downloadBook(book, result);
        }
        return book;
    }


    //Database operations

    public Book getBook(int id) throws SQLException {
        Book book=new Book();
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM bookstable WHERE id=?");
        statement.setInt(1,id);
        return getBook(book, statement);
    }
    public Book getBook(String title) throws SQLException {
        Book book=new Book();
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM bookstable WHERE title=?");
        statement.setString(1,title);
        return getBook(book, statement);
    }

    public List<Book> getBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM bookstable");
        ResultSet result=statement.executeQuery();
        while(result.next()){
            Book book=new Book();
            downloadBook(book, result);
            books.add(book);
            booksAmount++;
        }
        return books;
    }

    public void addBook(Book book) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO bookstable (title, author, category, borrowed, accessibility) VALUES(?, ?, ?, ?, ?)");
        uploadBook(statement, book);
        statement.executeUpdate();
    }
    public void editBook(Book book) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("UPDATE bookstable SET title=?, author=?, category=?, borrowed=?, accessibility=? WHERE id=?");
        uploadBook(statement, book);
        statement.setString(6, Integer.toString(book.getId()));
        statement.executeUpdate();
    }
    public void deleteBook(Book book) throws SQLException {
        PreparedStatement statement=connection.prepareStatement("DELETE FROM bookstable WHERE id=?");
        statement.setString(1,Integer.toString(book.getId()));
        statement.executeUpdate();
    }

}