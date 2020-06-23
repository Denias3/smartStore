package mc.smartStore.commands;

import mc.smartStore.Message;
import mc.smartStore.SmartStore;
import mc.smartStore.db.ApiDatabase;
import mc.smartStore.utils.StatusStore;
import org.bukkit.entity.Player;

public class Count {
    public static void count (String []args, Player p){
        if (p.hasPermission("ss.count") || p.hasPermission("ss.edit")) {
            if (args.length > 3) {
                if (SmartStore.stores.containsKey(args[1])){
                    if (SmartStore.stores.get(args[1]).items.containsKey(Integer.parseInt(args[2]) - 1)){
                        if (SmartStore.stores.get(args[1]).getStatus() == StatusStore.SAVE){
                            ApiDatabase.updateCount(args[1],Integer.parseInt(args[2]) - 1, Integer.parseInt(args[3]));
                        }
                        SmartStore.stores.get(args[1]).items.get(Integer.parseInt(args[2]) - 1).setCount(Integer.parseInt(args[3]));
                        p.sendMessage(Message.prefix +"Количество предметов на складе изменено");
                    }
                    else
                        p.sendMessage(Message.prefix +"Этого предмета нет в магазине");
                }
                else
                    p.sendMessage(Message.prefix +"Этот магазин не существует");
            }
            else
                p.sendMessage(Message.prefix +"/st count [name] [id] [money]");
        }
        else
            p.sendMessage( Message.prefix +"§cУ тебя нет прав");
    }
}
