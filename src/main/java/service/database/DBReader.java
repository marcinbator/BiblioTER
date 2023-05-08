package service.database;

import service.LogOutput;
import service.objects.Book;
import service.objects.Reader;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBReader extends DBConnect{


    //Constructor

    public DBReader() throws SQLException, IOException, ClassNotFoundException {
        this.connect();
    }


    //Utilities

    private void uploadReader(PreparedStatement statement, Reader reader) throws SQLException {
        statement.setString(1,reader.getName());
        statement.setString(2, reader.getSurname());
        statement.setString(3, reader.getPhone());
        statement.setInt(4, user.getId());
    }

    private void downloadReader(ResultSet results, Reader reader) throws SQLException, IOException {
        reader.setId(results.getInt("id"));
        reader.setName(results.getString("name"));
        reader.setSurname(results.getString("surname"));
        reader.setPhone(results.getString("phone"));
    }

    private Reader getReader(Reader reader, PreparedStatement statement) throws SQLException, IOException {
        ResultSet results= statement.executeQuery();
        while(results.next()){
            downloadReader(results, reader);
        }
        LogOutput.logEvent("Reader "+reader.getId()+" downloaded from database.");
        return reader;
    }


    //DB operations

    public void addReader(Reader reader) throws SQLException, IOException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO readerstable (name, surname, phone, userId) VALUES(?,?,?,?) ");
        uploadReader(statement, reader);
        statement.executeUpdate();
        LogOutput.logEvent("Reader "+reader.getId()+" added to database.");
    }

    public void editReader(Reader reader) throws SQLException, IOException {
        PreparedStatement statement = connection.prepareStatement("UPDATE readerstable SET name=?, surname=?, phone=? WHERE userId=? AND id=?");
        uploadReader(statement, reader);
        statement.setInt(5, reader.getId());
        statement.executeUpdate();
        LogOutput.logEvent("Reader "+reader.getId() +" edited in database.");
    }
    public void deleteReader(Reader reader) throws SQLException, IOException, ClassNotFoundException {
        PreparedStatement statement=connection.prepareStatement("DELETE FROM readerstable WHERE id=? AND userId=?");
        statement.setString(1,Integer.toString(reader.getId()));
        statement.setInt(2, user.getId());
        statement.executeUpdate();
        DBBorrows borrows=new DBBorrows();
        List<Book> books=borrows.getBooksByReader(reader);
        for(Book book:books){
            borrows.deactivateAllBorrows(book);
        }
        LogOutput.logEvent("Reader "+reader.getId() +" deleted from database.");
    }

    public Reader getReader(int id) throws SQLException, IOException {
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM readerstable WHERE id=? AND userId=?");
        statement.setInt(1, id);
        statement.setInt(2, user.getId());
        Reader reader=new Reader();
        return getReader(reader, statement);
    }

    public Reader getReader(String name) throws SQLException, IOException {
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM readerstable WHERE name=? AND userId=?");
        statement.setString(1, name);
        statement.setInt(2, user.getId());
        Reader reader=new Reader();
        return getReader(reader, statement);
    }

    public List<Reader> getReaders() throws SQLException, IOException {
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM readerstable WHERE userId=?");
        statement.setInt(1, user.getId());
        ResultSet results=statement.executeQuery();
        List<Reader> readers=new ArrayList<>();
        while(results.next()){
            Reader reader=new Reader();
            downloadReader(results, reader);
            readers.add(reader);
        }
        LogOutput.logEvent("Readers downloaded from database.");
        return  readers;
    }

}