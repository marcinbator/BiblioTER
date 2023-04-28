package service.database;

import java.io.IOException;
import java.sql.SQLException;

public class DBBorrows extends DBConnect{


    //Constructor

    public DBBorrows() throws SQLException, IOException, ClassNotFoundException {
        this.connect();
    }
}
