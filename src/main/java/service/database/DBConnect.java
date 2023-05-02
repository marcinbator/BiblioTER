package service.database;

import service.LogOutput;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {


    //Attributes

    protected Connection connection;
    public static int booksAmount=0;


    //Connection operations

    protected void connect() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        //String username = "sql7615554";
        //String password = "6kcHFl8kCK";
        //String url = "jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7615554";
        String username="root";
        String password="javademo";
        String url="jdbc:mysql://localhost:3306/javadatabase";
        connection = DriverManager.getConnection(url, username, password);
        LogOutput.logEvent("Database connection established.");
    }

    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    public void close() throws SQLException, IOException {
        connection.close();
        LogOutput.logEvent("Database connection closed.");
    }
}