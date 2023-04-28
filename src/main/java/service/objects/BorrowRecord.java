package service.objects;

import service.LogOutput;

import java.io.IOException;

public class BorrowRecord {


    //Attributes

    private int id;
    private Book book;
    private Reader reader;


    //Constructors

    public BorrowRecord(){
        this.id=0;
        this.book=new Book();
        this.reader=new Reader();
    }

    public BorrowRecord(int id, Book book, Reader reader) throws IOException {
        this.setId(id);
        this.setBook(book);
        this.setReader(reader);
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

}
