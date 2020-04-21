package mc.smartStore;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class smartStore extends JavaPlugin {

    public static FileConfiguration languages;
    public static HashMap<String, stores> stores = new HashMap<>();
    @Override
    public void onEnable() {
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists())
        {
            getLogger().info("Файл config.yml создался");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();;
        }
        File fileLanguages = new File(getDataFolder() + File.separator + "languages.yml");
        if (!fileLanguages.exists())
        {
            saveResource("languages.yml", false);
            getLogger().info("Файл languages.yml создался");
        }
        languages = YamlConfiguration.loadConfiguration(fileLanguages);
//        database.openConnectionMySQL();
//        database.createTable();
        Utils.init(this);
        Products.init(this);
        Bukkit.getPluginManager().registerEvents(new Handler(this), this);
        getCommand("st").setExecutor(new Commands(this));
        economyManager.init();

    }

    @Override
    public void onDisable(){
        database.close();
    }
    public static smartStore getPlugin() {
        return smartStore.getPlugin(smartStore.class);
    }
}
