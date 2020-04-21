package mc.smartStore;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Commands implements CommandExecutor {

    private final smartStore plugin;

    public Commands(smartStore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cnd, String s, String[] args) {
        if (args.length > 0){
            if (args[0].equals("create")) {
                if (sender.hasPermission("ss.create")) {
                    if (args.length > 1) {
                        Player p = (Player) sender;
                        if (!smartStore.stores.containsKey(args[1])){
                            smartStore.stores.put(args[1], new stores(4, args[1], (Player) sender));
                        }
                        else
                            p.sendMessage(msg.prefix +"Этот магазин уже существует");
                    }
                    else
                        sender.sendMessage(msg.prefix +"/st create [name]");
                }
                else
                    sender.sendMessage( msg.prefix +"§cУ тебя нет прав");
            }
            else if (args[0].equals("open")) {
                if (sender.hasPermission("ss.open")) {
                    if (args.length > 1) {
                        Player p = (Player) sender;

                        if (smartStore.stores.containsKey(args[1])){
                            smartStore.stores.get(args[1]).openStore(p);
                        }
                        else
                            p.sendMessage(msg.prefix +"Этот магазин не существует");
                    }
                    else
                        sender.sendMessage(msg.prefix +"/st open [name]");
                }
                else
                    sender.sendMessage( msg.prefix +"§cУ тебя нет прав");
            }
            else if (args[0].equals("delete")) {
                if (sender.hasPermission("ss.delete")) {
                    if (args.length > 1) {
                        Player p = (Player) sender;

                        if (smartStore.stores.containsKey(args[1])){
                            smartStore.stores.remove(args[1]);
                            p.sendMessage(msg.prefix +"Магазин "+args[1]+" удалён");
                        }
                        else
                            p.sendMessage(msg.prefix +"Этот магазин не существует");
                    }
                    else
                        sender.sendMessage(msg.prefix +"/st delete [name]");
                }
                else
                    sender.sendMessage( msg.prefix +"§cУ тебя нет прав");
            }
            else if (args[0].equals("list")) {
                if (sender.hasPermission("ss.list")) {
                    Set<String> keys = smartStore.stores.keySet();
                    String list = String.join(", ", keys);
                    sender.sendMessage(list);
                }
                else
                    sender.sendMessage( msg.prefix +"§cУ тебя нет прав");
            }
            else if (args[0].equals("change")) {
                if (sender.hasPermission("ss.change")) {
                    if (args.length > 5){
                        if (smartStore.stores.containsKey(args[1])){
                            if (args[2].equals("-n")){
                                Material item = Material.getMaterial(args[3]);
                                if (item != null)
                                    if (smartStore.stores.get(args[1]).getInv().contains(item)){

                                    }

                            }else if(args[2].equals("-i")){

                            }
                        }
                        else
                            sender.sendMessage(msg.prefix +"Этот магазин не существует");
                    }

                }
                else
                    sender.sendMessage( msg.prefix +"§cУ тебя нет прав");
            }
        }
        else
        {
            sender.sendMessage(msg.prefix +
                    "\n/st create [name]\n"+
                    "/st open [name]\n"+
                    "/st delete [name]\n"+
                    "/st list");
        }

        return true;
    }
}