package com.example.biblioter;

public class Book {


    //Attributes

    int id;
    private String title;
    private String author;
    private String category;
    private String accessible;
    private String borrowed;


    //Constructors

    Book(){
        this.id=0;
        this.title="";
        this.author="";
        this.borrowed="";
        this.accessible="Niedostępne";
        this.category="";
    }

    Book(int id, String title, String author, String category,boolean accessible, String borrowed){
        this.setId(id);
        this.setTitle(title);
        this.setAuthor(author);
        this.setCategory(category);
        this.setAccessible(accessible);
        this.setBorrowed(borrowed);
    }


    //Operations

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", accessible=" + accessible +
                ", borrowed='" + borrowed + '\'' +
                '}';
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(String borrowed) {
        this.borrowed = borrowed;
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

}
