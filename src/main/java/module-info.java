module com.example.biblioter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.biblioter to javafx.fxml;
    exports com.example.biblioter;
}