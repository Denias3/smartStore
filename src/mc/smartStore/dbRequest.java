package mc.smartStore;

import org.bukkit.Material;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class dbRequest {
    public static String createTableShops (int DB){
        if (DB == 0)
            return "CREATE TABLE IF NOT EXISTS `stores` (" +
                    "`store_id` INT(8) UNSIGNED NOT NULL ," +
                    "`name` VARCHAR(20) NOT NULL ," +
                    "PRIMARY KEY (`store_id`)" +
                    ") ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci";
        else if (DB == 1)
            return null;
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
                "`max_price` INT(8) UNSIGNED NOT NULL ," +
                "`min_price` INT(8) UNSIGNED NOT NULL ," +
                "`price` INT(8) UNSIGNED NOT NULL ," +
                "`max_count` INT(8) UNSIGNED NOT NULL ," +
                "`count` INT(8) UNSIGNED NOT NULL ," +
                "`step` INT(8) UNSIGNED NOT NULL ," +
                "`place` INT(8) UNSIGNED NOT NULL ," +
                "FOREIGN KEY (store_id) REFERENCES stores (store_id)" +
                ") ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci";
        else if (DB == 1)
            return null;
        else
            return null;
    }



}
