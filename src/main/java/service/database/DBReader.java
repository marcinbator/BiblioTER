package service.database;

import java.io.IOException;
import java.sql.SQLException;

public class DBReader extends DBConnect{


    //Constructor

    public DBReader() throws SQLException, IOException, ClassNotFoundException {
        this.connect();
    }


}