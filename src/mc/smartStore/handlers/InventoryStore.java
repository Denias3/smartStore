package mc.smartStore.handlers;


import mc.smartStore.Stores;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryStore {
    public static void inventoryStore (InventoryClickEvent e, Stores stores){
        Player p =  (Player) e.getView().getPlayer();
        int slot = e.getSlot();
        if (stores.menu.containsKey(slot))
            stores.menu.get(slot).click(p, e);
        else
            e.setCancelled(true);
    }
}
