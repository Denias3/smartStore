package mc.smartStore.db;

import mc.smartStore.Message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private String user, password, database, host, url;
    private int port;
    private Connection connection;

    public MySQL(String user, String password, String database, String host, int port) {
        this.user = user;
        this.password =  password;
        this.database = database;
        this.host = host;
        this.port = port;
        this.url = "jdbc:mysql://"+host+":"+port+"/"+database;
    }


    public Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed() && connection.isValid(1000)) {
            return connection;
        }
        return connection = DriverManager.getConnection(url, user, password);
    }
    public void close (){
        if (connection != null)
        {
            try {
                Message.toConsole("§2DB отключена");
                connection.close();
                connection = null;

            }catch (SQLException e){
                e.printStackTrace();
                Message.toConsole("§сОшибка в отключение DB");
            }
        }
    }
}