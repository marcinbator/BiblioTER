package service;

import java.io.IOException;
import java.util.regex.Matcher;

public class Book {


    //Attributes

    int id;
    private String title;
    private String author;
    private String category;
    private String accessible;
    private String borrowed;


    //Constructors

    public Book(){
        this.id=0;
        this.title="";
        this.author="";
        this.borrowed="";
        this.accessible="Niedostępne";
        this.category="";
    }

    public Book(int id, String title, String author, String category, boolean accessible, String borrowed) throws IOException {
        this.setId(id);
        this.setTitle(title);
        this.setAuthor(author);
        this.setCategory(category);
        this.setAccessible(accessible);
        this.setBorrowed(borrowed);
    }


    //Getters, setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws IOException {
        if(stringValidate(title)){
            this.title = title;
        }
        else{
            LogOutput.logError("Book - not proper title detected.");
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) throws IOException {
        if(stringValidate(author)){
            this.author = author;
        }
        else{
            LogOutput.logError("Book - not proper author detected.");
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) throws IOException {
        if(stringValidate(category)){
            this.category = category;
        }
        else{
            LogOutput.logError("Book - not proper author detected.");
        }
    }

    public String getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(String borrowed) throws IOException {
        if(stringValidate(borrowed)){
            this.borrowed = borrowed;
        }
        else{
            LogOutput.logError("Book - not proper author detected.");
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
        return line.matches("[a-zA-Z ]{5,20}");
    }

}
