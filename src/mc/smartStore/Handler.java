package mc.smartStore;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Handler implements Listener {
    private final smartStore plugin;

    public Handler(smartStore plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void shopClick(InventoryClickEvent e) {
        Inventory i = e.getInventory();
        Player p =  (Player) e.getView().getPlayer();
        for (HashMap.Entry<String, stores> item : smartStore.stores.entrySet()){
            Inventory s = item.getValue().getInv();
            if(i.equals(s)) {
                if (i.equals(e.getClickedInventory())) {
                    int slot = e.getSlot();
                    int size = s.getSize();
                    if (slot == size - 8) {
                        item.getValue().toggleStatus();
                        item.getValue().updateStore(p);
                    }
                    if (slot >= size - 9 | item.getValue().getStatus() == 1){

                        e.setCancelled(true);
                    }

                }
                else {
                    if (e.isShiftClick() && item.getValue().getStatus() == 1){
                        e.setCancelled(true);
                    }

                }

            }
        }

    }
    @EventHandler
    public void shopDrag(InventoryDragEvent e) {
        Inventory de = e.getInventory();
        for (HashMap.Entry<String, stores> item : smartStore.stores.entrySet()) {

            Inventory s = item.getValue().getInv();
            if (de.equals(s) & item.getValue().getStatus() == 1) {
                e.setCancelled(true);
            }

        }

    }
    @EventHandler
    public void chat(AsyncTabCompleteEvent e) {
        List<String> comp = new ArrayList<>();
        if (e.getBuffer().startsWith("/st")){

            if ("/st create".startsWith(e.getBuffer())){
                    comp.add("create");
            }
            if ("/st open ".startsWith(e.getBuffer())){
                if (e.getBuffer().equals("/st open ")){
                    Set<String> keys = smartStore.stores.keySet();
                    comp.addAll(keys);
                }
                else
                    comp.add("open");

            }
            if ("/st delete ".startsWith(e.getBuffer())){
                if (e.getBuffer().equals("/st delete ")){
                    Set<String> keys = smartStore.stores.keySet();
                    comp.addAll(keys);
                }
                else
                    comp.add("delete");

            }
            if ("/st list".startsWith(e.getBuffer())){
                comp.add("list");
            }
            e.setCompletions(comp);
        }


    }

}
