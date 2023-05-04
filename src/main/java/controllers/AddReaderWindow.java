package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import service.database.DBReader;
import service.objects.Book;
import service.database.DBBook;
import service.LogOutput;
import service.objects.Reader;

import java.io.IOException;
import java.sql.SQLException;

public class AddReaderWindow {


    //Attributes

    @FXML
    private Button addReaderButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextFlow messageField;

    private GUI parentController;
    private Reader defaultReader;
    private DBReader connection;


    //WindowControllers

    public static void launchAddReaderWindow(GUI parentController, Reader defaultReader) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(BookDetailsWindow.class.getResource("/view/addReaderForm.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Dodaj czytelnika");
        stage.setScene(new Scene(loader.load(), 600, 400));
        stage.setResizable(false);
        stage.getIcons().add(GUI.image);
        AddReaderWindow addReaderWindowController = loader.getController();
        addReaderWindowController.setForm(parentController, defaultReader);
        addReaderWindowController.setDefaults();
        stage.show();
        LogOutput.logEvent("Add reader window launched.");
    }

    private void setForm(GUI parentController, Reader reader) throws SQLException, ClassNotFoundException, IOException {
        this.parentController = parentController;
        this.defaultReader =reader;
        this.connection=new DBReader();
    }

    private void setDefaults(){
        this.nameField.setText(defaultReader.getName());
        this.surnameField.setText(defaultReader.getSurname());
        this.phoneField.setText(defaultReader.getPhone());
    }

    private void closeWindow() throws SQLException, IOException {
        Stage stage = (Stage) addReaderButton.getScene().getWindow();
        connection.close();
        stage.close();
        LogOutput.logEvent("Add reader window closed.");
    }


    //Operations

    private void addReader() throws SQLException, IOException {
        parentController.readersTable.getItems().add(defaultReader);
        connection.addReader(defaultReader);
        int id=connection.getReader(defaultReader.getName()).getId();
        defaultReader.setId(id);
    }

    private void editReader() throws SQLException, IOException {
        ObservableList<Reader> readers=parentController.readersTable.getItems();
        for(Reader reader:readers){
            if(reader.getId()== defaultReader.getId()){
                reader.setName(defaultReader.getName());
                reader.setSurname(defaultReader.getSurname());
                reader.setPhone(defaultReader.getPhone());
                LogOutput.logEvent("Reader "+reader.getId()+" edited.");
            }
        }
        parentController.readersTable.setItems(readers);
        parentController.readersTable.refresh();
        connection.editReader(defaultReader);
    }


    //Listeners

    @FXML
    private void onAddReaderButtonClicked() throws SQLException, IOException {
        if(!defaultReader.setName(nameField.getText())|| !defaultReader.setSurname(surnameField.getText())|| !defaultReader.setPhone(phoneField.getText())){
            LogOutput.logError("Reader not added - invalid parameters.");
            Text text=new Text("Nieprawidłowe dane. Tekst powinien zawierać tylko litery oraz spacje oraz być długości od 2 do 30 znaków, a telefon od 9 do 11 cyfr i spacji.");
            text.setFill(Color.WHITE);
            messageField.getChildren().add(text);
            return;
        }
        if (defaultReader.getId() != 0) {
            editReader();
        } else {
            addReader();
        }
        closeWindow();
    }
}