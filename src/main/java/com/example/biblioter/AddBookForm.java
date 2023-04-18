package com.example.biblioter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddBookForm {
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

    private GUI parentController;

    void setParentController(GUI parentController) {
        this.parentController = parentController;
    }
    public static void launchAddBookForm(GUI parentController) throws IOException {
        FXMLLoader loader = new FXMLLoader(AddBookForm.class.getResource("addBookForm.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Dodaj książkę");
        stage.setScene(new Scene(loader.load(), 700, 500));
        AddBookForm addBookFormController = loader.getController();
        addBookFormController.setParentController(parentController);
        stage.show();
    }

    void closeForm(){
        Stage stage = (Stage) addBookButton.getScene().getWindow();
        stage.close();
    }

    public void onAddBookButtonClicked() {
        Book book = new Book();
        book.setId(Integer.parseInt(idField.getText()));
        book.setTitle(titleField.getText());
        book.setAuthor(authorField.getText());
        book.setCategory(categoryField.getText());
        book.setBorrowed(borrowedField.getText());
        parentController.booksTable.getItems().add(book);
        closeForm();
    }
}
