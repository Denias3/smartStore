package mc.smartStore.handlers.menuStore;

import mc.smartStore.Stores;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class InventoryPlayer {
    public static void inventoryPlayer(InventoryClickEvent e, Stores stores){
        if (e.isShiftClick() && stores.getStatus() != 0) {
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
