package com.example.biblioter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnect {
    Connection connection;
    public static int booksAmount=0;
    DBConnect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String username = "root";
        String password = "94541409MaR!";
        String url = "jdbc:mysql://localhost:3306/javadatabase";
        connection = DriverManager.getConnection(url, username, password);
    }

    public List<Book> getRecords() throws SQLException {
        List<Book> books = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM bookstable");
        ResultSet result=statement.executeQuery();
        while(result.next()){
            Book book=new Book();
            book.setId(result.getInt("id"));
            book.setTitle(result.getString("title"));
            book.setAuthor(result.getString("author"));
            book.setCategory(result.getString("category"));
            book.setBorrowed(result.getString("borrowed"));
            books.add(book);
            booksAmount++;
        }
        connection.close();
        return books;
    }

    public void addBook(Book book) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO bookstable (title, author, category, borrowed) VALUES(?, ?, ?, ?)");
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setString(3, book.getCategory());
        statement.setString(4, book.getBorrowed());
        statement.executeUpdate();
        connection.close();
    }

    public void editBook(Book book) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("UPDATE bookstable SET title=?, author=?, category=?, borrowed=? WHERE id=?");
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setString(3, book.getCategory());
        statement.setString(4, book.getBorrowed());
        statement.setString(5, Integer.toString(book.getId()));
        statement.executeUpdate();
        connection.close();
    }

    public void deleteBook(Book book) throws SQLException {
        PreparedStatement statement=connection.prepareStatement("DELETE FROM bookstable WHERE id=?");
        statement.setString(1,Integer.toString(book.getId()));
        statement.executeUpdate();
        connection.close();
    }

    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }
}
