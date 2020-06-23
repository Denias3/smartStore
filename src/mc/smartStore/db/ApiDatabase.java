package mc.smartStore.db;

import mc.smartStore.Message;
import mc.smartStore.SmartStore;
import mc.smartStore.StoreItems;
import mc.smartStore.Stores;
import mc.smartStore.utils.DBType;
import mc.smartStore.utils.StatusStore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class ApiDatabase {
    public static Connection getConnection() throws SQLException {
        if (DB == DBType.MYSQL){
            return MY_SQL.getConnection();
        }
        else {
            return SQLITE.getConnection();
        }
    }
    public static void close(){
        if (DB == DBType.MYSQL){
            MY_SQL.close();
        }
        else {
            SQLITE.close();
        }
    }
    public static DBType DB;
    private static MySQL MY_SQL;
    private static SQLite SQLITE;
    public static void init() {

        Configuration cfg = SmartStore.getPlugin().getConfig();
        if (cfg.getBoolean("connection.enable")){
            DB = DBType.MYSQL;
            MY_SQL = new MySQL(
                    cfg.getString("connection.user"),
                    cfg.getString("connection.password"),
                    cfg.getString("connection.database"),
                    cfg.getString("connection.host"),
                    cfg.getInt("connection.port")
            );
            try(Statement statement = MY_SQL.getConnection().createStatement()) {
                statement.addBatch(dbRequest.createTableShops());
                statement.addBatch(dbRequest.createTableShopItems());
                statement.executeBatch();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            DB = DBType.SQLITE;
            SQLITE = new SQLite(new File(SmartStore.getPlugin().getDataFolder(), "database.db"));
            try(Statement statement = SQLITE.getConnection().createStatement()) {
                statement.addBatch(dbRequest.createTableShops());
                statement.addBatch(dbRequest.createTableShopItems());
                statement.executeBatch();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean saveShop (Stores store) {

        if (store.check()) {
            return false;
        }
        Bukkit.getScheduler().runTaskAsynchronously(SmartStore.getPlugin(), () -> {
            int nameId = 0;
            try (PreparedStatement stat = getConnection().prepareStatement("INSERT INTO `stores`(`name`, `row_item`, `UUID_owner`, `UUID_villager`, `capital`) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement stat2 = getConnection().prepareStatement("INSERT INTO `storeitems`(`store_id`, `name`,  `base_price`, `count`,  `place`) VALUES (?, ?, ?, ?, ?)")) {
                stat.setString(1, store.getName());
                stat.setInt(2, store.getRow());
                stat.setString(3, store.getPl().toString());
                stat.setString(4, store.getU().toString());
                stat.setDouble(5, store.getCapital());
                stat.executeUpdate();
                try(ResultSet rs = stat.getGeneratedKeys()) {
                    if (rs.next()) {
                        nameId = rs.getInt(1);
                    }
                }
                for (HashMap.Entry<Integer, StoreItems> item : store.items.entrySet()) {
                    stat2.setInt(1, nameId);
                    stat2.setString(2, item.getValue().getItem().getType().name());
                    stat2.setDouble(3, item.getValue().getBasePrice());
                    stat2.setInt(4, item.getValue().getCount());
                    stat2.setInt(5, item.getValue().getPlace());
                    stat2.addBatch();
                }
                stat2.executeBatch();
                store.setStatus(StatusStore.SAVE);
                store.createMenu();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    public static void loadAllStores(){
        Bukkit.getScheduler().runTaskAsynchronously(SmartStore.getPlugin(), () -> {
            try(Statement stat = getConnection().createStatement()) {
                try(ResultSet rs = stat.executeQuery("SELECT s.UUID_owner, s.UUID_villager, s.row_item, s.store_id, s.name AS name_store, s.capital, si.name AS name_item, si.base_price,  si.count, si.place FROM stores s INNER JOIN storeitems si ON s.store_id = si.store_id ORDER BY s.store_id ASC")){
                    Stores store;
                    while(rs.next()) {
                        String storeName = rs.getString("name_store");
                        if (SmartStore.stores.containsKey(storeName)){
                            store = SmartStore.stores.get(storeName);
                        }
                        else {
                            store = new Stores(
                                    rs.getInt("row_item"),
                                    storeName,
                                    StatusStore.SAVE,
                                    UUID.fromString(rs.getString("UUID_villager")),
                                    UUID.fromString(rs.getString("UUID_owner")),
                                    rs.getDouble("capital"));
                            SmartStore.stores.put(storeName, store);
                        }
                        store.items.put(rs.getInt("place"), new StoreItems(
                                rs.getString("name_item"),
                                rs.getDouble("base_price"),
                                rs.getInt("count"),
                                rs.getInt("place")
                        ));
                        Material mater = Material.getMaterial(rs.getString("name_item"));
                        if (mater == null){
                            SmartStore.stores.remove(storeName);
                            return;
                        }
                        store.getInventory().setItem(rs.getInt("place"), new ItemStack(mater));
                        store.updateStore();
                    }

                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void updateSQL(String sql){
        Bukkit.getScheduler().runTaskAsynchronously(SmartStore.getPlugin(), () -> {
            try(Statement stat = getConnection().createStatement()) {
                stat.executeUpdate(sql);
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void updateUUID(String name, UUID u){
        Bukkit.getScheduler().runTaskAsynchronously(SmartStore.getPlugin(), () -> {
            try(PreparedStatement stat = getConnection().prepareStatement("UPDATE `stores` SET `UUID_villager`=? WHERE `name`=?")) {
                stat.setString(1, u.toString());
                stat.setString(2, name);
                stat.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void updateCount(String name, int place, int count){
        Bukkit.getScheduler().runTaskAsynchronously(SmartStore.getPlugin(), () -> {
            try(PreparedStatement stat = getConnection().prepareStatement("UPDATE `storeitems` SET `count`=? WHERE `place`=? AND `store_id`=(SELECT `store_id` FROM `stores` WHERE `name`=?)")) {
                stat.setInt(1, count);
                stat.setInt(2, place);
                stat.setString(3, name);
                stat.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    public static void updateCapital(String name,  double capital){
        Bukkit.getScheduler().runTaskAsynchronously(SmartStore.getPlugin(), () -> {
            try(PreparedStatement stat = getConnection().prepareStatement("UPDATE `stores` SET `capital`=? WHERE `name`=?")) {
                stat.setDouble(1, capital);
                stat.setString(2, name);
                stat.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    public static void updateBasePrice(String name, int place, double basePrice){
        Bukkit.getScheduler().runTaskAsynchronously(SmartStore.getPlugin(), () -> {
            try(PreparedStatement stat = getConnection().prepareStatement("UPDATE `storeitems` SET `base_price`=? WHERE `place`=? AND `store_id`=(SELECT `store_id` FROM `stores` WHERE `name`=?)")) {
                stat.setDouble(1, basePrice);
                stat.setInt(2, place);
                stat.setString(3, name);
                stat.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }



    public static void deleteStore(String name) {
        Bukkit.getScheduler().runTaskAsynchronously(SmartStore.getPlugin(), () -> {
            try(PreparedStatement stat = getConnection().prepareStatement("DELETE FROM `stores` WHERE `name`=?")) {
                stat.setString(1, name);
                stat.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


}
