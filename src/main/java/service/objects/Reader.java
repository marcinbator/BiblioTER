package service.objects;

import service.LogOutput;

import java.io.IOException;

public class Reader {


    //Attributes

    private int id;
    private String name;
    private String surname;
    private String phone;


    //Constructors

    public Reader(){
        this.id=0;
        this.name="";
        this.surname="";
        this.phone="";
    }

    public Reader(int id, String name, String surname, String phone) throws IOException {
        this.setId(id);
        this.setName(name);
        this.setSurname(surname);
        this.setPhone(phone);
    }


    //Getters, setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) throws IOException {
        if(stringValidate(name)){
            this.name = name;
            return true;
        }
        LogOutput.logError("Reader - not proper name detected");
        return false;
    }

    public String getSurname() {
        return surname;
    }

    public boolean setSurname(String surname) throws IOException {
        if(stringValidate(surname)){
            this.surname = surname;
            return true;
        }
        LogOutput.logError("Reader - not proper surname detected");
        return false;
    }

    public String getPhone() {
        return phone;
    }

    public boolean setPhone(String phone) throws IOException {
        if(phoneValidate(phone)){
            this.phone = phone;
            return true;
        }
        LogOutput.logError("Reader - not proper phone detected");
        return false;
    }


    //Validation

    private static boolean stringValidate(String line){
        return line.matches("[0-9a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ*/!?\\-()& ]{2,30}");
    }
    private static boolean phoneValidate(String phone){
        return phone.matches("^\\d{3}[\\s\\-]?\\d{3}[\\s\\-]?\\d{3}$");
    }
}
