package mc.smartStore.commands;

import mc.smartStore.Message;
import mc.smartStore.db.ApiDatabase;
import mc.smartStore.SmartStore;
import org.bukkit.entity.Player;

public class Delete {
    public static void delete (String []args, Player p){
        if (p.hasPermission("ss.delete")) {
            if (args.length > 1) {
                if (SmartStore.stores.containsKey(args[1])){
                    if (SmartStore.stores.get(args[1]).getStatus() == 2){
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
