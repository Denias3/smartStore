package mc.smartStore;

import org.bukkit.Bukkit;

public class msg {
    public static String prefix = "§5[§dsmartStore§5] §f";

    public static void toConsole(String msg){
        Bukkit.getConsoleSender().sendMessage(prefix+msg);
    }
}
