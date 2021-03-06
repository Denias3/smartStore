package mc.smartStore.commands;

import mc.smartStore.Message;
import mc.smartStore.db.ApiDatabase;
import mc.smartStore.SmartStore;
import mc.smartStore.utils.StatusStore;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import static mc.smartStore.commands.Create.setProfVillager;
import static mc.smartStore.commands.Create.setTypeVillager;

public class Join {
    public static void join (String []args, Player p){
        if (p.hasPermission("ss.join") || p.hasPermission("ss.edit")){
            if (args.length > 1){
                if (SmartStore.stores.containsKey(args[1])){
//                            SmartStore.stores.get(args[1]).print();
                    Entity e = p.getTargetEntity(5);
                    if (e != null){
                        e.setCustomName(args[1]);
                        if (e instanceof Villager){
                            Villager v = (Villager)e;
                            v.setAI(false);
                            if (args.length > 3)

                                setTypeVillager(v, args[2]);
                            if (args.length > 4)
                                setProfVillager(v, args[3]);
                            v.setInvulnerable(true);
                        }
                        else {
                            e.setInvulnerable(true);
                            e.setGravity(false);
                            e.setCustomName(args[1]);
                        }
                        SmartStore.stores.get(args[1]).setU(e.getUniqueId());
                        if (SmartStore.stores.get(args[1]).getStatus() == StatusStore.SAVE)
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
