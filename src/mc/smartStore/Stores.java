package mc.smartStore;

import mc.smartStore.db.ApiDatabase;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Stores implements InventoryHolder {


    private String name;
    public int temp = -1;


    private UUID u;
    private Inventory inv;


    // 0 Перемещение, 1 = установка цены, 2 = сохранен
    private int status = 0;
    public HashMap<Integer, StoreItems> items = new HashMap<>();


    public Stores(int row, String n, Player p, UUID u){
        if (row <= 5 && row >= 0) {
            this.u = u;
            name = n;
            inv = Bukkit.createInventory(this, (row + 1) * 9, name);
            createMenuEdit();
            p.openInventory(inv);
        }
        else
            p.sendMessage("Не правильное количесто строк в магазине");
    }
    public Stores(int row, String n, int status, UUID u){
        if (row <= 5 && row >= 0) {
            this.u = u;
            name = n;
            inv = Bukkit.createInventory(this, (row + 1) * 9, name);
            this.status = status;
            createMenu();
        }
        else
            Message.toConsole("Не правильное количесто строк в магазине");
    }

    public void print(){
        Message.toConsole("name: "+ name);
        Message.toConsole("UUID: "+ u.toString());
        Message.toConsole("status: "+ status);
        Message.toConsole("temp: "+ temp);
        for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
            item.getValue().print();
        }
    }

    public UUID getU() {
        return u;
    }

    public void setU(UUID u) {
        this.u = u;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void createMenuEdit(){
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
                if (status == 0){
                    ItemStack item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(smartStore.languages.getString("menu.move"));
                    item.setItemMeta(meta);
                    inv.setItem(size, item);
                }
                else if (status == 1){
                    ItemStack item = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(smartStore.languages.getString("menu.data"));
                    item.setItemMeta(meta);
                    inv.setItem(size, item);
                }

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
    public void createMenu(){
        int size = inv.getSize();

        for (int i = 8; i >= 0;i--) {
            size--;
            if (i == 8){
                ItemStack item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
                List<String> lore = new ArrayList<>();
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("Редактировать");
                lore.add("§cДля админов");
                item.setItemMeta(meta);
                item.setLore(lore);
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

    public void openStore(Player p){
        if(status == 0)
            p.openInventory(inv);
        else if (status == 1 || status == 2){
            for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
                List<String> lore = new ArrayList<>();
                ItemStack itemTemp = inv.getItem(item.getKey());
                lore.add("§7ЛКМ - Продать одну штуку");
                lore.add("§7Shift + ЛКМ - Все что есть");
                lore.add("");
                lore.add("§fЦена продажи: §5" + (item.getValue().getPrice() == -1 ? "" : item.getValue().getPrice()));
                lore.add("§fКоличество: §5" + (item.getValue().getCount() == -1 ? "" : item.getValue().getCount()) +
                        "§f/§5" +(item.getValue().getMaxCount() == -1 ? "" : item.getValue().getMaxCount()));


//                lore.add("Макс цена: " + (item.getValue().getMaxPrice() == -1 ? "" : item.getValue().getMaxPrice()));
//                lore.add("Мин цена: " + (item.getValue().getMinPrice() == -1 ? "" : item.getValue().getMinPrice()));
//                lore.add("Цена: " + (item.getValue().getPrice() == -1 ? "" : item.getValue().getPrice()));
//                lore.add("Макс количество: " + (item.getValue().getMaxCount() == -1 ? "" : item.getValue().getMaxCount()));
//                lore.add("Количество: " + (item.getValue().getCount() == -1 ? "" : item.getValue().getCount()));
//                lore.add("Шаг: " + (item.getValue().getStep() == -1 ? "" : item.getValue().getStep()));
                if (itemTemp != null)
                    itemTemp.setLore(lore);
            }
            p.openInventory(inv);
        }
    }

    public void updateStore(Player p){
        if(status == 0){
//            for (HashMap.Entry<Integer,storeItems> item : items.entrySet()){
//                ItemStack itemTemp = inv.getItem(item.getKey());
//                item.getValue().clearStats();
//                if (itemTemp != null)
//                    itemTemp.setLore(null);
//            }

            p.openInventory(inv);
        }
        else {
            for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
                List<String> lore = new ArrayList<>();
                ItemStack itemTemp = inv.getItem(item.getKey());
                lore.add("§7ЛКМ - Продать одну штуку");
                lore.add("§7Shift + ЛКМ - Все что есть");
                lore.add("");
                lore.add("§fЦена продажи: §5" + (item.getValue().getPrice() == -1 ? "" : item.getValue().getPrice()));
                lore.add("§fКоличество: §5" + (item.getValue().getCount() == -1 ? "" : item.getValue().getCount()) +
                        "§f/§5" +(item.getValue().getMaxCount() == -1 ? "" : item.getValue().getMaxCount()));
                if (itemTemp != null)
                    itemTemp.setLore(lore);

            }
        }
        p.updateInventory();
    }
    public void updateStore(){
        if(status == 0){
//            for (HashMap.Entry<Integer,storeItems> item : items.entrySet()){
//                ItemStack itemTemp = inv.getItem(item.getKey());
//                item.getValue().clearStats();
//                if (itemTemp != null)
//                    itemTemp.setLore(null);
//            }
        }
        else {
            for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
                List<String> lore = new ArrayList<>();
                ItemStack itemTemp = inv.getItem(item.getKey());
                lore.add("§7ЛКМ - Продать одну штуку");
                lore.add("§7Shift + ЛКМ - Все что есть");
                lore.add("");
                lore.add("§fЦена продажи: §5" + (item.getValue().getPrice() == -1 ? "" : item.getValue().getPrice()));
                lore.add("§fКоличество: §5" + (item.getValue().getCount() == -1 ? "" : item.getValue().getCount()) +
                        "§f/§5" +(item.getValue().getMaxCount() == -1 ? "" : item.getValue().getMaxCount()));
                if (itemTemp != null)
                    itemTemp.setLore(lore);

            }
        }
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

                if (arrItems[i] != null){
                    Message.toConsole("arrItems id = "+i + " name = "+ arrItems[i].getType().name() );
                    if (!items.containsKey(i)){
                        Message.toConsole("+++");
                        items.put(i, new StoreItems(arrItems[i], i));
                    }
                    else {
                        if (inv.getItem(i) != null){
                            items.get(i).setItem(inv.getItem(i));
                        }
                    }
                }
                else {
                    items.remove(i);
                }
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
        temp = -1;

    }


    public void updateStats(Player p, int id){
        int idi = id + 1;
        p.sendMessage(new ComponentBuilder("§6Измегить параметры товара")
                .append("\n§eМакс цена: §6" + (items.get(id).getMaxPrice() == -1 ? "" : items.get(id).getMaxPrice()))
                .append(" §e[§cРед§e]§r")
                .event(new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/st change n:"+name+" id:"+ idi+" mp:"))
                .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Нажми чтобы изменить" ).create()))
                .append("\n§eМин цена: §6" + (items.get(id).getMinPrice() == -1 ? "" : items.get(id).getMinPrice()))
                .append(" §e[§cРед§e]§r")
                .event(new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/st change n:"+name+" id:"+ idi+" mip:"))
                .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Нажми чтобы изменить" ).create()))
                .append("\n§eЦена: §6" + (items.get(id).getPrice() == -1 ? "" : items.get(id).getPrice()))
                .append(" §e[§cРед§e]§r")
                .event(new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/st change n:"+name+" id:"+ idi+" p:"))
                .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Нажми чтобы изменить" ).create()))
                .append("\n§eМакс количество: §6" + (items.get(id).getMaxCount() == -1 ? "" : items.get(id).getMaxCount()))
                .append(" [§cРед§e]§r")
                .event(new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/st change n:"+name+" id:"+ idi+" mc:"))
                .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Нажми чтобы изменить" ).create()))
                .append("\n§eКоличество: §6" + (items.get(id).getCount() == -1 ? "" : items.get(id).getCount()))
                .append(" §e[§cРед§e]§r")
                .event(new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/st change n:"+name+" id:"+ idi+" c:"))
                .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Нажми чтобы изменить" ).create()))
                .append("\n§eШаг: §6" + (items.get(id).getStep() == -1 ? "" : items.get(id).getStep()))
                .append(" §e[§cРед§e]§r")
                .event(new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/st change n:"+name+" id:"+ idi+" s:"))
                .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Нажми чтобы изменить" ).create()))
                .create());

    }
    public boolean check(){
        int ch = 0;
        for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
            if (item.getValue().check()){
                return true;
            }
            ch++;
        }
        if (ch == 0)
            return true;
        return false;
    }

    public void sellOne(Player p, StoreItems storeItem)
    {
        PlayerInventory inv = p.getInventory();
        ItemStack item = storeItem.getItem();
        if  (item == null)
            return;
        if (!inv.contains(item.getType()))
            return;
        int position = inv.first(item.getType());
        ItemStack temp = inv.getItem(position);
        if (temp != null && storeItem.getCount() > 0)
        {
            if (temp.getAmount() == 1)
                inv.clear(position);
            else
            {
                temp.setAmount(temp.getAmount() -1);
                inv.setItem(position, temp);
            }
            int count = storeItem.getCount() - 1;
            storeItem.setCount(count);
            ApiDatabase.updateCount(name, storeItem.getPlace(), count);
            EconomyManager.payMoney(p, storeItem.getPrice());
            updateStore(p);
        }
    }

    public void sellAll(Player p, StoreItems storeItem)
    {
        PlayerInventory invP = p.getInventory();
        ItemStack item = storeItem.getItem();
        if (item != null) {
            int count = storeItem.getCount();
            if (invP.contains(item.getType()))
            {
                HashMap<Integer, ? extends ItemStack> map = invP.all(item.getType());
                int countP = 0;
                for (HashMap.Entry<Integer, ? extends ItemStack> it : map.entrySet()) {
                    int itemCount = it.getValue().getAmount();
                    if (count >= countP + itemCount)
                    {
                        countP += itemCount;
                        invP.clear(it.getKey());
                    }
                    else
                    {
                        invP.getItem(it.getKey()).setAmount(itemCount - (count - countP));
                        countP += count - countP;
                    }
                }
                storeItem.setCount(count - countP);
                ApiDatabase.updateCount(name, storeItem.getPlace(), count - countP);
                EconomyManager.payMoney(p, countP * storeItem.getPrice());
                updateStore(p);
            }
        }
    }

    public void bust ()
    {
        for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
            StoreItems StoreItem = item.getValue();
            int maxCount = StoreItem.getMaxCount();
            int count = StoreItem.getCount();
            int price = StoreItem.getPrice();
            int step = StoreItem.getStep();
            double percent30 = maxCount * 0.30;
            double percent70 = maxCount * 0.70;

            if (count < percent30)
            {
                int minPrice = StoreItem.getMinPrice();
                if (status == 2)
                    ApiDatabase.updatePrice(name, StoreItem.getPlace(), Math.max(price - step, minPrice));
                StoreItem.setPrice(Math.max(price - step, minPrice));
            }
            else if (count > percent70)
            {
                int maxPrice = StoreItem.getMaxPrice();
                if (status == 2)
                    ApiDatabase.updatePrice(name, StoreItem.getPlace(), Math.min(price + step, maxPrice));
                StoreItem.setPrice(Math.min(price + step, maxPrice));
            }
            if (status == 2)
                ApiDatabase.updateCount(name, StoreItem.getPlace(), maxCount);
            StoreItem.setCount(maxCount);

        }
        updateStore();
        List<HumanEntity> players = inv.getViewers();
        Player p;
        for (HumanEntity pl: players ){
            if (pl instanceof Player){
                p = (Player)pl;
                p.updateInventory();
            }
        }

    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
