package mc.smartStore;

import mc.smartStore.msg;
import mc.smartStore.smartStore;
import org.bukkit.Material;

import java.io.File;
import java.sql.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class database {
    private static Connection con = null;
    private static int DB = -1;

    public static void openConnectionMySQL(){
        if (smartStore.getPlugin().getConfig().getBoolean("Connection.Enable")){
            String USER = smartStore.getPlugin().getConfig().getString("Connection.User");
            String PASSWORD = smartStore.getPlugin().getConfig().getString("Connection.Password");
            String HOST = smartStore.getPlugin().getConfig().getString("Connection.Host");
            int PORT = smartStore.getPlugin().getConfig().getInt("Connection.Port");
            String DATABASE = smartStore.getPlugin().getConfig().getString("Connection.Database");
            String URL = "jdbc:mysql://"+HOST+":"+PORT+"/"+DATABASE;
            msg.toConsole(URL);
            try {
                con = DriverManager.getConnection(URL,USER,PASSWORD);
                DB = 0;
                msg.toConsole("§2MySQL подключена");
            }catch (SQLException e) {
                e.printStackTrace();
                msg.toConsole("§cMySQL ошибка подлючения");
            }
        }else {
            smartStore.getPlugin().getPluginLoader().disablePlugin(smartStore.getPlugin());
            msg.toConsole("§cMySQL не подключен. Плагин отключен");
        }
    }
    public static void openConnectionSQLite(){
        File file = new File(smartStore.getPlugin().getDataFolder(), "database.db");
        String URL = "jdbc:sqlite:" + file;
        msg.toConsole(URL);
        try {
            con = DriverManager.getConnection(URL);
            DB = 1;
            msg.toConsole("§2SQLite подключена");
        }catch (SQLException e) {
            e.printStackTrace();
            msg.toConsole("§cSQLite ошибка подлючения. Плагин отключен");
            smartStore.getPlugin().getPluginLoader().disablePlugin(smartStore.getPlugin());
        }
    }

    public static void createTable (){
        try(Statement stat = con.createStatement()) {
            stat.execute(dbRequest.createTableShops(DB));
        }catch (SQLException e) {
            e.printStackTrace();
        }
        try(Statement stat = con.createStatement()) {
            stat.execute(dbRequest.createTableShopItems(DB));
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void close (){
        if (con != null)
        {
            try {
                con.isClosed();
                con = null;
                msg.toConsole("§2BD отключена");
            }catch (SQLException e){
                e.printStackTrace();
                msg.toConsole("§сОшибка в отключение BD");
            }
        }

    }

}
