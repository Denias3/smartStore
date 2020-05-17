package mc.smartStore;

import mc.smartStore.db.ApiDatabase;
import mc.smartStore.handlers.MenuElement;
import mc.smartStore.handlers.inventoryPlayer.*;
import mc.smartStore.utils.PermissionClick;
import mc.smartStore.utils.StatusStore;
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


    private StatusStore status = StatusStore.MOVE;
    private String name;
    public int temp = -1;
    private double volume = 0;
    private double curVolume = 0;
    private double capital = 0;
    private UUID u;
    private Inventory inv;
    public HashMap<Integer, StoreItems> items = new HashMap<>();
    public HashMap<Integer, MenuElement> menu = new HashMap<>();

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
    public Stores(int row, String n, StatusStore status, UUID u, double capital){
        if (row <= 5 && row >= 0) {
            this.u = u;
            this.capital = capital;
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
    public void createMenuEdit(){
        menu.clear();
        Integer size = inv.getSize() - 1;
        int i = 8;
        while (size >= 0) {
            if (i == 0){
                ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(SmartStore.languages.getString("menu.save"));
                item.setItemMeta(meta);
                inv.setItem(size, item);
                menu.put(size, new SaveStore(this, size));
            }
            else if (i == 1) {
                if (status == StatusStore.MOVE){
                    ItemStack item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(SmartStore.languages.getString("menu.move"));
                    item.setItemMeta(meta);
                    inv.setItem(size, item);
                    menu.put(size, new Toggle(this, size));
                }
                else if (status == StatusStore.PRICE){
                    ItemStack item = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(SmartStore.languages.getString("menu.data"));
                    item.setItemMeta(meta);
                    inv.setItem(size, item);
                    menu.put(size, new Toggle(this, size));
                }

            }
            else if (i == 8){
                ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(SmartStore.languages.getString("menu.delete"));
                item.setItemMeta(meta);
                inv.setItem(size, item);
                menu.put(size, new DeleteStore(this, size));
            }
            else if (i >= 0) {
                ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("");
                item.setItemMeta(meta);
                inv.setItem(size, item);
            }
            else {
                if (inv.getItem(size) != null){
                    menu.put(size, new ItemStoreSell(this, size));
                }
            }
            size--;
            i--;
        }
    }
    public void createMenu(){
        menu.clear();
        int size = inv.getSize() - 1;
        int i = 8;
        while (size >= 0) {
            if (i == 8){
                ItemStack item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
                List<String> lore = new ArrayList<>();
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("Редактировать");
                lore.add("§cДля админов");
                item.setItemMeta(meta);
                item.setLore(lore);
                inv.setItem(size, item);
                menu.put(size, new Edit(this, size));
            }
            else if (i == 0){
                ItemStack item = new ItemStack(Material.BOOK);
                List<String> lore = new ArrayList<>();
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("Информация");
                lore.add("§7Нажмите чтобы отобразить в чате");
                lore.add("§fОборот: §5" + String.format("%.2f", capital)+ "§d$");
                lore.add("§fОбъем: §5" + String.format("%.2f", curVolume)+ "§d$" +"§f/§5" +
                        String.format("%.2f", volume)+ "§d$");
                lore.add("");
                lore.add("§5§l| §fКаждые 2 часа количество товара");
                lore.add("§5§l| §fпополняется до максимума");
                lore.add("§5§l| §fи цена меняется в зависимости");
                lore.add("§5§l| §fот того какое количество отсалось.");
                lore.add("§5§l| §fКоличество общее для всех.");
                lore.add("§5§l| §fЕсли продали больше 70% то");
                lore.add("§5§l| §fцена уменьшается на шаг.");
                lore.add("§5§l| §fЕсли продали меньше 30% то");
                lore.add("§5§l| §fцена увеличивается на шаг.");
                lore.add("§5§l| §fЕсли продали в районе от ");
                lore.add("§5§l| §f30% до 70% то цена не меняется.");
                item.setItemMeta(meta);
                item.setLore(lore);
                inv.setItem(size, item);
                menu.put(size, new InfoStore(this, size));
            }
            else if (i >= 0) {
                ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("");
                item.setItemMeta(meta);
                inv.setItem(size, item);
            }
            else {
                if (inv.getItem(size) != null){
                    menu.put(size, new ItemStoreSell(this, size));
                }
            }
            size--;
            i--;
        }
    }

    public void openStore(Player p){
        if(status == StatusStore.MOVE)
            p.openInventory(inv);
        else if (status == StatusStore.PRICE || status == StatusStore.SAVE){
            for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
                List<String> lore = new ArrayList<>();
                ItemStack itemTemp = inv.getItem(item.getKey());
                StoreItems itemp = item.getValue();
                lore.add("§7ЛКМ - Продать одну штуку");
                lore.add("§7Shift + ЛКМ - Продать все что есть");
                lore.add("§7Shift + ПКМ - Информация о товаре");
                lore.add("");
                lore.add("§f§lЦена продажи");
                lore.add("§fЗа штуку: §5" + (itemp.getPrice() == -1 ? "" : String.format("%.2f", itemp.getPrice()))  + "§d$");
                lore.add("§fЗа стак: §5" + (itemp.getPrice() == -1 ? "" : String.format("%.2f", itemp.getPrice() * 64)) + "§d$");
                lore.add("");
                lore.add("§fКоличество: §5" + (itemp.getCount() == -1 ? "" : itemp.getCount()) +
                        "§f/§5" +(itemp.getMaxCount() == -1 ? "" : itemp.getMaxCount()));
                if (itemTemp != null)
                    itemTemp.setLore(lore);
            }
            p.openInventory(inv);
        }
    }

    public void updateStore(Player p){
        calculVolume();
        if(status == StatusStore.MOVE)
            p.openInventory(inv);
        else {
            if (status == StatusStore.SAVE){
                List<String> lore = inv.getItem(inv.getSize() - 9).getLore();
                if (lore != null){
                    lore.set(1, "§fОборот: §5" + String.format("%.2f", capital) + "§d$");
                    lore.set(2, "§fОбъем: §5" + String.format("%.2f", curVolume)+ "§d$" +"§f/§5" +
                            String.format("%.2f", volume)+ "§d$");
                    inv.getItem(inv.getSize() - 9).setLore(lore);
                }

            }
            for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
                List<String> lore = new ArrayList<>();
                ItemStack itemTemp = inv.getItem(item.getKey());
                StoreItems itemp = item.getValue();
                lore.add("§7ЛКМ - Продать одну штуку");
                lore.add("§7Shift + ЛКМ - Продать все что есть");
                lore.add("§7Shift + ПКМ - Информация о товаре");
                lore.add("");
                lore.add("§f§lЦена продажи");
                lore.add("§fЗа штуку: §5" + (itemp.getPrice() == -1 ? "" : String.format("%.2f", itemp.getPrice()))  + "§d$");
                lore.add("§fЗа стак: §5" + (itemp.getPrice() == -1 ? "" : String.format("%.2f", itemp.getPrice() * 64)) + "§d$");
                lore.add("");
                lore.add("§fКоличество: §5" + (itemp.getCount() == -1 ? "" : itemp.getCount()) +
                        "§f/§5" +(itemp.getMaxCount() == -1 ? "" : itemp.getMaxCount()));
                if (itemTemp != null)
                    itemTemp.setLore(lore);

            }
        }
        p.updateInventory();
    }
    public void updateStore(){
        if (status != StatusStore.MOVE) {
            calculVolume();
            if (status == StatusStore.SAVE){
                List<String> lore = inv.getItem(inv.getSize() - 9).getLore();
                if (lore != null){
                    lore.set(1, "§fОборот: §5" + String.format("%.2f", capital) + "§d$");
                    lore.set(2, "§fОбъем: §5" + String.format("%.2f", curVolume)+ "§d$" +"§f/§5" +
                            String.format("%.2f", volume)+ "§d$");
                    inv.getItem(inv.getSize() - 9).setLore(lore);
                }
            }
            for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
                List<String> lore = new ArrayList<>();
                ItemStack itemTemp = inv.getItem(item.getKey());
                StoreItems itemp = item.getValue();
                lore.add("§7ЛКМ - Продать одну штуку");
                lore.add("§7Shift + ЛКМ - Продать все что есть");
                lore.add("§7Shift + ПКМ - Информация о товаре");
                lore.add("");
                lore.add("§f§lЦена продажи");
                lore.add("§fЗа штуку: §5" + (itemp.getPrice() == -1 ? "" : String.format("%.2f", itemp.getPrice()))  + "§d$");
                lore.add("§fЗа стак: §5" + (itemp.getPrice() == -1 ? "" : String.format("%.2f", itemp.getPrice() * 64)) + "§d$");
                lore.add("");
                lore.add("§fКоличество: §5" + (itemp.getCount() == -1 ? "" : itemp.getCount()) +
                        "§f/§5" +(itemp.getMaxCount() == -1 ? "" : itemp.getMaxCount()));
                if (itemTemp != null)
                    itemTemp.setLore(lore);

            }

        }
    }

    public void toggleStatus () {

        if (status == StatusStore.MOVE) {
            ItemStack []arrItems;
            status = StatusStore.PRICE;
            createMenuEdit();
            arrItems = inv.getContents();
            for (int i = 0; i < arrItems.length - 9; i++){

                if (arrItems[i] != null){
//                    Message.toConsole("arrItems id = "+i + " name = "+ arrItems[i].getType().name() );
                    if (!items.containsKey(i)){
//                        Message.toConsole("+++");
                        menu.put(i, new ItemStoreSell(this, i));
                        items.put(i, new StoreItems(arrItems[i], i));
                    }
                    else {
                        if (inv.getItem(i) != null){
                            menu.put(i, new ItemStoreSell(this, i));
                            items.get(i).setItem(inv.getItem(i));
                        }
                    }
                }
                else {
                    menu.remove(i);
                    items.remove(i);
                }
            }

        }
        else if (status == StatusStore.PRICE){
            menu.clear();
            status = StatusStore.MOVE;
            createMenuEdit();
        }
        temp = -1;

    }

    public void showStats(Player p, int id){
        int idi = id + 1;
        p.sendMessage(new ComponentBuilder("§6Параметры товара: ")
                .append(inv.getItem(id).getType().name())
                .append(" ("+ idi)
                .append(")\n§eМакс цена: §6" + (items.get(id).getMaxPrice() == -1 ? "" : String.format("%.2f", items.get(id).getMaxPrice())))
                .append("\n§eМин цена: §6" + (items.get(id).getMinPrice() == -1 ? "" : String.format("%.2f", items.get(id).getMinPrice())))
                .append("\n§eЦена: §6" + (items.get(id).getPrice() == -1 ? "" : String.format("%.2f", items.get(id).getPrice())))
                .append("\n§eМакс количество: §6" + (items.get(id).getMaxCount() == -1 ? "" : items.get(id).getMaxCount()))
                .append("\n§eКоличество: §6" + (items.get(id).getCount() == -1 ? "" : items.get(id).getCount()))
                .append("\n§eШаг: §6" + (items.get(id).getStep() == -1 ? "" : String.format("%.2f", items.get(id).getStep())))
                .create());

    }

    public void updateStats(Player p, int id){
        int idi = id + 1;
        p.sendMessage(new ComponentBuilder("§6Измегить параметры товара: ")
                .append(inv.getItem(id).getType().name())
                .append(" ("+ idi)
                .append(")\n§eМакс цена: §6" + (items.get(id).getMaxPrice() == -1 ? "" : String.format("%.2f", items.get(id).getMaxPrice())))
                .append(" §e[§cРед§e]§r")
                .event(new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/st change n:"+name+" id:"+ idi+" mp:"))
                .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Нажми чтобы изменить" ).create()))
                .append("\n§eМин цена: §6" + (items.get(id).getMinPrice() == -1 ? "" : String.format("%.2f", items.get(id).getMinPrice())))
                .append(" §e[§cРед§e]§r")
                .event(new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/st change n:"+name+" id:"+ idi+" mip:"))
                .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Нажми чтобы изменить" ).create()))
                .append("\n§eЦена: §6" + (items.get(id).getPrice() == -1 ? "" : String.format("%.2f", items.get(id).getPrice())))
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
                .append("\n§eШаг: §6" + (items.get(id).getStep() == -1 ? "" : String.format("%.2f", items.get(id).getStep())))
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

    public void calculVolume(){
        volume = 0;
        curVolume = 0;
        for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
            volume += item.getValue().getMaxCount() * item.getValue().getPrice();
            curVolume += item.getValue().getCount() * item.getValue().getPrice();
        }
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
            ApiDatabase.updateCapital(name,storeItem.getPrice());
            EconomyManager.payMoney(p, storeItem.getPrice());
            capital += storeItem.getPrice();
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
                ApiDatabase.updateCapital(name,countP * storeItem.getPrice());
                EconomyManager.payMoney(p, countP * storeItem.getPrice());
                capital += countP * storeItem.getPrice();
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
            double price = StoreItem.getPrice();
            double step = StoreItem.getStep();
            double percent30 = maxCount * 0.30;
            double percent70 = maxCount * 0.70;

            if (count < percent30)
            {
                double minPrice = StoreItem.getMinPrice();
                if (status == StatusStore.SAVE)
                    ApiDatabase.updatePrice(name, StoreItem.getPlace(), Math.max(price - step, minPrice));
                StoreItem.setPrice(Math.max(price - step, minPrice));
            }
            else if (count > percent70)
            {
                double maxPrice = StoreItem.getMaxPrice();
                if (status == StatusStore.SAVE)
                    ApiDatabase.updatePrice(name, StoreItem.getPlace(), Math.min(price + step, maxPrice));
                StoreItem.setPrice(Math.min(price + step, maxPrice));
            }
            if (status == StatusStore.SAVE)
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
    public double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getCurVolume() {
        return curVolume;
    }

    public void setCurVolume(double curVolume) {
        this.curVolume = curVolume;
    }
    public UUID getU() {
        return u;
    }

    public void setU(UUID u) {
        this.u = u;
    }

    public void setStatus(StatusStore status) {
        this.status = status;
    }
    public StatusStore getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
}
