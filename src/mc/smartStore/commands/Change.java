package mc.smartStore.commands;


import mc.smartStore.*;
import mc.smartStore.db.ApiDatabase;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

import static mc.smartStore.smartStore.stores;

public class Change {
    private String []argc;
    private Player p;

    private int place = -1;
    private String name = null;
    private int max_price= -1;
    private int min_price= -1;
    private int price= -1;
    private int max_count= -1;
    private int count= -1;
    private int step= -1;

    public String getError() {
        return error;
    }

    private String error = null;

    public void print(){
        p.sendMessage(place +"");
        p.sendMessage(name + "");
        p.sendMessage(max_price+"");
        p.sendMessage(min_price+"");
        p.sendMessage(price+"");
        p.sendMessage(max_count+"");
        p.sendMessage(count+"");
        p.sendMessage(step+"");
    }



    public Change(String []argc, Player p){
        this.argc = argc;
        this.p = p;
    }

    private boolean repeatParam(int param, String param2){
        if (param != -1){
            this.error = "Повторяющиеся параметры " +param2;
            return false;
        }
        else return true;
    }

    private boolean repeatParam(String param, String param2){
        if (param != null){
            this.error = "Повторяющиеся параметры: " +param2;
            return false;
        }
        else return true;
    }

    public void printError(){
        if (error != null)
            p.sendMessage(error);
    }

    private boolean error(String name, String param){
        this.error = name +": " +param;
        return false;
    }

    public boolean parse(){
        int ch = 0;
        for(int i = 1; i < argc.length; i++){
            if (argc[i].matches("id:([0-9]+|all)")){
                if (!repeatParam(place, argc[i]))
                    return false;
                if (argc[i].substring(3).equals("all")){
                    place = -2;
                }
                else
                    place = Integer.parseInt(argc[i].substring(3)) - 1;
                if (!(place == -2 || (place >= 0 && place <= 35)))
                    return error("Такой ячейки нет", argc[i]);
            }
            else if(argc[i].matches("n:[\\w]+")){
                if (!repeatParam(name, argc[i]))
                    return false;
                if (argc[i].substring(2).length() <= 3)
                    return error("Имя магазина должно содержать больше 3 букв", argc[i]);
                name = argc[i].substring(2);
            }
            else if(argc[i].matches("mp:[0-9]+")){
                if (!repeatParam(max_price, argc[i]))
                    return false;
                if (argc[i].substring(3).length() > 9)
                    return error("Параметр сликом большой", argc[i]);
                max_price = Integer.parseInt(argc[i].substring(3));
                ch++;
            }
            else if(argc[i].matches("mip:[0-9]+")){
                if (!repeatParam(min_price, argc[i]))
                    return false;
                if (argc[i].substring(4).length() > 9)
                    return error("Параметр сликом большой", argc[i]);
                min_price = Integer.parseInt(argc[i].substring(4));
                ch++;
            }
            else if(argc[i].matches("p:[0-9]+")){
                if (!repeatParam(price, argc[i]))
                    return false;
                if (argc[i].substring(2).length() > 9)
                    return error("Параметр сликом большой", argc[i]);
                price = Integer.parseInt(argc[i].substring(2));
                ch++;
            }
            else if(argc[i].matches("mc:[0-9]+")){
                if (!repeatParam(max_count, argc[i]))
                    return false;
                if (argc[i].substring(3).length() > 9)
                    return error("Параметр сликом большой", argc[i]);
                max_count = Integer.parseInt(argc[i].substring(3));
                ch++;
            }
            else if(argc[i].matches("c:[0-9]+")){
                if (!repeatParam(count, argc[i]))
                    return false;
                if (argc[i].substring(2).length() > 9)
                    return error("Параметр сликом большой", argc[i]);
                count = Integer.parseInt(argc[i].substring(2));
                ch++;
            }
            else if(argc[i].matches("s:[0-9]+")){
                if (!repeatParam(step, argc[i]))
                    return false;
                if (argc[i].substring(2).length() > 9)
                    return error("Параметр сликом большой", argc[i]);
                step = Integer.parseInt(argc[i].substring(2));
                ch++;
            }
            else
                return error("Не правильный параметр", argc[i]);
        }
        if (name == null || place == -1)
            return error("Указаны не все обязательные параметры", "[n:] или [id:]");
        else if (ch == 0)
            return error("Нужно указать хотя-бы один не обязательный параметр", "<mp:> <mip:> <p:> <mc:> <c:> <s:>");
        return true;
    }

    private void setParams(StoreItems item){
        if (max_price != -1)
            item.setMaxPrice(max_price);
        if (min_price != -1)
            item.setMinPrice(min_price);
        if (price != -1)
            item.setPrice(price);
        if (max_count != -1)
            item.setMaxCount(max_count);
        if (count != -1)
            item.setCount(count);
        if (step != -1)
            item.setStep(step);
    }

    private void updateView(Stores Store){
        Store.updateStore();
        List<HumanEntity> players = Store.getInventory().getViewers();
        Player p;
        for (HumanEntity pl: players ){
            if (pl instanceof Player){
                p = (Player)pl;
                p.updateInventory();
            }
        }
    }

    public boolean execute(){
        Stores store = null;
        if (!name.equals("all")){
            if (!smartStore.stores.containsKey(name))
                return error("Магазин не существует", name);
            store = stores.get(name);
            if (store.getStatus() == 0)
                return error("Магазин находится в режиме перемещения", name);
            if (!store.items.containsKey(place) && place !=- 2)
                return error("Этого слота нет в магазине", (place + 1)+"");
            if (place == -2){
                for (HashMap.Entry<Integer, StoreItems> item : store.items.entrySet())
                    setParams(item.getValue());
            }
            else {
                if (store.items.containsKey(place))
                    setParams(store.items.get(place));
            }
            updateView(store);
        }
        else {
            for (HashMap.Entry<String, Stores> st : smartStore.stores.entrySet()) {
                if (place == -2){
                    for (HashMap.Entry<Integer, StoreItems> item : st.getValue().items.entrySet())
                        setParams(item.getValue());
                }
                else {
                    if (st.getValue().items.containsKey(place))
                        setParams(st.getValue().items.get(place));

                }
                updateView(st.getValue());
            }
        }
        if (!name.equals("all") && store.getStatus() != 2)  return true;
        else executeDB();
        return true;
    }
    private void executeDB(){
        StringBuilder sql;
        if (ApiDatabase.DB == 0){
            sql = new StringBuilder("UPDATE stores s INNER JOIN storeitems si ON s.store_id = si.store_id SET ");
            if (max_price != -1)
                sql.append("si.max_price = ").append(max_price).append(',');
            if (min_price != -1)
                sql.append("si.min_price = ").append(min_price).append(',');
            if (price != -1)
                sql.append("si.price = ").append(price).append(',');
            if (max_count != -1)
                sql.append("si.max_count = ").append(max_count).append(',');
            if (count != -1)
                sql.append("si.count = ").append(count).append(',');
            if (step != -1)
                sql.append("si.step = ").append(step).append(',');
            sql.setCharAt(sql.length() - 1, ' ');
            if (!name.equals("all") || place != -2){
                sql.append("WHERE ");
                if (!name.equals("all"))
                    sql.append("s.name = \"").append(name).append("\" AND ");
                if (place != -2)
                    sql.append("si.place = ").append(place).append("  AND ");
                sql.replace(sql.length() -5, sql.length(), "");
            }
        }
        else{
            sql = new StringBuilder("UPDATE `storeitems` SET ");
            if (max_price != -1)
                sql.append("max_price = ").append(max_price).append(',');
            if (min_price != -1)
                sql.append("min_price = ").append(min_price).append(',');
            if (price != -1)
                sql.append("price = ").append(price).append(',');
            if (max_count != -1)
                sql.append("max_count = ").append(max_count).append(',');
            if (count != -1)
                sql.append("count = ").append(count).append(',');
            if (step != -1)
                sql.append("step = ").append(step).append(',');
            sql.setCharAt(sql.length() - 1, ' ');
            if (!name.equals("all") || place != -2){
                sql.append("WHERE ");
                if (!name.equals("all"))
                    sql.append("store_id = (SELECT `store_id` FROM `stores` WHERE name=\"").append(name).append("\") AND ");
                if (place != -2)
                    sql.append("place = ").append(place).append("  AND ");
                sql.replace(sql.length() -5, sql.length(), "");
            }
        }
        Message.toConsole(sql.toString());
        ApiDatabase.updateSQL(sql.toString());

    }
}
