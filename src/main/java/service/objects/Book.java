package service.objects;

import service.LogOutput;

import java.io.IOException;

public class Book {


    //Attributes

    int id;
    private String number;
    private String title;
    private String author;
    private String category;
    private String accessible;

    //Constructors

    public Book(){
        this.id=0;
        this.number="";
        this.title="";
        this.author="";
        this.accessible="Dostępna";
        this.category="";
    }

    public Book(int id, String number, String title, String author, String category, boolean accessible) throws IOException {
        this.setId(id);
        this.setNumber(number);
        this.setTitle(title);
        this.setAuthor(author);
        this.setCategory(category);
        this.setAccessible(accessible);
    }


    //Getters, setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber(){
        return number;
    }

    public void setNumber(String number){
        this.number=number;
    }

    public String getTitle() {
        return title;
    }

    public boolean setTitle(String title) throws IOException {
        if(stringValidate(title)){
            this.title = title;
            return true;
        }
        else{
            LogOutput.logError("Book - not proper title detected.");
            return false;
        }
    }

    public String getAuthor() {
        return author;
    }

    public boolean setAuthor(String author) throws IOException {
        if(stringValidate(author)){
            this.author = author;
            return true;
        }
        else{
            LogOutput.logError("Book - not proper author detected.");
            return false;
        }
    }

    public String getCategory() {
        return category;
    }

    public boolean setCategory(String category) throws IOException {
        if(stringValidate(category)){
            this.category = category;
            return true;
        }
        else{
            LogOutput.logError("Book - not proper author detected.");
            return false;
        }
    }

    public String getAccessible(){
        return this.accessible;
    }

    public boolean isAccessible() {
        return accessible.equals("Dostępna");
    }

    public void setAccessible(boolean accessible) {
        if(accessible){
            this.accessible="Dostępna";
        }
        else{
            this.accessible="Niedostępna";
        }
    }


    //Validation

    private static boolean stringValidate(String line){
        return line.matches("[0-9a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ ]{2,30}");
    }

}
