package service.database;

import service.LogOutput;
import service.objects.Book;
import service.objects.BorrowRecord;
import service.objects.Reader;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBBorrows extends DBConnect{

    private final String readerQuery = "SELECT readerstable.id, readerstable.name, readerstable.surname, readerstable.phone  FROM readerstable";


    //Constructor

    public DBBorrows() throws SQLException, IOException, ClassNotFoundException {
        this.connect();
    }


    //Utilities

    private void downloadReader(ResultSet result, Reader reader) throws SQLException, IOException {
        reader.setId(result.getInt("id"));
        reader.setName(result.getString("name"));
        reader.setSurname(result.getString("surname"));
        reader.setPhone(result.getString("phone"));
        LogOutput.logEvent("Reader "+reader.getId()+" downloaded from database.");
    }

    private void downloadBook(ResultSet result, Book book) throws SQLException, IOException {
        book.setId(result.getInt("bookstable.id"));
        book.setTitle(result.getString("bookstable.title"));
        book.setAuthor(result.getString("bookstable.author"));
        book.setCategory(result.getString("bookstable.category"));
        book.setAccessible(result.getBoolean("bookstable.accessibility"));
        LogOutput.logEvent("Book "+book.getId()+" downloaded from database.");
    }

    private void downloadBorrow(ResultSet result, BorrowRecord record) throws SQLException, IOException {
        Book book=new Book();
        Reader reader=new Reader();
        book.setId(result.getInt("bookstable.id"));
        book.setTitle(result.getString("bookstable.title"));
        book.setAuthor(result.getString("bookstable.author"));
        book.setCategory(result.getString("bookstable.category"));
        book.setAccessible(result.getBoolean("bookstable.accessibility"));
        reader.setId(result.getInt("readerstable.id"));
        reader.setName(result.getString("readerstable.name"));
        reader.setSurname(result.getString("readerstable.surname"));
        reader.setPhone(result.getString("readerstable.phone"));
        record.setId(result.getInt("id"));
        record.setBook(book);
        record.setReader(reader);
        record.setActive(result.getBoolean("active"));
    }

    private void uploadBorrow(BorrowRecord record, PreparedStatement statement) throws SQLException {
        statement.setInt(1,record.getBook().getId());
        statement.setInt(2,record.getReader().getId());
        statement.setBoolean(3,record.isActive());
        statement.setDate(4, Date.valueOf(record.getBorrowDate()));
        statement.executeUpdate();
    }


    //Database operations

    public List<Reader> getReadersByBook(Book book) throws SQLException, IOException {
        List<Reader> readers=new ArrayList<>();
        String query= readerQuery +" JOIN borrowstable ON readerstable.id=borrowstable.borrowerid WHERE bookid=? ";
        PreparedStatement statement= connection.prepareStatement(query);
        statement.setInt(1,book.getId());
        ResultSet results=statement.executeQuery();
        while(results.next()){
            Reader reader=new Reader();
            downloadReader(results, reader);
            readers.add(reader);
        }
        LogOutput.logEvent("Readers of book "+book.getId()+" downloaded from database.");
        return readers;
    }

    public List<Book> getBooksByReader(Reader reader) throws SQLException, IOException {
        List<Book> books=new ArrayList<>();
        String bookQuery = "SELECT bookstable.id, bookstable.title, bookstable.author, bookstable.category, bookstable.accessibility FROM bookstable";
        String query= bookQuery +" JOIN borrowstable ON bookstable.id=borrowstable.bookid WHERE borrowerid=? AND borrowstable.active=1";
        PreparedStatement statement= connection.prepareStatement(query);
        statement.setInt(1, reader.getId());
        ResultSet results=statement.executeQuery();
        while(results.next()){
            Book book=new Book();
            downloadBook(results, book);
            books.add(book);
        }
        LogOutput.logEvent("Books that has reader "+reader.getId()+" in history downloaded from database.");
        return books;
    }

    public Reader getCurrentReader(Book book) throws SQLException, IOException {
        Reader reader=new Reader();
        String query=readerQuery+" JOIN borrowstable ON readerstable.id=borrowstable.borrowerid WHERE bookid=? AND active=1";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setInt(1,book.getId());
        ResultSet resultSet=statement.executeQuery();
        while(resultSet.next()){
            downloadReader(resultSet, reader);
        }
        return reader;
    }

    public void addBorrow(BorrowRecord record) throws SQLException, IOException {
        PreparedStatement statement=connection.prepareStatement("INSERT INTO borrowstable (bookid, borrowerid, active, date) VALUES(?,?,?,?)");
        uploadBorrow(record, statement);
        LogOutput.logEvent("Borrow "+record.getId()+" created successfully in database.");
    }

    public void deactivateAllBorrows(Book book) throws SQLException, IOException, ClassNotFoundException {
        PreparedStatement statement=connection.prepareStatement("UPDATE borrowstable SET active=0 WHERE bookid=?");
        statement.setInt(1,book.getId());
        DBBook books=new DBBook();
        book.setAccessible(true);
        books.editBook(book);
        statement.executeUpdate();
        LogOutput.logEvent("All borrows for book "+book.getId()+" deactivated.");
    }

    public LocalDate getBorrowDate(Reader reader, Book book) throws SQLException, IOException {
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM borrowstable WHERE borrowerid=? AND bookid=? AND active=1");
        statement.setInt(1,reader.getId());
        statement.setInt(2, book.getId());
        ResultSet resultSet=statement.executeQuery();
        LocalDate date = null;
        while(resultSet.next()){
            date=resultSet.getDate("date").toLocalDate();
        }
        return date;
    }
}
