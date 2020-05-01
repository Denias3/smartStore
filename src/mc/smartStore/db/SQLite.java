package mc.smartStore.db;

import mc.smartStore.Message;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLite {
    private File file;
    private String url;
    private Connection connection;

    public SQLite(File file) {
        this.file = file;
        this.url = "jdbc:sqlite:" + this.file;
    }


    public Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed() && connection.isValid(1000)) {
            return connection;
        }
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        return connection = DriverManager.getConnection(url, config.toProperties());
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
