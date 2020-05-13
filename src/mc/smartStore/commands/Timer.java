package mc.smartStore.commands;

import mc.smartStore.Message;
import mc.smartStore.SmartStore;
import org.bukkit.entity.Player;

public class Timer {
    public static void timer (String []args, Player p){
        if (p.hasPermission("ss.timer")) {
            if (args[1].equals("on")){
                SmartStore.refresh.run();
                p.sendMessage(Message.prefix + "Таймер включен");
            }
            else if (args[1].equals("off")){
                SmartStore.refresh.stop();
                p.sendMessage(Message.prefix + "Таймер выключен");
            }

        }
        else
            p.sendMessage( Message.prefix +"§cУ тебя нет прав");
    }
}
