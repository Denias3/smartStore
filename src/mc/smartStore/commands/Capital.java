package mc.smartStore.commands;

import mc.smartStore.Message;
import mc.smartStore.SmartStore;
import mc.smartStore.db.ApiDatabase;
import mc.smartStore.utils.StatusStore;
import org.bukkit.entity.Player;

public class Capital {
    public static void capital (String []args, Player p){
        if (p.hasPermission("ss.capital") || p.hasPermission("ss.edit")) {
            if (args.length > 2) {
                if (SmartStore.stores.containsKey(args[1])){
                    SmartStore.stores.get(args[1]).setCapital(Double.parseDouble(args[2]));
                    if (SmartStore.stores.get(args[1]).getStatus() == StatusStore.SAVE){
                        ApiDatabase.updateCapital(args[1], Double.parseDouble(args[2]));
                        SmartStore.stores.get(args[1]).createMenu();
                    }
                    p.sendMessage(Message.prefix +"Капитал магазина изменен");
                }
                else
                    p.sendMessage(Message.prefix +"Этот магазин не существует");
            }
            else
                p.sendMessage(Message.prefix +"/st capital [name] [money]");
        }
        else
            p.sendMessage( Message.prefix +"§cУ тебя нет прав");
    }
}
