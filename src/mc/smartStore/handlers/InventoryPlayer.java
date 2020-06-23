package mc.smartStore.handlers;

import mc.smartStore.Message;
import mc.smartStore.StoreItems;
import mc.smartStore.Stores;
import mc.smartStore.utils.StatusStore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryPlayer {
    public static void inventoryPlayer(InventoryClickEvent e, Stores stores){
        Player p =  (Player) e.getView().getPlayer();
        if ( stores.getStatus() != StatusStore.MOVE) {
            if (e.isShiftClick()){
                e.setCancelled(true);
                if (stores.getStatus() != StatusStore.SAVE)
                    return;
                PlayerInventory invP =  p.getInventory();
                if (e.isLeftClick()){
                    int slot = e.getSlot();
                    ItemStack temp = invP.getItem(slot);
                    if (temp == null)
                        return;
                    Material tempM = temp.getType();
                    for (HashMap.Entry<Integer, StoreItems> item : stores.items.entrySet()){
                        if (item.getValue().getItem().getType() == tempM){
                            stores.sellMore(p, item.getValue(), stores, temp, slot);
                            return;
                        }
                    }
                }
            }

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
