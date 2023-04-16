package com.example.biblioter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Book {
    int id;
    String title;
    String author;
    String category;
    String borrowed;
    Date whenBack;
    Book(int id, String title, String author, String category, String borrowed, Date whenBack){
        this.id=id;
        this.title=title;
        this.author=author;
        this.borrowed=borrowed;
        this.category=category;
        this.whenBack=whenBack;
    }
    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(whenBack);
        return title + " | " + author + " | " + category + " | " + borrowed + " | " + formattedDate;
    }
}
