package service.objects;

import service.LogOutput;

import java.io.IOException;

public class User {


    //Attributes

    private int id;
    private String userName;
    private String password;


    //Constructors

    public User(){
        this.id=0;
        this.userName="none";
        this.password="none";
    }

    public User(int id, String userName, String password) throws IOException {
        this.setId(id);
        this.setUserName(userName);
        this.setPassword(password);
    }


    //Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public boolean setUserName(String userName) throws IOException {
        if(stringValidate(userName)){
            this.userName = userName;
            return true;
        }
        LogOutput.logError("User creation failed - invalid username");
        return false;
    }

    public String getPassword() {
        return password;
    }

    public boolean setPassword(String password) throws IOException {
        if(passwordValidate(password)){
            this.password = password;
            return true;
        }
        LogOutput.logError("User creation failed - invalid password");
        return false;
    }


    //Authentication

    private static boolean stringValidate(String line){
        return line.matches("[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ0-9]{5,30}");
    }
    private static boolean passwordValidate(String line){
        return line.matches("[a-zA-Z0-9]{4,30}");
    }
}
