package russian.denis;

import com.mojang.datafixers.types.templates.Product;
import jdk.jshell.execution.Util;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class Utils {
    private static smartStore plugin;

    public static void init (smartStore plugin) { Utils.plugin = plugin; }

    public static Inventory [] createStores(String[][] items, String []name)
    {
        Inventory [] stores = new Inventory[items.length];
        for (int i = 0; i < items.length; i++)
            stores[i] = createStore(items[i], name[i]);
        return stores;
    }

    public static Inventory createStore(String[] items, String name) {
        Inventory inv = Bukkit.createInventory(null, 3 * 9, name);
        if (items == null)
            return inv;
        for (int i = 0; i < items.length; i++) {
            if (Products.product.contains(items[i])) {
                List<String> lore = new ArrayList<>();
                Material temp = Material.getMaterial(items[i]);
                ItemStack item = new ItemStack(temp);
                ItemMeta meta = item.getItemMeta();
                lore.add("§7ЛКМ - Продать одну штуку");
                lore.add("§7Shift + ЛКМ - Все что есть");
                lore.add("");
//                lore.add("Максимальная цена: " + Products.getMaxPrice(items[i]));
//                lore.add("Минимальная цена: " + Products.getMinPrice(items[i]));
                lore.add("§fЦена продажи: §5" +  Products.getPrice(items[i]));
                lore.add("§fКоличество: §5" + Products.getCount(items[i]) + "§f/§5" + Products.getMaxCount(items[i]));
                meta.setLore(lore);
                item.setItemMeta(meta);
                inv.setItem(i, item);
            } else {
                List<String> lore = new ArrayList<>();
                Material temp = Material.getMaterial(items[i]);
                ItemStack item = new ItemStack(temp);
                ItemMeta meta = item.getItemMeta();
                lore.add("§7Предмет не продается");
                meta.setLore(lore);
                item.setItemMeta(meta);
                inv.setItem(i, item);
            }
        }
        return inv;
    }

    public static Inventory personStore(Inventory inv, Player p) {
        PlayerInventory invP = p.getInventory();
        for (int i = 0; i < (3 * 9) - 1; i++) {
            ItemStack temp = inv.getItem(i);
            if (temp != null) {
                HashMap<Integer, ? extends ItemStack> map = invP.all(temp.getType());
                int count = 0;
                for (HashMap.Entry<Integer, ? extends ItemStack> item : map.entrySet()) {
                    count += item.getValue().getAmount();
                }
                ItemMeta meta = temp.getItemMeta();
                List<String> lore = meta.getLore();
                if (lore != null) {
                    if (lore.size() == 5)
                    {
                        lore.set(3, "§fЦена продажи: §5" + Products.getPrice(temp.getType()));
                        lore.set(4, "§fКоличество: §5" + Products.getCount(temp.getType()) + "§f/§5" + Products.getMaxCount(temp.getType()));
                    }
                    meta.setLore(lore);
                    temp.setItemMeta(meta);
                    inv.setItem(i, temp);
                }
            }
        }
        return inv;
    }

    public static void sellOne(Player p, ItemStack item, Inventory store)
    {
        PlayerInventory inv = p.getInventory();
        if  (item == null)
            return;
        if (!inv.contains(item.getType()))
            return;
        int position = inv.first(item.getType());
        ItemStack temp = inv.getItem(position);
        if (temp != null && Products.getCount(item.getType()) > 0)
        {

            if (temp.getAmount() == 1)
                inv.clear(position);
            else
            {
                temp.setAmount(temp.getAmount() -1);
                inv.setItem(position, temp);
            }
            int count = Products.getCount(item.getType()) - 1;
            Products.setCount(item.getType(), count);
            economyManager.payMoney(p, Products.getPrice(item.getType()));
            Utils.personStore(store, p);
            p.updateInventory();
        }
    }

    public static void sellAll(Player p, ItemStack temp, Inventory store)
    {
        PlayerInventory invP = p.getInventory();
        if (temp != null) {
            int count = Products.getCount(temp.getType());
            if (invP.contains(temp.getType()))
            {
                HashMap<Integer, ? extends ItemStack> map = invP.all(temp.getType());
                int countP = 0;
                for (HashMap.Entry<Integer, ? extends ItemStack> item : map.entrySet()) {
                    int itemCount = item.getValue().getAmount();
                    if (count >= countP + itemCount)
                    {
                        countP += itemCount;
                        invP.clear(item.getKey());
                    }
                    else
                    {
                        invP.getItem(item.getKey()).setAmount(itemCount - (count - countP));
                        countP += count - countP;
                    }
                }
                Products.setCount(temp.getType(),count - countP);
                economyManager.payMoney(p, countP * Products.getPrice(temp.getType()));
                Utils.personStore(store, p);
                p.updateInventory();
            }
        }
    }


}
