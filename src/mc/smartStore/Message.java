package mc.smartStore;

import org.bukkit.Bukkit;

public class Message {
    public static String prefix = "§6[§eСкупщик§6] §f";

    public static void toConsole(String msg){
        Bukkit.getConsoleSender().sendMessage(prefix+msg);
    }
}
