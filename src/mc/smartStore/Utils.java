package mc.smartStore;

import org.bukkit.Bukkit;

import java.util.HashMap;

public class Utils {
    public static String[] stats = new String[] {
        "maxPrice",
        "minPrice",
        "price",
        "maxCount",
        "count",
        "step",
    };

    public static void allBust(){
        for (HashMap.Entry<String, Stores> store : SmartStore.stores.entrySet()){
            if (store.getValue().getStatus() == 2)
                store.getValue().bust();
        }
        Bukkit.broadcastMessage(Message.prefix + "Магазины обновились");
    }

}
