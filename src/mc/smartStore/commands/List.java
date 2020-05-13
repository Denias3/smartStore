package mc.smartStore.commands;

import mc.smartStore.Message;
import mc.smartStore.SmartStore;
import org.bukkit.entity.Player;

import java.util.Set;

public class List {
    public static void list (Player p){
        if (p.hasPermission("ss.list")) {
            Set<String> keys = SmartStore.stores.keySet();
            String list = String.join(", ", keys);
            p.sendMessage(list);
        }
        else
            p.sendMessage( Message.prefix +"§cУ тебя нет прав");
    }
}
