package com.example.biblioter;

public class Book {
    int id;
    String title;
    String author;
    String category;
    String borrowed;
    Book(){
        this.id=0;
        this.title="unset";
        this.author="unset";
        this.borrowed="unset";
        this.category="unset";
    }
    Book(int id, String title, String author, String category, String borrowed){
        this.id=id;
        this.title=title;
        this.author=author;
        this.borrowed=borrowed;
        this.category=category;
    }
    public void showBook(){
        System.out.println(this.getId());
        System.out.println(this.getTitle());
        System.out.println(this.getAuthor());
        System.out.println(this.getCategory());
        System.out.println(this.getBorrowed());
    }
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
}
