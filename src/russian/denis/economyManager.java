package russian.denis;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class economyManager {
    private static Economy e;

    public static void init() {
        RegisteredServiceProvider<Economy> reg = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (reg != null) e = reg.getProvider();
    }

    public static boolean takeMoney(Player p, double amount){
        if (e == null) return false;
        if (e.getBalance(p) < amount) return false;
        return e.withdrawPlayer(p, amount).transactionSuccess();
    }
    public static boolean payMoney(Player p, double amount){
        if (e == null) return false;

        return e.depositPlayer(p, amount).transactionSuccess();
    }
}
