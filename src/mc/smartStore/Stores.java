package mc.smartStore;

import mc.smartStore.db.ApiDatabase;
import mc.smartStore.handlers.MenuElement;
import mc.smartStore.handlers.inventoryStore.*;
import mc.smartStore.utils.StatusStore;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
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
    private int row;
    public int temp = -1;
    private double capital = 0;
    private UUID u;
    private UUID pl;
    private Inventory inv;
    public HashMap<Integer, StoreItems> items = new HashMap<>();
    public HashMap<Integer, MenuElement> menu = new HashMap<>();

    public Stores(int row, String n, Player p, UUID u, double capital){
        if (row <= 5 && row >= 1) {
            this.capital = capital;
            this.u = u;
            this.row = row;
            this.name = n;
            pl = p.getUniqueId();
            inv = Bukkit.createInventory(this, (row + 1) * 9, name);
            createMenuEdit();
            p.openInventory(inv);
        }
        else
            p.sendMessage(Message.notCountLineStore());
    }
    public Stores(int row, String n, StatusStore status, UUID u, UUID pl, double capital){
        if (row <= 5 && row >= 1) {
            this.u = u;
            this.capital = capital;
            this.row = row;
            this.name = n;
            this.pl = pl;
            inv = Bukkit.createInventory(this, (row + 1) * 9, name);
            this.status = status;
            createMenu();
        }
        else
            Message.toConsole(Message.notCountLineStore());
    }

    public void print(){
        Message.toConsole("name: "+ name);
        Message.toConsole("UUID Villager: "+ u.toString());
        Message.toConsole("UUID owner: "+ pl.toString());
        Message.toConsole("status: "+ status);
        Message.toConsole("temp: "+ temp);
        for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
            item.getValue().print();
        }
    }

    private void createMenuElement(int id, Material material, String name,  List<String> lore, MenuElement menuElement){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        item.setLore(lore);
        inv.setItem(id, item);
        menu.put(id, menuElement);
    }
    private void createMenuElement(int id, Material material, String name, MenuElement menuElement){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        inv.setItem(id, item);
        menu.put(id, menuElement);
    }
    private void createMenuElement(int id, StoreItems item, List<String> lore){
//                ItemStack itemTemp = inv.getItem(item.getKey());

        item.getItem().setLore(lore);
        inv.setItem(id, item.getItem());
    }
    private void createMenuElement(int id, Material material, String name){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        inv.setItem(id, item);
    }

    public void createMenuEdit(){
        menu.clear();
        int size = inv.getSize() - 1;
        int i = 8;
        while (size >= 0) {
            if (i == 0){
                createMenuElement(size, Material.LIME_STAINED_GLASS_PANE, Message.menuSave(), new SaveStore(this, size));
            }
            else if (i == 1) {
                if (status == StatusStore.MOVE){
                    createMenuElement(size, Material.ORANGE_STAINED_GLASS_PANE, Message.menuMove(), new Toggle(this, size));
                }
                else if (status == StatusStore.PRICE){
                    createMenuElement(size, Material.BLUE_STAINED_GLASS_PANE, Message.menuData(), new Toggle(this, size));
                }
            }
            else if (i == 8){
                createMenuElement(size, Material.RED_STAINED_GLASS_PANE, Message.menuDelete(), new DeleteStore(this, size));
            }
            else if (i >= 0) {
                createMenuElement(size, Material.GRAY_STAINED_GLASS_PANE, "");
            }
            else {
                if (inv.getItem(size) != null){
                    menu.put(size, new ItemStoreSell(this, size));
                }
                else
                    menu.put(size, new Move(this, size));
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
                createMenuElement(size, Material.YELLOW_STAINED_GLASS_PANE, Message.menuEdit(), Message.menuEditDescription(),new Edit(this, size));
            }
            else if (i == 0){
                createMenuElement(size, Material.BOOK, Message.menuInfo(), Message.menuInfoMore(this),new InfoStore(this, size));
            }
            else if (i >= 0) {
                createMenuElement(size, Material.GRAY_STAINED_GLASS_PANE, "");
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

    public void playerInvSellMenu(Player p){
        PlayerInventory invP = p.getInventory();
        for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
            HashMap<Integer, ? extends ItemStack> tempItems = invP.all(item.getValue().getItem().getType());
            for (HashMap.Entry<Integer, ? extends ItemStack> tempItem : tempItems.entrySet()){
                List<String> lore = new ArrayList<>();
                lore.add("§7Shift + ЛКМ - Продать (§5" + String.format("%.2f", item.getValue().getSellMore(tempItem.getValue().getAmount()))+"§d$§7)");

//                lore.add(tempItem.getValue().getAmount()+" Amount");
//                lore.add(String.format("%.10f",tempItem.getValue().getAmount() *item.getValue().getDerivative()) +" Amount * der");
//                lore.add(String.format("%.10f",item.getValue().getDerivative())+" Derivative");
//                lore.add(String.format("%.10f", item.getValue().WarehousePercentage())+" test");
                tempItem.getValue().setLore(lore);
                invP.setItem(tempItem.getKey(), tempItem.getValue());
            }
        }
        p.updateInventory();
    }

    public void openStore(Player p){
        if(status == StatusStore.MOVE)
            p.openInventory(inv);
        else if (status == StatusStore.PRICE || status == StatusStore.SAVE){
            for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
                createMenuElement(item.getKey(), item.getValue(), Message.menuProduct(item.getValue()));

            }
            p.openInventory(inv);
            if (status == StatusStore.SAVE){
                playerInvSellMenu(p);
            }
        }
    }

    public void updateStore(Player p){
        if(status == StatusStore.MOVE)
            p.openInventory(inv);
        else {
            for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
                createMenuElement(item.getKey() ,item.getValue(), Message.menuProduct(item.getValue()));
            }
            if (status == StatusStore.SAVE)
            {
                createMenu();
                List<HumanEntity> views = inv.getViewers();
                for (HumanEntity pl:views){
                    playerInvSellMenu((Player) pl);
                }
//                playerInvSellMenu(p);
            }
        }
        p.updateInventory();
    }
    public void updateStore(){
        if (status != StatusStore.MOVE) {
            for (HashMap.Entry<Integer, StoreItems> item : items.entrySet()){
                createMenuElement(item.getKey() ,item.getValue(), Message.menuProduct(item.getValue()));
            }
            if (status == StatusStore.SAVE)
                createMenu();
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
                .append(")\n§eБазовая цена: §6" + (items.get(id).getBasePrice() == -1 ? "" : String.format("%.2f", items.get(id).getBasePrice())))
                .append("\n§eКоличество на складе: §6" + (items.get(id).getCount() == -1 ? "" : items.get(id).getCount()))
                .append("\n§eЦена продажи: §6" + (items.get(id).getBasePrice() == -1 ? "" : String.format("%.2f", items.get(id).getSellOne())))
                .append("\n§eЦена покупки: §6" + (items.get(id).getBasePrice() == -1 ? "" : String.format("%.2f", items.get(id).getBuyOne())))
                .create());

    }

    public void updateStats(Player p, int id){
        int idi = id + 1;
        p.sendMessage(new ComponentBuilder("§6Измегить параметры товара: ")
                .append(inv.getItem(id).getType().name())
                .append(" ("+ idi)
                .append(")\n§eБазовая цена: §6" + (items.get(id).getBasePrice() == -1 ? "" : String.format("%.2f", items.get(id).getBasePrice())))
                .append(" §e[§cРед§e]§r")
                .event(new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/st price "+name+" "+ idi+" "))
                .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Нажми чтобы изменить" ).create()))
                .append("\n§eКоличество на складе: §6" + (items.get(id).getCount() == -1 ? "" : items.get(id).getCount()))
                .append(" §e[§cРед§e]§r")
                .event(new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/st count "+name+" "+ idi+" "))
                .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Нажми чтобы изменить" ).create()))
                .append("\n§eЦена продажи: §6" + (items.get(id).getBasePrice() == -1 ? "" : String.format("%.2f", items.get(id).getSellOne())))
                .append("\n§eЦена покупки: §6" + (items.get(id).getBasePrice() == -1 ? "" : String.format("%.2f", items.get(id).getBuyOne())))
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

    public void sellOne(Player p, StoreItems storeItem, Stores store)
    {
        PlayerInventory inv = p.getInventory();
        ItemStack item = storeItem.getItem();
        double sell =  storeItem.getSellOne();
        if (item == null)
            return;
        if (!inv.contains(item.getType()))
            return;
        int position = inv.first(item.getType());
        ItemStack temp = inv.getItem(position);
        if (temp != null && store.getCapital() >= sell )
        {
            if (temp.getAmount() == 1)
                inv.clear(position);
            else
            {
                temp.setAmount(temp.getAmount() -1);
                inv.setItem(position, temp);
            }
            int count = storeItem.getCount() + 1;
            ApiDatabase.updateCount(name, storeItem.getPlace(), count);
            storeItem.setCount(count);
            ApiDatabase.updateCapital(name,store.getCapital() - sell );
            store.setCapital(store.getCapital() - sell);
            EconomyManager.payMoney(p, sell);
            updateStore(p);
        }
        else
            p.sendMessage("В магазине не достаточно денег чтобы вам заплатить");
    }

    private int firstNoStack(PlayerInventory invP, ItemStack item){

        ItemStack[] tempItems = invP.getContents();
        for (int  i = 0; i < tempItems.length; i++){
//            if (tempItems[i] != null){
//                Message.toConsole("id: " + i +" - "+tempItems[i].getAmount() +" < " + tempItems[i].getMaxStackSize());
//                Message.toConsole(tempItems[i].getType().name() +" = " + item.getType().name());
//
//            }
            if (tempItems[i] != null && tempItems[i].getType().name().equals(item.getType().name())){
                if (tempItems[i].getAmount() < tempItems[i].getMaxStackSize())
                {
//                    Message.toConsole(i+"");
//                    Message.toConsole(tempItems[i].getType().name());
                    return i;
                }
            }

        }
        return -1;
    }

    public void buyOne(Player p, StoreItems storeItem, Stores store)
    {
        PlayerInventory invP = p.getInventory();
        ItemStack item = storeItem.getItem();
        double buy =  storeItem.getBuyOne();
        if (item == null)
            return;
        int position = firstNoStack(invP, item);
        if (storeItem.getCount() > 0 )
        {
            if (!EconomyManager.takeMoney(p, buy)){
                p.closeInventory();
                p.sendMessage("У тебя не достаточно денег");
                return;
            }
            if (position != -1){
//                p.sendMessage("test 1 " + position);
                ItemStack temp = invP.getItem(position);
                temp.add();
                invP.setItem(position, temp);
            }
            else {
//                p.sendMessage("test 2 " + position);
                position = invP.firstEmpty();
                if (position == -1){
                    p.closeInventory();
                    p.sendMessage("В инвенторе нет места");
                    return;
                }
                invP.setItem(position, new ItemStack(item.getType()));
            }
            int count = storeItem.getCount() - 1;
            ApiDatabase.updateCount(name, storeItem.getPlace(), count);
            storeItem.setCount(count);
            ApiDatabase.updateCapital(name,store.getCapital() + buy );
            store.setCapital(store.getCapital() + buy);
            updateStore(p);
        }
        else {
            p.sendMessage("На складе нет данного предмета");
        }
    }

    public void buyMore(Player p, StoreItems storeItem, Stores store)
    {

        ItemStack item = storeItem.getItem();
        if (item == null)
            return;

        PlayerInventory invP = p.getInventory();
        int position = invP.firstEmpty();
        if (position == -1){
            p.closeInventory();
            p.sendMessage("В инвенторе нет места");
            return;
        }
        if (storeItem.getCount() > 0 )
        {
            int size = item.getMaxStackSize();
            if (storeItem.getCount() < item.getMaxStackSize()){
                size = storeItem.getCount();
            }
            double buy =  storeItem.getBuyMore(size);
            if (!EconomyManager.takeMoney(p, buy)){
                p.closeInventory();
                p.sendMessage("У тебя не достаточно денег");
                return;
            }
            ItemStack temp = new ItemStack(item.getType());
            temp.setAmount(size);
            invP.setItem(position, temp);
            int count = storeItem.getCount() - size;
            ApiDatabase.updateCount(name, storeItem.getPlace(), count);
            storeItem.setCount(count);
            ApiDatabase.updateCapital(name,store.getCapital() + buy );
            store.setCapital(store.getCapital() + buy);
            updateStore(p);
        }
        else {
            p.sendMessage("На складе нет данного предмета");
        }
    }
    public void sellMore(Player p, StoreItems storeItem, Stores store, ItemStack sellItem, int position)
    {
        PlayerInventory inv = p.getInventory();
        ItemStack item = storeItem.getItem();
        double sell =  storeItem.getSellMore(sellItem.getAmount());
        if (item == null)
            return;
        if (!inv.contains(item.getType()))
            return;

        if (store.getCapital() >= sell )
        {
            inv.clear(position);
            int count = storeItem.getCount() + sellItem.getAmount();
            ApiDatabase.updateCount(name, storeItem.getPlace(), count);
            storeItem.setCount(count);
            ApiDatabase.updateCapital(name,store.getCapital() - sell );
            store.setCapital(store.getCapital() - sell);
            EconomyManager.payMoney(p, sell);
            updateStore(p);
        }
        else
            p.sendMessage("В магазине не достаточно денег чтобы вам заплатить");
    }


    @Override
    public Inventory getInventory() {
        return inv;
    }


    public UUID getU() {
        return u;
    }

    public void setU(UUID u) {
        this.u = u;
    }
    public UUID getPl() {
        return pl;
    }

    public void setPl(UUID pl) {
        this.pl = pl;
    }
    public double getCapital() {
        return capital;
    }

    public int getRow() {
        return row;
    }
    public void setCapital(double capital) {
        this.capital = capital;
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
