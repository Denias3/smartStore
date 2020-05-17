package mc.smartStore;

import org.bukkit.Bukkit;

public class Message {
    public static String prefix = "§6[§eСкупщик§6] §f";

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

    public static String infoStore(Stores stores){
        return ("§7------------------------------------\n"+
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
                "§7------------------------------------");
    }

}
