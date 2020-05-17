package mc.smartStore.db;

import mc.smartStore.utils.DBType;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.sql.*;

import java.sql.DriverManager;

public abstract class AbstractSql {
    private Connection con;
    private String user, password, database, host;
    private int port;
    private File file;

    protected AbstractSql(String user, String password, String database, String host, int port) {
        this.user = user;
        this.password = password;
        this.database = database;
        this.host = host;
        this.port = port;
    }
    protected AbstractSql(File file) {
        this.file = file;
    }

    public abstract DBType getType();

    public Connection getConnection() throws SQLException {
        if(con != null && !con.isClosed() & con.isValid(1000)) {
            return con;
        }
        if (ApiDatabase.DB == DBType.SQLITE){
            String url = getDriver()+ this.file;
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            return DriverManager.getConnection(url, config.toProperties());
        }
        else if (ApiDatabase.DB == DBType.MYSQL){
            String url = getDriver() +host+":"+port+"/"+database;
            return con = DriverManager.getConnection(url, user, password);
        }
        return con;
    }
    public abstract String getDriver();

    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
