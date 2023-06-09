package service.database;

import controllers.LoginWindow;
import service.LogOutput;
import service.objects.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    protected Connection connection;
    protected User user;


    //Connection operations

    protected boolean connect() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver");
//        String username = "sql7615554";
//        String password = "6kcHFl8kCK";
//        String url = "jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7615554";
        String username="root";
        String password="javademo";
        String url="jdbc:mysql://localhost:3306/sys";
        try{
            connection = DriverManager.getConnection(url, username, password);
        }
        catch(SQLException e){
            return false;
        }
        user=LoginWindow.user;
        LogOutput.logEvent("Database connection established.");
        return true;
    }

    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    public void close() throws SQLException, IOException {
        connection.close();
        LogOutput.logEvent("Database connection closed.");
    }
}