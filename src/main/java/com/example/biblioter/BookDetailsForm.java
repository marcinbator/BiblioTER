package com.example.biblioter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class BookDetailsForm {
    @FXML
    TextFlow textPanel;

    GUI parentController;
    Book book;

    void setBook(Book book) {
        this.book = book;
    }

    void setParentController(GUI parentController){
        this.parentController=parentController;
    }

    void showBook(Book book){
        Text id,title,author,category,borrowed;
        id=new Text("ID książki: "+book.id +"\n");
        title=new Text("Tytuł: "+book.title+"\n");
        author=new Text("Autor: "+book.author+"\n");
        category=new Text("Kategoria: "+book.category+"\n");
        borrowed=new Text("Pożyczył: "+book.borrowed+"\n");
        textPanel.getChildren().add(id);
        textPanel.getChildren().add(title);
        textPanel.getChildren().add(author);
        textPanel.getChildren().add(category);
        textPanel.getChildren().add(borrowed);
    }

    public static void launchBookDetails(GUI parentController, Book book) throws Exception {
        FXMLLoader loader=new FXMLLoader(BookDetailsForm.class.getResource("bookDetails.fxml"));
        Stage stage=new Stage();
        stage.setTitle(book.title);
        stage.setScene(new Scene(loader.load(), 600, 300));
        BookDetailsForm bookDetailsForm=loader.getController();
        bookDetailsForm.setParentController(parentController);
        bookDetailsForm.setBook(book);
        bookDetailsForm.showBook(book);
        stage.show();
    }
}
