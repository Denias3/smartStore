package mc.smartStore;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.scheduler.BukkitTask;

public class RefreshManager {
    private BukkitTask Timer;
    private Configuration cfg;

    public RefreshManager(){
        cfg = smartStore.getPlugin().getConfig();
        if (cfg.getBoolean("refresh.enable"))
            Timer = Bukkit.getScheduler().runTaskTimer(smartStore.getPlugin(), Utils::allBust, cfg.getInt("refresh.timer"), cfg.getInt("refresh.timer"));
    }
    public void run(){
        if (cfg.getBoolean("refresh.enable"))
            Timer = Bukkit.getScheduler().runTaskTimer(smartStore.getPlugin(), Utils::allBust, cfg.getInt("refresh.timer"), cfg.getInt("refresh.timer"));
    }
    public void stop(){
        Timer.cancel();
    }
}
