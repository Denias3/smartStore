package mc.smartStore;

import mc.smartStore.db.ApiDatabase;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

public class SmartStore extends JavaPlugin {

    public static FileConfiguration languages;
    public static HashMap<String, Stores> stores = new HashMap<>();

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
        ApiDatabase.loadAllStores();
        Bukkit.getPluginManager().registerEvents(new HandlerManager(), this);
        getCommand("st").setExecutor(new CommandsManager());
        EconomyManager.init();
        ApiDatabase.init();
    }

    @Override
    public void onDisable(){
        ApiDatabase.close();
    }
    public static SmartStore getPlugin() {
        return SmartStore.getPlugin(SmartStore.class);
    }
}
