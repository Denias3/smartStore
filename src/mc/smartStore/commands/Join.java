package mc.smartStore.commands;

import mc.smartStore.Message;
import mc.smartStore.db.ApiDatabase;
import mc.smartStore.SmartStore;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Join {
    public static void join (String []args, Player p){
        if (p.hasPermission("ss.join")){
            if (args.length > 1){
                if (SmartStore.stores.containsKey(args[1])){
//                            smartStore.stores.get(args[1]).print();
                    Entity e = p.getTargetEntity(5);
                    if (e != null){
                        SmartStore.stores.get(args[1]).setU(e.getUniqueId());
                        e.setInvulnerable(true);
                        e.setGravity(false);
                        if (SmartStore.stores.get(args[1]).getStatus() == 2)
                            ApiDatabase.updateUUID(args[1], e.getUniqueId());
                        p.sendMessage("Привязка произошла");
                    }
                    else
                        p.sendMessage("Сущность не найдена");
                }
            }
        }
        else
            p.sendMessage( Message.prefix +"§cУ тебя нет прав");
    }

}
