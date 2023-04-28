package service;
import controllers.GUI;
import javafx.application.Application;
import service.database.DBBook;
import service.database.DBBorrows;
import service.objects.Book;

import java.io.IOException;
import java.sql.SQLException;

public class BiblioTER {

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        LogOutput.clearLog();
        LogOutput.logEvent("BiblioTER launched.");
        Application.launch(GUI.class);
    }

}