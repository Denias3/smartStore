package mc.smartStore.commands;

import mc.smartStore.Message;
import mc.smartStore.Stores;
import mc.smartStore.SmartStore;
import mc.smartStore.utils.StatusStore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Refresh {
    public static void refresh (String []args, Player p){
        if (p.hasPermission("ss.refresh")){
            if (args.length > 1 ){
                if (SmartStore.stores.get(args[1]).getStatus() == StatusStore.SAVE){
                    SmartStore.stores.get(args[1]).bust();
                    Bukkit.broadcastMessage(Message.prefix + "Магазин "+args[1]+" обновился");
                }
                else
                    p.sendMessage(Message.prefix + "Магазин не в ррежиме продажи");
            }
            else {
                for (HashMap.Entry<String, Stores> store : SmartStore.stores.entrySet()){
                    if (store.getValue().getStatus() == StatusStore.SAVE)
                        store.getValue().bust();
                }
                Bukkit.broadcastMessage(Message.prefix + "Магазины обновились");
            }
        }
        else
            p.sendMessage( Message.prefix +"§cУ тебя нет прав");
    }
}
