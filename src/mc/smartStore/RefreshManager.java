package mc.smartStore;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class RefreshManager {
    private BukkitTask Timer = null;
    private BukkitScheduler scheduler;
    private Configuration cfg;

    public RefreshManager(){
        cfg = SmartStore.getPlugin().getConfig();
        if (cfg.getBoolean("refresh.enable"))
            scheduler = Bukkit.getScheduler();
    }
    public void run(){
        if (cfg.getBoolean("refresh.enable") && scheduler != null)
            Timer = scheduler.runTaskTimer(SmartStore.getPlugin(), Utils::allBust, cfg.getInt("refresh.timer"), cfg.getInt("refresh.timer"));
    }
    public void stop(){
        if (scheduler != null && Timer != null)
            scheduler.cancelTask(Timer.getTaskId());
    }
    public void reload(){
        cfg = SmartStore.getPlugin().getConfig();
        stop();
        run();
    }
}
