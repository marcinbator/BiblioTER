module view {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens controllers to javafx.fxml;
    exports service;
    exports controllers;
}