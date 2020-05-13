package mc.smartStore.handlers.menuStore;

import mc.smartStore.Message;
import mc.smartStore.StoreItems;
import mc.smartStore.Stores;
import mc.smartStore.db.ApiDatabase;
import mc.smartStore.SmartStore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class InventoryStore {

    public static void inventoryStore (InventoryClickEvent e, Inventory i, Player p, Stores stores){
        int slot = e.getSlot();
        int size = i.getSize();
        if (p.hasPermission("ss.edit")) {
            if (slot == size - 8) {
                stores.toggleStatus();
                stores.updateStore(p);
            } else if (slot == size - 9) {
                if (stores.getStatus() == 1){
                    if (ApiDatabase.saveShop(stores)) {
                        p.sendMessage(Message.prefix + "Магазин " + stores.getName() + " сохранен");
                        p.updateInventory();
                    } else {
                        p.closeInventory();
                        p.sendMessage(Message.prefix + "Не удалось сохранить магазин, не все параметры заполнены или магазин пуст");
                    }
                }
                else if (stores.getStatus() == 2){
                    p.closeInventory();
                    p.sendMessage(
                    "§7------------------------------------\n"+
                    "§fОборот: §5" + String.format("%.2f", stores.getCapital())+ "§d$\n"+
                            "§fОбъем: §5" + String.format("%.2f", stores.getCurVolume())+ "§d$" +"§f/§5" +
                            String.format("%.2f", stores.getVolume())+ "§d$\n" +
                    "§5§l| §fКаждые 2 часа количество товара\n"+
                    "§5§l| §fпополняется до максимума\n"+
                    "§5§l| §fи цена меняется в зависимости\n"+
                    "§5§l| §fот того какое количество отсалось.\n"+
                    "§5§l| §fКоличество общее для всех.\n"+
                    "§5§l| §fЕсли продали больше 70% то\n"+
                    "§5§l| §fцена уменьшается на шаг.\n"+
                    "§5§l| §fЕсли продали меньше 30% то\n"+
                    "§5§l| §fцена увеличивается на шаг.\n"+
                    "§5§l| §fЕсли продали в районе от \n"+
                    "§5§l| §f30% до 70% то цена не меняется.\n"+
                    "§7------------------------------------"
                    );
                }
            } else if (slot == size - 1) {
                if (stores.getStatus() != 2) {
                    p.closeInventory();
                    SmartStore.stores.remove(stores.getName());
                    p.sendMessage(Message.prefix + "Магазин " + stores.getName() + " удалён");
                } else {
                    ApiDatabase.deleteStore(stores.getName());
                    stores.setStatus(1);
                    stores.createMenuEdit();
                    p.updateInventory();
                }
            }
            if (slot >= size - 9 | stores.getStatus() != 0) {
                e.setCancelled(true);
            }
            if (stores.items.containsKey(slot)) {
                if (stores.getStatus() == 2) {
                    if (e.isRightClick() && e.isShiftClick()){
                        p.closeInventory();
                        stores.updateStats(p, slot);
                    }
                    else if ( e.isShiftClick() && e.isLeftClick()){
                        stores.sellAll(p, stores.items.get(slot));
                    }
                    else if(e.isLeftClick()){
                        stores.sellOne(p, stores.items.get(slot));
                    }
                } else if (stores.getStatus() == 1) {
                    p.closeInventory();
                    stores.updateStats(p, slot);
                }
                else {
                    if (stores.temp == -1){
                        stores.temp = slot;
                        if (e.isLeftClick() && e.isShiftClick()){
                            stores.items.remove(slot);
                            e.getCurrentItem().setLore(new ArrayList<>());
                            stores.temp = -1;
                        }
                    }
                    else{
                        e.setCancelled(true);
                    }

                }
            } else {
                if (e.getCursor() != null){
                    if(!e.getCursor().getType().equals(Material.AIR) &&
                            e.getCurrentItem() ==null &&
                            stores.temp != -1){
                        StoreItems temp = stores.items.remove(stores.temp);
                        temp.setPlace(slot);
                        stores.items.put(slot, temp);
                        stores.temp = -1;
                        stores.updateStore();
                    }
                }
            }
        }
        else{
            e.setCancelled(true);
            if (stores.items.containsKey(slot)) {
                if (stores.getStatus() == 2) {
                    if ( e.isShiftClick() && e.isLeftClick()){
                        stores.sellAll(p, stores.items.get(slot));
                    }
                    else if(e.isLeftClick()){
                        stores.sellOne(p, stores.items.get(slot));
                    }
                    else if (e.isRightClick() && e.isShiftClick()){
                        p.closeInventory();
                        stores.showStats(p, slot);
                    }
                }
            }
        }
    }
}
