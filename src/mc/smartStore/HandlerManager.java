package mc.smartStore;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import mc.smartStore.db.ApiDatabase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HandlerManager implements Listener {
    private final smartStore plugin;

    public HandlerManager(smartStore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void shopClick(InventoryClickEvent e) {
        Inventory i = e.getInventory();
        Player p =  (Player) e.getView().getPlayer();
        if (i.getHolder() instanceof Stores) {
            Stores stores = (Stores)i.getHolder();
            if (i.equals(e.getClickedInventory())) {
                int slot = e.getSlot();
                int size = i.getSize();
                if (p.hasPermission("ss.edit")) {
                    if (slot == size - 8) {
                        stores.toggleStatus();
                        stores.updateStore(p);
                    } else if (slot == size - 9) {
                        if (ApiDatabase.saveShop(stores)) {
                            p.sendMessage(Message.prefix + "Магазин " + stores.getName() + " сохранен");
                            p.updateInventory();
                        } else {
                            p.closeInventory();
                            p.sendMessage(Message.prefix + "Не удалось сохранить магазин, не все параметры заполнены или магазин пуст");
                        }
                    } else if (slot == size - 1) {
                        if (stores.getStatus() != 2) {
                            p.closeInventory();
                            smartStore.stores.remove(stores.getName());
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
                        }
                    }
                }

            } else {
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
                    Set<String> keys = smartStore.stores.keySet();
                    comp.addAll(keys);
                }
                else
                    comp.add("open");
            }
            if ("/st delete ".startsWith(e.getBuffer()) && p.hasPermission("ss.delete")){
                if (e.getBuffer().equals("/st delete ")){
                    Set<String> keys = smartStore.stores.keySet();
                    comp.addAll(keys);
                }
                else
                    comp.add("delete");
            }
            if ("/st list".startsWith(e.getBuffer()) && p.hasPermission("ss.list")){
                comp.add("list");
            }
            if ("/st reload".startsWith(e.getBuffer()) && p.hasPermission("ss.reload")){
                comp.add("reload");
            }
            if ("/st refresh ".startsWith(e.getBuffer()) && p.hasPermission("ss.refresh")){
                if (e.getBuffer().equals("/st refresh ")){
                    Set<String> keys = smartStore.stores.keySet();
                    comp.addAll(keys);
                }
                else
                    comp.add("refresh");
            }
            if ("/st join ".startsWith(e.getBuffer()) && p.hasPermission("ss.join")){
                if (e.getBuffer().equals("/st join ")){
                    Set<String> keys = smartStore.stores.keySet();
                    comp.addAll(keys);
                }
                else
                    comp.add("join");
            }

            if ("/st change ".startsWith(e.getBuffer())){
                List<String> changeParam = new ArrayList<>();
                changeParam.add("n:");
                changeParam.add("id:");
                changeParam.add("mp:");
                changeParam.add("mip:");
                changeParam.add("p:");
                changeParam.add("mc:");
                changeParam.add("c:");
                changeParam.add("s:");
                if (e.getBuffer().equals("/st change ")){
                    comp.addAll(changeParam);
                }
                else
                    comp.add("change");
            }
            else if (e.getBuffer().startsWith("/st change ")) {
                List<String> changeParam = new ArrayList<>();
                changeParam.add("n:");
                changeParam.add("id:");
                changeParam.add("mp:");
                changeParam.add("mip:");
                changeParam.add("p:");
                changeParam.add("mc:");
                changeParam.add("c:");
                changeParam.add("s:");
                if (e.getBuffer().matches(".+\\s[a-z]{0,3}$")){

                    String param = null;

                    Pattern pattern1 = Pattern.compile("\\s[a-z]{0,3}$");
                    Matcher matcher1 = pattern1.matcher(e.getBuffer());
                    if (matcher1.find())
                        param = matcher1.group().substring(1);
                    List<String> useParams = new ArrayList<>();
                    Pattern pattern = Pattern.compile("\\s[a-z]+:");
                    Matcher matcher = pattern.matcher(e.getBuffer());
                    while (matcher.find())
                        useParams.add(matcher.group().substring(1));
                    String finalParam = param;
                    comp.addAll(changeParam.stream().filter(x ->{
                        for (String par :useParams){
                            if (x.equals(par))
                                return false;
                        }
                        return x.startsWith(finalParam);
                    }).collect(Collectors.toList()));
                }
                else {
                    if (e.getBuffer().matches(".+n:$")){
                        comp.add("n:all");
                        comp.addAll(smartStore.stores.keySet().stream().map(x -> "n:"+x).collect(Collectors.toList()));
                    }
                    else if (e.getBuffer().matches(".+id:$")){
                        comp.addAll(Stream.of("all","1", "2", "3","4","5","6","7","8","9").map(x -> "id:"+x).collect(Collectors.toList()));
                    }
                    else {
                        for (String par: changeParam){
                            if (e.getBuffer().matches(".+"+par+"$")){
                                comp.addAll(Stream.of("1", "2", "3","4","5","6","7","8","9").map(x -> par+x).collect(Collectors.toList()));
                                break;
                            }
                        }
                    }

                }

            }
            e.setCompletions(comp);
        }
    }
    @EventHandler
    public void entityEvent(PlayerInteractEntityEvent e) {
        for (HashMap.Entry<String, Stores> store : smartStore.stores.entrySet()) {
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
