package mc.smartStore;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Message {
    public static String prefix = "§6[§eМагаз§6] §f";

    public static void toConsole(String msg){
        Bukkit.getConsoleSender().sendMessage(prefix+msg);
    }

    public static String permsBuy(){
        return (prefix + "У тебя нет прав на покупку");
    }
    public static String permsSell(){
        return (prefix + "У тебя нет прав на продажу");
    }
    public static String permsInfo(){
        return (prefix + "У тебя нет прав для просмотра параметров товара");
    }
    public static String permsEdit(){
        return (prefix + "У тебя нет прав на редактирование");
    }
    public static String editNotSell(){
        return (prefix + "Во время редактирования магазина нельзя продавать");
    }
    public static String editNotBuy(){
        return (prefix + "Во время редактирования магазина нельзя покупать");
    }

    public static String saveStore(String name){
        return (prefix + "Магазин " + name + " сохранен");
    }
    public static String saveStoreError(){
        return (prefix + "Не удалось сохранить магазин, не все параметры заполнены или магазин пуст");
    }
    public static String deleteStore(String name){
        return (prefix +  "Магазин " + name + " удалён");
    }
    public static String deleteStoreNotPerms(){
        return (prefix +  "У тебя нет прав удалять магазин");
    }

    public static BaseComponent[] infoStore(Stores stores){
        return (new ComponentBuilder("§7------------------------------------\n")
                .append("§5§l| §fДеньги магазина: " + String.format("%.2f",stores.getCapital()))
                .append(" §e[§cРед§e]§r")
                .event(new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/st capital "+stores.getName()+" "))
                .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Нажми чтобы изменить" ).create()))
                .append("\n§7------------------------------------")
                .create());

    }
    public static String notCountLineStore(){
        return (prefix + "Не правильное количесто строк в магазине");
    }

    //Menu
    public static String menuEdit(){
        return ("Редактировать");
    }
    public static String menuInfo(){
        return ("Информация");
    }
    public static List<String> menuInfoMore(Stores stores){
        List<String> lore = new ArrayList<>();
        lore.add("§7Нажмите чтобы отобразить в чате");
        lore.add("");
        lore.add("§5§l| §fДеньги магазина: " + String.format("%.2f",stores.getCapital()));
        return (lore);
    }

    public static List<String> menuEditDescription(){
        List<String> lore = new ArrayList<>();
        lore.add("§cДля админов");
        return (lore);
    }
    public static String menuSave(){
        return ("Сохранить");
    }
    public static String menuDelete(){
        return ("Удалить");
    }
    public static String menuMove(){
        return ("Режим перемещение предметов");
    }
    public static String menuData(){
        return ("Режим установки данных о товаре");
    }
    public static List<String> menuProduct(StoreItems itemp){
        int size;
        if (itemp.getCount() >= itemp.getItem().getMaxStackSize()) {
            size = itemp.getItem().getMaxStackSize();
        }
        else
            size = itemp.getCount();
        List<String> lore = new ArrayList<>();
        lore.add("§7ПКМ - Купить");
        lore.add("§7Shift + ПКМ - Купить " + size);
        lore.add("§7Shift + ЛКМ - Информация о товаре");
        lore.add("");
        lore.add("§fЦена продажи: §5" + (itemp.getBasePrice() == -1 ? "" : String.format("%.2f", itemp.getSellOne())) + "§d$");
        lore.add("§fЦена покупки: §5" + (itemp.getBasePrice() == -1 ? "" : String.format("%.2f", itemp.getBuyOne())) + "§d$");
        lore.add("§fЦена покупки "+size+": §5" + (itemp.getBasePrice() == -1 ? "" : String.format("%.2f", itemp.getBuyMore(size))) + "§d$");

        lore.add("");
        lore.add("§fКоличество на складе: §5" + (itemp.getCount() == -1 ? "" : itemp.getCount()));
        return (lore);
    }

}
