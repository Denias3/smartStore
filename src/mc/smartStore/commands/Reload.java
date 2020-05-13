package mc.smartStore.commands;

import mc.smartStore.Message;
import mc.smartStore.db.ApiDatabase;
import mc.smartStore.SmartStore;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class Reload {
    public static void reload (Player p){
        if (p.hasPermission("ss.reload")) {
            SmartStore.getPlugin().reloadConfig();
            try {
                ApiDatabase.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ApiDatabase.init();
            SmartStore.refresh.reload();
            p.sendMessage(Message.prefix + "Ты обновил конфиг");
        }
        else
            p.sendMessage( Message.prefix +"§cУ тебя нет прав");
    }
}
