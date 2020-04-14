package russian.denis;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Commands implements CommandExecutor {

    private final smartStore plugin;

    public Commands(smartStore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cnd, String s, String[] args) {
        if (args.length == 1)
        {
            if (args[0].equals("1"))
            {
                Player p = (Player) sender;
                p.openInventory(Utils.personStore(plugin.stores[0], p));

            }
            else if (args[0].equals("2"))
            {
                Player p = (Player) sender;
                p.openInventory(Utils.personStore(plugin.stores[1], p));

            }
            else if (args[0].equals("3"))
            {
                Player p = (Player) sender;
                p.openInventory(Utils.personStore(plugin.stores[2], p));

            }
            else if (args[0].equals("bust"))
            {
                if (sender.hasPermission("ss.bust"))
                {
                    for (int j = 0; j < plugin.stores.length;j++)
                        Products.bust(plugin.items_stores[j]);
                    Bukkit.broadcastMessage("Магазин обновился");
                }
                else
                    sender.sendMessage( "§cУ тебя нет прав");
            }

        }
        return true;
    }
}