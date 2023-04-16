module com.example.biblioter {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.biblioter to javafx.fxml;
    exports com.example.biblioter;
}