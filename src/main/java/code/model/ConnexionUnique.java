package code.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Vincent on 01/05/2019.
 */
public class ConnexionUnique {

    private Connection connection;

    private static ConnexionUnique instance = null;
    private static final String CONNECT_URL = "jdbc:mysql://mysql-vincent-weber.alwaysdata.net:3306/vincent-weber_projet_hotel";
    private static final String LOGIN = "144459_bd_appli";
    private static final String PASSWORD = "connexionbdappli";

    private ConnexionUnique(){
        try {
            connection = DriverManager.getConnection(CONNECT_URL,LOGIN,PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static ConnexionUnique getInstance() {
        if(instance == null)
            instance = new ConnexionUnique();
        return instance;
    }
}
