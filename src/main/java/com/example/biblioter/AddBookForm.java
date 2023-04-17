package com.example.biblioter;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddBookForm extends Application {
    @FXML
    private Button addBookButton;

    @FXML
    private TextField authorField;

    @FXML
    private TextField borrowedField;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField idField;

    @FXML
    private TextField titleField;

    private GUI guiController;

    public void setGUIController(GUI guiController) {
        this.guiController = guiController;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(BiblioTER.class.getResource("addBookForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("Dodaj książkę");
        stage.setScene(scene);
        stage.show();
    }

    public void onAddBookButtonClicked() throws IOException {
        Book book=new Book();
        book.setId(Integer.parseInt(idField.getText()));
        book.setTitle(titleField.getText());
        book.setAuthor(authorField.getText());
        book.setCategory(categoryField.getText());
        book.setBorrowed(borrowedField.getText());
        guiController.addBook(book);
        Stage stage = (Stage) addBookButton.getScene().getWindow();
        stage.close();
    }
}
