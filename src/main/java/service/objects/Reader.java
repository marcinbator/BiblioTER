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

    public void setName(String name) throws IOException {
        if(stringValidate(name)){
            this.name = name;
        }
        else{
            LogOutput.logError("Reader - not proper name detected");
        }
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) throws IOException {
        if(stringValidate(surname)){
            this.surname = surname;
        }
        else{
            LogOutput.logError("Reader - not proper surname detected");
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) throws IOException {
        if(phoneValidate(phone)){
            this.phone = phone;
        }
        else{
            LogOutput.logError("Reader - not proper phone detected");
        }
    }


    //Validation

    private static boolean stringValidate(String line){
        return line.matches("[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]{5,20}");
    }

    private static boolean phoneValidate(String phone){
        return phone.matches("[0-9 ]{9,11}");
    }
}
