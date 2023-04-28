package service.objects;

import service.LogOutput;

import java.io.IOException;

public class BorrowRecord {


    //Attributes

    private int id;
    private Book book;
    private Reader reader;
    private boolean active;


    //Constructors

    public BorrowRecord(){
        this.id=0;
        this.book=new Book();
        this.reader=new Reader();
        this.active=true;
    }

    public BorrowRecord(int id, Book book, Reader reader, boolean active) throws IOException {
        this.setId(id);
        this.setBook(book);
        this.setReader(reader);
        this.setActive(active);
    }


    //Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) throws IOException {
        if(!book.getTitle().equals("")&&!book.getAuthor().equals("")){
            this.book = book;
        }
        else{
            LogOutput.logError("BorrowRecord - book is not set properly.");
        }
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) throws IOException {
        if(!reader.getName().equals("")&&!reader.getSurname().equals("")){
            this.reader = reader;
        }
        else{
            LogOutput.logError("BorrowRecord - book is not set properly.");
        }
    }

    public boolean isActive(){
        return  active;
    }

    public String getActive(){
        if(active){
            return "Aktywne";
        }
        return "Nieaktywne";
    }

    public void setActive(boolean active){
        this.active=active;
    }

}
