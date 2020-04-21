package mc.smartStore;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class stores {


    private String name;
    private Inventory inv;
    private int status = 0;
    public HashMap<Integer, storeItems> items = new HashMap<>();

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
    public Inventory getInv() {
        return inv;
    }

    private void createMenu(){
        int size = inv.getSize();

        for (int i = 8; i >= 0;i--) {
            size--;
            if (i == 0){
                ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(smartStore.languages.getString("menu.save"));
                item.setItemMeta(meta);
                inv.setItem(size, item);
            }
            else if (i == 1) {
                ItemStack item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(smartStore.languages.getString("menu.move"));
                item.setItemMeta(meta);
                inv.setItem(size, item);
            }
            else if (i == 8){
                ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(smartStore.languages.getString("menu.delete"));
                item.setItemMeta(meta);
                inv.setItem(size, item);
            }
            else {
                ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("");
                item.setItemMeta(meta);
                inv.setItem(size, item);
            }

        }
    }

    public stores (int row, String n, Player p){
        if (row <= 5 && row >= 0) {
            name = n;
            inv = Bukkit.createInventory(null, (row + 1) * 9, name);
            createMenu();
            p.openInventory(inv);
        }
        else
            p.sendMessage("Не правильное количесто строк в магазине");
    }

    public void openStore(Player p){
        msg.toConsole(status+"");
        if(status == 0)
            p.openInventory(inv);
        else if (status == 1){
            for (HashMap.Entry<Integer,storeItems> item : items.entrySet()){
                List<String> lore = new ArrayList<>();
                ItemStack itemTemp = inv.getItem(item.getKey());
                lore.add("Макс цена: " + (item.getValue().getMaxPrice() == -1 ? "" : item.getValue().getMaxPrice()));
                lore.add("Мин цена: " + (item.getValue().getMinPrice() == -1 ? "" : item.getValue().getMinPrice()));
                lore.add("Цена: " + (item.getValue().getPrice() == -1 ? "" : item.getValue().getPrice()));
                lore.add("Макс количество: " + (item.getValue().getMaxCount() == -1 ? "" : item.getValue().getMaxCount()));
                lore.add("Количество: " + (item.getValue().getCount() == -1 ? "" : item.getValue().getCount()));
                lore.add("Шаг: " + (item.getValue().getStep() == -1 ? "" : item.getValue().getStep()));
                if (itemTemp != null)
                    itemTemp.setLore(lore);
            }
            p.openInventory(inv);
        }
    }
    public void updateStore(Player p){
        if(status == 0){
            for (HashMap.Entry<Integer,storeItems> item : items.entrySet()){
                ItemStack itemTemp = inv.getItem(item.getKey());
                item.getValue().clearStats();
                if (itemTemp != null)
                    itemTemp.setLore(null);
            }

            p.openInventory(inv);
        }
        else if (status == 1){
            for (HashMap.Entry<Integer,storeItems> item : items.entrySet()){
                List<String> lore = new ArrayList<>();
                ItemStack itemTemp = inv.getItem(item.getKey());
                lore.add("Макс цена: " + (item.getValue().getMaxPrice() == -1 ? "" : item.getValue().getMaxPrice()));
                lore.add("Мин цена: " + (item.getValue().getMinPrice() == -1 ? "" : item.getValue().getMinPrice()));
                lore.add("Цена: " + (item.getValue().getPrice() == -1 ? "" : item.getValue().getPrice()));
                lore.add("Макс количество: " + (item.getValue().getMaxCount() == -1 ? "" : item.getValue().getMaxCount()));
                lore.add("Количество: " + (item.getValue().getCount() == -1 ? "" : item.getValue().getCount()));
                lore.add("Шаг: " + (item.getValue().getStep() == -1 ? "" : item.getValue().getStep()));
                if (itemTemp != null)
                    itemTemp.setLore(lore);

            }
        }
        p.updateInventory();
    }

    public void toggleStatus () {
        if (status == 0) {
            ItemStack []arrItems;
            status = 1;
            ItemStack item = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(smartStore.languages.getString("menu.data"));
            item.setItemMeta(meta);
            inv.setItem(inv.getSize() - 8, item);
            arrItems = inv.getContents();
            for (int i = 0; i < arrItems.length - 9; i++){
                if (arrItems[i] != null)
                    items.put(i, new storeItems(arrItems[i], i));
            }

        }
        else if (status == 1){
            status = 0;
            ItemStack item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(smartStore.languages.getString("menu.move"));
            item.setItemMeta(meta);
            inv.setItem(inv.getSize() - 8, item);
        }

    }
}
