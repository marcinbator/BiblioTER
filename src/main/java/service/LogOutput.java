package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogOutput {

    public static void clearLog() throws IOException {
        File file = new File("logs.txt");
        if (new File("logs.txt").exists()) {
            new FileWriter(file).write("");
        }
    }

    public static void logEvent(String message) throws IOException {
        File file = new File("logs.txt");
        if (!new File("logs.txt").exists()) {
            System.out.println("Logs file created in " + file.getPath());
        }
        FileWriter writer = new FileWriter(file, true);
        try {
            writer.append("ERROR: ").append(String.valueOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))).append(": ").append(message).append("\n");
            System.out.println("Log created.");
        } catch (IOException e) {
            System.out.println("Error writing to a file: " + e.getMessage());
        }
        writer.close();
    }

    public static void logError(String message) throws IOException {
        File file = new File("logs.txt");
        if (!new File("logs.txt").exists()) {
            System.out.println("Logs file created in " + file.getPath());
        }
        FileWriter writer = new FileWriter(file, true);
        try {
            writer.append(String.valueOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))).append(": ").append(message).append("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}