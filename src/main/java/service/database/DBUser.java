package service.database;

import service.LogOutput;
import service.objects.User;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUser extends DBConnect{


    //Constructor

    public DBUser() throws SQLException, IOException, ClassNotFoundException {
        this.connect();
    }


    //Database operations

    public boolean ifUserExists(User user) throws SQLException {
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM userstable WHERE username=?");
        statement.setString(1, user.getUserName());
        ResultSet result=statement.executeQuery();
        boolean isUser=false;
        while(result.next()){
            isUser=true;
        }
        return isUser;
    }

    public void registerUser(User user) throws SQLException, IOException {
        PreparedStatement statement=connection.prepareStatement("INSERT INTO userstable (id, password, username) VALUES (?, ?, ?)");
        statement.setInt(1,user.getId());
        statement.setString(2,user.getUserName());
        statement.setString(3,user.getPassword());
        statement.executeUpdate();
        LogOutput.logEvent("User "+user.getUserName()+" registered successfully.");
    }

    public boolean authenticate(User user) throws SQLException, IOException {
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM userstable WHERE username=? AND password=?");
        statement.setString(1,user.getUserName());
        statement.setString(2, user.getPassword());
        ResultSet result=statement.executeQuery();
        boolean isUser=false;
        while(result.next()){
            isUser=true;
            LogOutput.logEvent("User "+user.getUserName()+" authenticated.");
        }
        return isUser;
    }

    public void editUser(User user) throws SQLException, IOException {
        PreparedStatement statement=connection.prepareStatement("UPDATE userstable SET username=?, password=? WHERE id=?");
        statement.setString(1, user.getUserName());
        statement.setString(2,user.getPassword());
        statement.setInt(3, user.getId());
        statement.executeUpdate();
        LogOutput.logEvent("User "+user.getUserName()+" edited successfully.");
    }

    public void deleteUser(User user) throws SQLException, IOException {
        PreparedStatement statement=connection.prepareStatement("DELETE FROM userstable WHERE id=?");
        statement.setInt(1,user.getId());
        LogOutput.logEvent("User "+user.getUserName()+" deleted successfully.");
    }
}
