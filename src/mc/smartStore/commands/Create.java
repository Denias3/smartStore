package mc.smartStore.commands;

import mc.smartStore.Message;
import mc.smartStore.Stores;
import mc.smartStore.SmartStore;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.UUID;

public class Create {

    public static void create (String []args, Player p){
        if (p.hasPermission("ss.create") && p.hasPermission("ss.edit")) {
            if (args.length > 1) {
                if (args[1].length() <= 3 || args[1].length() > 20){
                    p.sendMessage(Message.prefix +"Название магазина должно быть больше чем 3 символа и меньше чем 20");
                    return ;
                }
                else if (!args[1].matches("[a-zA-Zа-яА-Я0-9_]+")){
                    p.sendMessage(Message.prefix +"Не правильное название магазина");
                    return ;
                }
                if (!SmartStore.stores.containsKey(args[1])){
                    Location loc = p.getLocation();
                    Villager v = (Villager) p.getWorld().spawnEntity(loc, EntityType.VILLAGER);
                    v.setInvulnerable(true);
                    v.setGravity(false);
                    UUID u = v.getUniqueId();
                    SmartStore.stores.put(args[1], new Stores(4, args[1], p, u));
                }
                else
                    p.sendMessage(Message.prefix +"Этот магазин уже существует");
            }
            else
                p.sendMessage(Message.prefix +"/st create [name]");
        }
        else
            p.sendMessage( Message.prefix +"§cУ тебя нет прав");
    }

}
