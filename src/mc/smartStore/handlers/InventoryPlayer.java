package mc.smartStore.handlers;

import mc.smartStore.Stores;
import mc.smartStore.utils.StatusStore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class InventoryPlayer {
    public static void inventoryPlayer(InventoryClickEvent e, Stores stores){
        if (e.isShiftClick() && stores.getStatus() != StatusStore.MOVE) {
            e.setCancelled(true);
        }
        else{
            if (e.getCursor() != null){
                if (!e.getCursor().getType().equals(Material.AIR) && stores.temp != -1){
                    stores.items.remove(stores.temp);
                    e.getCursor().setLore(new ArrayList<>());
                    stores.temp = -1;
//                            p.updateInventory();
                }
            }
        }
    }
}
