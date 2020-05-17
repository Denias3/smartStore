package mc.smartStore.commands;

import mc.smartStore.Message;
import mc.smartStore.SmartStore;
import mc.smartStore.utils.StatusStore;
import org.bukkit.entity.Player;

public class Open {
    public static void open (String []args, Player p){
        if (p.hasPermission("ss.open")) {
            if (args.length > 1) {
                if (SmartStore.stores.containsKey(args[1])){
                    if (SmartStore.stores.get(args[1]).getStatus() == StatusStore.SAVE)
                        SmartStore.stores.get(args[1]).openStore(p);
                    else if (p.hasPermission("ss.edit")){
                        SmartStore.stores.get(args[1]).openStore(p);
                    }
                    else
                        p.sendMessage("Вы не можете открыть магазин который редактируется");
                }
                else
                    p.sendMessage(Message.prefix +"Этот магазин не существует");
            }
            else
                p.sendMessage(Message.prefix +"/st open [name]");
        }
        else
            p.sendMessage( Message.prefix +"§cУ тебя нет прав");
    }
}
