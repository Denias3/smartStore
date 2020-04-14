package russian.denis;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class smartStore extends JavaPlugin {

    public Inventory [] stores;
    public String [][] items_stores = {
            {"APPLE"},
            {"COAL","IRON_INGOT","GOLD_INGOT"},
            {"COAL","DIAMOND"}
    };
    @Override
    public void onEnable() {
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists())
        {
            getLogger().info("Файл config.yml создался");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();;
        }
        File fileProduct = new File(getDataFolder() + File.separator + "product.yml");
        if (!fileProduct.exists())
        {
            saveResource("product.yml", false);
            getLogger().info("Файл product.yml создался");
        }
        Utils.init(this);
        Products.init(this);
        stores = Utils.createStores(items_stores, new String[]{"Тест1", "Тест2", "Тест3"});
        Bukkit.getPluginManager().registerEvents(new Handler(this), this);
        getCommand("shop").setExecutor(new Commands(this));
        economyManager.init();
    }

    @Override
    public void onDisable(){

    }
    public static void sendMessageToConsole(String msg){
        Bukkit.getConsoleSender().sendMessage("§e"+msg);
    }
}
