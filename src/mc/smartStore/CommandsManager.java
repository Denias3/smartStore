package mc.smartStore;

import mc.smartStore.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cnd, String s, String[] args) {
        if (args.length > 0 && sender instanceof Player){
            if (args[0].equals("create")) {
                Create.create(args, (Player)sender);
            }
            else if (args[0].equals("open")) {
                Open.open(args, (Player)sender);
            }
            else if (args[0].equals("delete")) {
                Delete.delete(args, (Player) sender);
            }
            else if (args[0].equals("capital")) {
                Capital.capital(args, (Player) sender);
            }
            else if (args[0].equals("price")) {
                Price.price(args, (Player) sender);
            }
            else if (args[0].equals("count")) {
                Count.count(args, (Player) sender);
            }
            else if (args[0].equals("list")) {
                List.list((Player) sender);
            }
//            else if (args[0].equals("change")) {
//                if (sender.hasPermission("ss.change") && sender.hasPermission("ss.edit")) {
//                    if (args.length > 2){
//                        Change chan = new Change(args, (Player)sender);
//                        if (!chan.parse()){
//                            chan.printError();
//                            return true;
//                        }
//                        if (!chan.execute()){
//                            chan.printError();
//                            return true;
//                        }
//                        sender.sendMessage(Message.prefix + "Успех!");
//                    } else sender.sendMessage(Message.prefix +"/st change [n:] [id:] <mp:> <mip:> <p:> <mc:> <c:> <s:>");
//                } else sender.sendMessage( Message.prefix +"§cУ тебя нет прав");
//            }
            else if (args[0].equals("join")) {
                Join.join(args, (Player) sender);
            }
            else if (args[0].equals("reload")) {
              Reload.reload((Player) sender);
            }
        }
        else
        {
            if (sender instanceof Player)
                sender.sendMessage(Message.prefix +
                    "\n/st create [name]\n"+
                    "/st open [name]\n"+
                    "/st delete [name]\n"+
                    "/st list");
            else
                Message.toConsole("Эту команду нельзя выполнить в консоли");
        }

        return true;
    }
}