package mc.smartStore;

import mc.smartStore.commands.Change;
import mc.smartStore.db.ApiDatabase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class CommandsManager implements CommandExecutor {

    private final smartStore plugin;

    public CommandsManager(smartStore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cnd, String s, String[] args) {
        if (args.length > 0){
            if (args[0].equals("create")) {
                if (sender.hasPermission("ss.create") && sender.hasPermission("ss.edit")) {
                    if (args.length > 1) {
                        Player p = (Player) sender;
                        if (!smartStore.stores.containsKey(args[1])){
                            Location loc = p.getLocation();
                            Villager v = (Villager) p.getWorld().spawnEntity(loc, EntityType.VILLAGER);
                            v.setInvulnerable(true);
                            v.setGravity(false);
                            UUID u = v.getUniqueId();
                            smartStore.stores.put(args[1], new Stores(4, args[1], p, u));
                        }
                        else
                            p.sendMessage(Message.prefix +"Этот магазин уже существует");
                    }
                    else
                        sender.sendMessage(Message.prefix +"/st create [name]");
                }
                else
                    sender.sendMessage( Message.prefix +"§cУ тебя нет прав");
            }
            else if (args[0].equals("open")) {
                if (sender.hasPermission("ss.open")) {
                    if (args.length > 1) {
                        Player p = (Player) sender;

                        if (smartStore.stores.containsKey(args[1])){
                            if (smartStore.stores.get(args[1]).getStatus() == 2)
                                smartStore.stores.get(args[1]).openStore(p);
                            else if (sender.hasPermission("ss.edit")){
                                smartStore.stores.get(args[1]).openStore(p);
                            }
                            else
                                sender.sendMessage("Вы не можете открыть магазин который редактируется");
                        }
                        else
                            p.sendMessage(Message.prefix +"Этот магазин не существует");
                    }
                    else
                        sender.sendMessage(Message.prefix +"/st open [name]");
                }
                else
                    sender.sendMessage( Message.prefix +"§cУ тебя нет прав");
            }
            else if (args[0].equals("delete")) {
                if (sender.hasPermission("ss.delete")) {
                    if (args.length > 1) {
                        Player p = (Player) sender;

                        if (smartStore.stores.containsKey(args[1])){
                            if (smartStore.stores.get(args[1]).getStatus() == 2){
                                ApiDatabase.deleteStore(args[1]);
                            }
                            smartStore.stores.remove(args[1]);
                            p.sendMessage(Message.prefix +"Магазин "+args[1]+" удалён");
                        }
                        else
                            p.sendMessage(Message.prefix +"Этот магазин не существует");
                    }
                    else
                        sender.sendMessage(Message.prefix +"/st delete [name]");
                }
                else
                    sender.sendMessage( Message.prefix +"§cУ тебя нет прав");
            }
            else if (args[0].equals("list")) {
                if (sender.hasPermission("ss.list")) {
                    Set<String> keys = smartStore.stores.keySet();
                    String list = String.join(", ", keys);
                    sender.sendMessage(list);
                }
                else
                    sender.sendMessage( Message.prefix +"§cУ тебя нет прав");
            }
            else if (args[0].equals("change")) {
                if (sender.hasPermission("ss.change") && sender.hasPermission("ss.edit")) {
                    if (args.length > 2){


                        Change chan = new Change(args, (Player)sender);
                        if (!chan.parse()){
                            chan.printError();
                            return true;
                        }
                        if (!chan.execute()){
                            chan.printError();
                            return true;
                        }
                        sender.sendMessage(Message.prefix + "Успех!");
                    }
                    else
                        sender.sendMessage(Message.prefix +"/st change [n:] [id:] <mp:> <mip:> <p:> <mc:> <c:> <s:>");
                }
                else
                    sender.sendMessage( Message.prefix +"§cУ тебя нет прав");
            }
            else if (args[0].equals("refresh")) {
                if (sender.hasPermission("ss.refresh")){
                    if (args.length > 1 ){
                        if (smartStore.stores.get(args[1]).getStatus() == 2){
                            smartStore.stores.get(args[1]).bust();
                            Bukkit.broadcastMessage(Message.prefix + "Магазин "+args[1]+" обновился");
                        }
                    }
                    else {
                        for (HashMap.Entry<String, Stores> store : smartStore.stores.entrySet()){
                            if (store.getValue().getStatus() == 2)
                                store.getValue().bust();
                        }
                        Bukkit.broadcastMessage(Message.prefix + "Магазины обновились");
                    }
                }
                else
                    sender.sendMessage( Message.prefix +"§cУ тебя нет прав");

            }
            else if (args[0].equals("join")) {
                if (sender.hasPermission("ss.join")){
                    if (args.length > 1){
                        Player p = (Player)sender;
                        if (smartStore.stores.containsKey(args[1])){
                            smartStore.stores.get(args[1]).print();
                            Entity e = p.getTargetEntity(5);
                            if (e != null){
                                smartStore.stores.get(args[1]).setU(e.getUniqueId());
                                e.setInvulnerable(true);
                                e.setGravity(false);
                                if (smartStore.stores.get(args[1]).getStatus() == 2)
                                    ApiDatabase.updateUUID(args[1], e.getUniqueId());
                                p.sendMessage("Привязка произошла");
                            }
                            else
                                p.sendMessage("Сущность не найдена");
                        }

                    }

                }
                else
                    sender.sendMessage( Message.prefix +"§cУ тебя нет прав");

            }
            else if (args[0].equals("reload")) {
                if (sender.hasPermission("ss.reload")) {
                   plugin.reloadConfig();
                    try {
                        ApiDatabase.getConnection().close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    ApiDatabase.init();
                    smartStore.refresh.stop();
                    smartStore.refresh.run();
                    sender.sendMessage(Message.prefix + "Ты обновил конфиг");
                }
                else
                    sender.sendMessage( Message.prefix +"§cУ тебя нет прав");
            }
        }
        else
        {
            sender.sendMessage(Message.prefix +
                    "\n/st create [name]\n"+
                    "/st open [name]\n"+
                    "/st delete [name]\n"+
                    "/st list");
        }

        return true;
    }
}