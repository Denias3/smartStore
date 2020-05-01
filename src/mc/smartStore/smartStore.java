package mc.smartStore;

import mc.smartStore.db.ApiDatabase;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

public class smartStore extends JavaPlugin {

    public static FileConfiguration languages;
    public static HashMap<String, Stores> stores = new HashMap<>();
    public static RefreshManager refresh;

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
        Bukkit.getPluginManager().registerEvents(new HandlerManager(this), this);
        getCommand("st").setExecutor(new CommandsManager(this));
        EconomyManager.init();
        ApiDatabase.init();
        refresh = new RefreshManager();
    }

    @Override
    public void onDisable(){
        try {
            ApiDatabase.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static smartStore getPlugin() {
        return smartStore.getPlugin(smartStore.class);
    }
}
