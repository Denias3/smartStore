package mc.smartStore.db;

import mc.smartStore.smartStore;
import mc.smartStore.StoreItems;
import mc.smartStore.Stores;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class ApiDatabase {
    public static int DB;

    public static Connection getConnection() throws SQLException {
        if (DB == 0){
            return MY_SQL.getConnection();
        }
        else {
            return SQLITE.getConnection();
        }
    }

    private static MySQL MY_SQL;
    private static SQLite SQLITE;
    public static void init() {

        Configuration cfg = smartStore.getPlugin().getConfig();
        if (cfg.getBoolean("connection.enable")){
            DB = 0;
            MY_SQL = new MySQL(
                    cfg.getString("connection.user"),
                    cfg.getString("connection.password"),
                    cfg.getString("connection.database"),
                    cfg.getString("connection.host"),
                    cfg.getInt("connection.port")
            );
            try(Statement statement = MY_SQL.getConnection().createStatement()) {
                statement.addBatch(dbRequest.createTableShops(DB));
                statement.addBatch(dbRequest.createTableShopItems(DB));
                statement.executeBatch();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            DB = 1;
            SQLITE = new SQLite(new File(smartStore.getPlugin().getDataFolder(), "database.db"));
            try(Statement statement = SQLITE.getConnection().createStatement()) {
                statement.addBatch(dbRequest.createTableShops(DB));
                statement.addBatch(dbRequest.createTableShopItems(DB));
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
        Bukkit.getScheduler().runTaskAsynchronously(smartStore.getPlugin(), () -> {
            int nameId = 0;
            try (PreparedStatement stat = getConnection().prepareStatement("INSERT INTO `stores`(`name`, `UUID`) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS); PreparedStatement stat2 = getConnection().prepareStatement("INSERT INTO `storeitems`(`store_id`, `name`, `max_price`, `min_price`, `price`, `max_count`, `count`, `step`, `place`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                stat.setString(1, store.getName());
                stat.setString(2, store.getU().toString());
                stat.executeUpdate();
                try(ResultSet rs = stat.getGeneratedKeys()) {
                    if (rs.next()) {
                        nameId = rs.getInt(1);
                    }
                }
                for (HashMap.Entry<Integer, StoreItems> item : store.items.entrySet()) {
                    stat2.setInt(1, nameId);
                    stat2.setString(2, item.getValue().getItem().getType().name());
                    stat2.setInt(3, item.getValue().getMaxPrice());
                    stat2.setInt(4, item.getValue().getMinPrice());
                    stat2.setInt(5, item.getValue().getPrice());
                    stat2.setInt(6, item.getValue().getMaxCount());
                    stat2.setInt(7, item.getValue().getCount());
                    stat2.setInt(8, item.getValue().getStep());
                    stat2.setInt(9, item.getKey());
                    stat2.addBatch();
                }
                stat2.executeBatch();
                store.setStatus(2);
                store.createMenu();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    public static void loadAllStores(){
        Bukkit.getScheduler().runTaskAsynchronously(smartStore.getPlugin(), () -> {
            try(Statement stat = getConnection().createStatement()) {
                try(ResultSet rs = stat.executeQuery("SELECT s.UUID, s.store_id, s.name AS name_store, si.name AS name_item, si.max_price, si.min_price, si.price, si.max_count, si.count, si.place, si.step FROM stores s INNER JOIN storeitems si ON s.store_id = si.store_id ORDER BY s.store_id ASC")){
                    Stores store;
                    while(rs.next()) {
                        String storeName = rs.getString("name_store");
                        if (smartStore.stores.containsKey(storeName)){

                            store = smartStore.stores.get(storeName);
                        }
                        else {
                            store = new Stores(4, storeName, 2, UUID.fromString(rs.getString("UUID")));
                            smartStore.stores.put(storeName, store);
                        }
                        store.items.put(rs.getInt("place"), new StoreItems(
                                rs.getString("name_item"),
                                rs.getInt("max_price"),
                                rs.getInt("min_price"),
                                rs.getInt("price"),
                                rs.getInt("max_count"),
                                rs.getInt("count"),
                                rs.getInt("step"),
                                rs.getInt("place")
                        ));
                        Material mater = Material.getMaterial(rs.getString("name_item"));
                        if (mater == null){
                            smartStore.stores.remove(storeName);
                            return;
                        }
                        store.getInventory().setItem(rs.getInt("place"), new ItemStack(mater));
                    }
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void updateSQL(String sql){
        Bukkit.getScheduler().runTaskAsynchronously(smartStore.getPlugin(), () -> {
            try(Statement stat = getConnection().createStatement()) {
                stat.executeUpdate(sql);
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void updateUUID(String name, UUID u){
        Bukkit.getScheduler().runTaskAsynchronously(smartStore.getPlugin(), () -> {
            try(PreparedStatement stat = getConnection().prepareStatement("UPDATE `stores` SET `UUID`=? WHERE `name`=?")) {
                stat.setString(1, u.toString());
                stat.setString(2, name);
                stat.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void updateCount(String name, int place, int count){
        Bukkit.getScheduler().runTaskAsynchronously(smartStore.getPlugin(), () -> {
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
    public static void updateMaxCount(String name, int place, int naxCount){
        Bukkit.getScheduler().runTaskAsynchronously(smartStore.getPlugin(), () -> {
            try(PreparedStatement stat = getConnection().prepareStatement("UPDATE `storeitems` SET `max_count`=? WHERE `place`=? AND `store_id`=(SELECT `store_id` FROM `stores` WHERE `name`=?)")) {
                stat.setInt(1, naxCount);
                stat.setInt(2, place);
                stat.setString(3, name);
                stat.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    public static void updateMaxPrice(String name, int place, int maxPrice){
        Bukkit.getScheduler().runTaskAsynchronously(smartStore.getPlugin(), () -> {
            try(PreparedStatement stat = getConnection().prepareStatement("UPDATE `storeitems` SET `max_price`=? WHERE `place`=? AND `store_id`=(SELECT `store_id` FROM `stores` WHERE `name`=?)")) {
                stat.setInt(1, maxPrice);
                stat.setInt(2, place);
                stat.setString(3, name);
                stat.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    public static void updateMinPrice(String name, int place, int minPrice){
        Bukkit.getScheduler().runTaskAsynchronously(smartStore.getPlugin(), () -> {
            try(PreparedStatement stat = getConnection().prepareStatement("UPDATE `storeitems` SET `min_price`=? WHERE `place`=? AND `store_id`=(SELECT `store_id` FROM `stores` WHERE `name`=?)")) {
                stat.setInt(1, minPrice);
                stat.setInt(2, place);
                stat.setString(3, name);
                stat.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    public static void updatePrice(String name, int place, int price){
        Bukkit.getScheduler().runTaskAsynchronously(smartStore.getPlugin(), () -> {
            try(PreparedStatement stat = getConnection().prepareStatement("UPDATE `storeitems` SET `price`=? WHERE `place`=? AND `store_id`=(SELECT `store_id` FROM `stores` WHERE `name`=?)")) {
                stat.setInt(1, price);
                stat.setInt(2, place);
                stat.setString(3, name);
                stat.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    public static void updateStep(String name, int place, int step){
        Bukkit.getScheduler().runTaskAsynchronously(smartStore.getPlugin(), () -> {
            try(PreparedStatement stat = getConnection().prepareStatement("UPDATE `storeitems` SET `step`=? WHERE `place`=? AND `store_id`=(SELECT `store_id` FROM `stores` WHERE `name`=?)")) {
                stat.setInt(1, step);
                stat.setInt(2, place);
                stat.setString(3, name);
                stat.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    public static void deleteStore(String name) {
        Bukkit.getScheduler().runTaskAsynchronously(smartStore.getPlugin(), () -> {
            try(PreparedStatement stat = getConnection().prepareStatement("DELETE FROM `stores` WHERE `name`=?")) {
                stat.setString(1, name);
                stat.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


}
