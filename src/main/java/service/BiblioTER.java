package service;
import controllers.GUI;
import javafx.application.Application;

import java.io.IOException;

public class BiblioTER {

    public static void main(String[] args) throws IOException {
        LogOutput.clearLog();
        LogOutput.logEvent("BiblioTER launched.");
        Application.launch(GUI.class);
    }

}