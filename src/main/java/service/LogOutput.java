package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogOutput {

    public static void logEvent(String message) throws IOException {
        FileWriter writer=null;
        try {
            File logFile = new File("logs.txt");
            writer = new FileWriter(logFile);
            System.out.println("Logs file created in " + logFile.getPath());
        } catch (IOException e) {
            System.out.println("Error generating logs file: " + e.getMessage());
        }
        long time = System.currentTimeMillis();//todo

        try {
            assert writer != null;
            writer.append(String.valueOf(time)).append(": ").append(message).append("\n");
            System.out.println("Log created.");
        } catch (IOException e) {
            System.out.println("Error writing to a file: " + e.getMessage());
        }
        writer.close();
    }
}
