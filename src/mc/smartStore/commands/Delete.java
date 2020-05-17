package mc.smartStore.commands;

import mc.smartStore.Message;
import mc.smartStore.db.ApiDatabase;
import mc.smartStore.SmartStore;
import mc.smartStore.utils.StatusStore;
import org.bukkit.entity.Player;

public class Delete {
    public static void delete (String []args, Player p){
        if (p.hasPermission("ss.delete") || p.hasPermission("ss.edit")) {
            if (args.length > 1) {
                if (SmartStore.stores.containsKey(args[1])){
                    if (SmartStore.stores.get(args[1]).getStatus() == StatusStore.SAVE){
                        ApiDatabase.deleteStore(args[1]);
                    }
                    SmartStore.stores.remove(args[1]);
                    p.sendMessage(Message.prefix +"Магазин "+args[1]+" удалён");
                }
                else
                    p.sendMessage(Message.prefix +"Этот магазин не существует");
            }
            else
                p.sendMessage(Message.prefix +"/st delete [name]");
        }
        else
            p.sendMessage( Message.prefix +"§cУ тебя нет прав");
    }
}
