package mc.smartStore;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import mc.smartStore.handlers.menuStore.InventoryPlayer;
import mc.smartStore.handlers.TabComplete.TabChange;
import mc.smartStore.handlers.menuStore.InventoryStore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class HandlerManager implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void shopClick(InventoryClickEvent e) {
        Inventory i = e.getInventory();
        Player p =  (Player) e.getView().getPlayer();
        if (i.getHolder() instanceof Stores) {
            Stores stores = (Stores)i.getHolder();
            if (i.equals(e.getClickedInventory())) {
                InventoryStore.inventoryStore(e, i, p, stores);
            } else {
                InventoryPlayer.inventoryPlayer(e, stores);
            }
        }
    }
    @EventHandler
    public void shopDrag(InventoryDragEvent e) {
        Inventory de = e.getInventory();
        if (de.getHolder() instanceof Stores) {
            Stores stores = (Stores)de.getHolder();
//            if (stores.getStatus() != 0) {
                e.setCancelled(true);
//            }
        }
    }

    @EventHandler
    public void chat(AsyncTabCompleteEvent e) {
        List<String> comp = new ArrayList<>();
        if(!(e.getSender() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getSender();
        if (e.getBuffer().startsWith("/st")){
            if ("/st create".startsWith(e.getBuffer()))
                    comp.add("create");
            if ("/st open ".startsWith(e.getBuffer()) && p.hasPermission("ss.open")){
                if (e.getBuffer().equals("/st open ")){
                    Set<String> keys = SmartStore.stores.keySet();
                    comp.addAll(keys);
                }
                else
                    comp.add("open");
            }
            if ("/st delete ".startsWith(e.getBuffer()) && p.hasPermission("ss.delete")){
                if (e.getBuffer().equals("/st delete ")){
                    Set<String> keys = SmartStore.stores.keySet();
                    comp.addAll(keys);
                }
                else
                    comp.add("delete");
            }
            if ("/st list".startsWith(e.getBuffer()) && p.hasPermission("ss.list")){
                comp.add("list");
            }
//            if ("/st reload".startsWith(e.getBuffer()) && p.hasPermission("ss.reload")){
//                comp.add("reload");
//            }
            if ("/st refresh ".startsWith(e.getBuffer()) && p.hasPermission("ss.refresh")){
                if (e.getBuffer().equals("/st refresh ")){
                    Set<String> keys = SmartStore.stores.keySet();
                    comp.addAll(keys);
                }
                else
                    comp.add("refresh");
            }
            if ("/st join ".startsWith(e.getBuffer()) && p.hasPermission("ss.join")){
                if (e.getBuffer().equals("/st join ")){
                    Set<String> keys = SmartStore.stores.keySet();
                    comp.addAll(keys);
                }
                else
                    comp.add("join");
            }
            TabChange.tabChange(e, comp);
            e.setCompletions(comp);
        }
    }
    @EventHandler
    public void entityEvent(PlayerInteractEntityEvent e) {
        for (HashMap.Entry<String, Stores> store : SmartStore.stores.entrySet()) {
            if (e.getRightClicked().getUniqueId().equals(store.getValue().getU())){
                e.setCancelled(true);
                Player p = e.getPlayer();
                if (store.getValue().getStatus() != 2)
                {
                    if (p.hasPermission("ss.edit"))
                        store.getValue().openStore(p);
                    else
                        p.sendMessage("Вы не можете открыть магазин который редактируется");
                }
                else
                    store.getValue().openStore(p);
            }
        }


    }
}
