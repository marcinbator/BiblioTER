package com.example.biblioter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    public void setParentController(GUI parentController) {
        this.parentController = parentController;
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
