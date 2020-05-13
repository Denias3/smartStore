package mc.smartStore.db;

import org.bukkit.Material;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class dbRequest {
    public static String createTableShops (int DB){
        if (DB == 0)
            return "CREATE TABLE IF NOT EXISTS `stores` (" +
                    "`store_id` INT(8) UNSIGNED NOT NULL AUTO_INCREMENT ," +
                    "`name` VARCHAR(20) NOT NULL," +
                    "`UUID` VARCHAR(50) NOT NULL," +
                    "`capital` DOUBLE UNSIGNED DEFAULT 0.00,"+
                    "PRIMARY KEY (`store_id`)" +
                    ") ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci";
        else if (DB == 1)
            return "CREATE TABLE IF NOT EXISTS `stores` (" +
                    "`store_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`name` VARCHAR(20) NOT NULL," +
                    "`UUID` VARCHAR(50) NOT NULL," +
                    "`capital` DOUBLE DEFAULT 0.00"+
                    ")";
        else
            return null;
    }


    public static String createTableShopItems (int DB){
        if (DB == 0)
            return "CREATE TABLE IF NOT EXISTS `storeitems` (" +
                "`item_id` INT(8) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "`store_id` INT(8) UNSIGNED NOT NULL ," +
                "`name` ENUM("+ Stream.of(Material.values())
                    .filter(Material::isItem)
                    .map(m -> '\''+m.name()+'\'')
                    .collect(Collectors.joining(", ")) +") NOT NULL ," +
                "`max_price` DOUBLE UNSIGNED NOT NULL ," +
                "`min_price` DOUBLE UNSIGNED NOT NULL ," +
                "`price` DOUBLE UNSIGNED NOT NULL ," +
                "`max_count` INT(8) UNSIGNED NOT NULL ," +
                "`count` INT(8) UNSIGNED NOT NULL ," +
                "`step` INT(8) UNSIGNED NOT NULL ," +
                "`place` INT(8) UNSIGNED NOT NULL ," +
                "FOREIGN KEY (store_id) REFERENCES stores (store_id)" +
                "ON DELETE CASCADE" +
                ") ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci";
        else if (DB == 1)
            return "CREATE TABLE IF NOT EXISTS `storeitems` (" +
                    "`item_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`store_id` INTEGER NOT NULL," +
                    "`name` VARCHAR(30) NOT NULL," +
                    "`max_price` DOUBLE NOT NULL," +
                    "`min_price` DOUBLE NOT NULL," +
                    "`price` DOUBLE NOT NULL," +
                    "`max_count` INT(8) NOT NULL," +
                    "`count` INT(8) NOT NULL," +
                    "`step` INT(8) NOT NULL," +
                    "`place` INT(8) NOT NULL," +
                    "FOREIGN KEY (store_id) REFERENCES stores (store_id)" +
                    "ON DELETE CASCADE" +
                    ")";
        else
            return null;
    }



}
