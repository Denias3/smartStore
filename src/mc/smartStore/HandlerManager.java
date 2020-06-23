package mc.smartStore;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import mc.smartStore.handlers.InventoryPlayer;
import mc.smartStore.handlers.tabComplete.TabChange;
import mc.smartStore.handlers.InventoryStore;
import mc.smartStore.utils.StatusStore;
import mc.smartStore.utils.listVillager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class HandlerManager implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void InventoryClick(InventoryClickEvent e) {
        Inventory i = e.getInventory();
        if (i.getHolder() instanceof Stores) {
            Stores stores = (Stores)i.getHolder();
            if (i.equals(e.getClickedInventory())) {
                InventoryStore.inventoryStore(e, stores);
            } else {
                InventoryPlayer.inventoryPlayer(e, stores);
            }
        }
    }
    @EventHandler
    public void Drag(InventoryDragEvent e) {
        Inventory de = e.getInventory();
        if (de.getHolder() instanceof Stores) {
            Stores stores = (Stores)de.getHolder();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void TabComplete(AsyncTabCompleteEvent e) {
        List<String> comp = new ArrayList<>();
        if(!(e.getSender() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getSender();
        if (e.getBuffer().startsWith("/st")){
            if ("/st create".startsWith(e.getBuffer())){
                comp.add("create");
            }
            else if (e.getBuffer().startsWith("/st create ")) {
                if (e.getBuffer().matches("/st create [a-zA-Zа-яА-Я0-9_]+ ")){
                    comp.add("1");
                    comp.add("2");
                    comp.add("3");
                    comp.add("4");
                    comp.add("5");
                }
                else if (e.getBuffer().matches("/st create [a-zA-Zа-яА-Я0-9_]+ [12345]{1} ")){
                    comp.addAll(listVillager.type);
                }
                else if (e.getBuffer().matches("/st create [a-zA-Zа-яА-Я0-9_]+ [12345]{1} [а-яА-Я]+ ")){
                    comp.addAll(listVillager.prof);
                }
            }
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
            if ("/st capital ".startsWith(e.getBuffer()) && p.hasPermission("ss.capital")){
                if (e.getBuffer().equals("/st capital ")){
                    Set<String> keys = SmartStore.stores.keySet();
                    comp.addAll(keys);
                }
                else
                    comp.add("capital");
            }
            if ("/st count ".startsWith(e.getBuffer()) && p.hasPermission("ss.count")){
                if (e.getBuffer().equals("/st count ")){
                    Set<String> keys = SmartStore.stores.keySet();
                    comp.addAll(keys);
                }
                else
                    comp.add("count");
            }
            if ("/st price ".startsWith(e.getBuffer()) && p.hasPermission("ss.price")){
                if (e.getBuffer().equals("/st price ")){
                    Set<String> keys = SmartStore.stores.keySet();
                    comp.addAll(keys);
                }
                else
                    comp.add("price");
            }

            if ("/st list".startsWith(e.getBuffer()) && p.hasPermission("ss.list")){
                comp.add("list");
            }
//            if ("/st reload".startsWith(e.getBuffer()) && p.hasPermission("ss.reload")){
//                comp.add("reload");
//            }
            if ("/st join".startsWith(e.getBuffer())){
                comp.add("join");
            }
            else if (e.getBuffer().startsWith("/st join ") && p.hasPermission("ss.join")){

                if (e.getBuffer().equals("/st join ")){
                    Set<String> keys = SmartStore.stores.keySet();
                    comp.addAll(keys);
                }
                else if (e.getBuffer().matches("/st join [a-zA-Zа-яА-Я0-9_]+ ")){
                    comp.addAll(listVillager.type);
                }
                else if (e.getBuffer().matches("/st join [a-zA-Zа-яА-Я0-9_]+ [а-яА-Я]+ ")){
                    comp.addAll(listVillager.prof);
                }
            }

//            TabChange.tabChange(e, comp);
            e.setCompletions(comp);
        }
    }
    @EventHandler
    public void InteractEntity(PlayerInteractEntityEvent e) {
        for (HashMap.Entry<String, Stores> store : SmartStore.stores.entrySet()) {
            if (e.getRightClicked().getUniqueId().equals(store.getValue().getU())){
                e.setCancelled(true);
                Player p = e.getPlayer();
                if (store.getValue().getStatus() != StatusStore.SAVE)
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
    @EventHandler
    public void InventoryClose(InventoryCloseEvent e) {
        Inventory i = e.getInventory();
        if (i.getHolder() instanceof Stores) {
            Player p = (Player) e.getPlayer();
            Inventory invP = p.getInventory();
            ItemStack[] items = invP.getContents();
            for (int j = 0 ; j < items.length ; j++){
                if (items[j] != null){
                    List<String> lore = items[j].getLore();
                    if (lore != null){
                        if (lore.get(0).startsWith("§7Shift + ЛКМ")){
                            items[j].setLore(null);
                            invP.setItem(j, items[j]);
                        }

                    }
                }


            }


        }

    }
}
