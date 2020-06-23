package mc.smartStore.commands;

import mc.smartStore.Message;
import mc.smartStore.StoreItems;
import mc.smartStore.Stores;
import mc.smartStore.SmartStore;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.HashMap;
import java.util.UUID;


public class Create {

    public static void setTypeVillager(Villager v, String type){
        if (type.equals("пустыня")){
            v.setVillagerType(Villager.Type.DESERT);
        }
        else if (type.equals("джунгли")){
            v.setVillagerType(Villager.Type.JUNGLE);
        }
        else if (type.equals("равнины")){
            v.setVillagerType(Villager.Type.PLAINS);
        }
        else if (type.equals("саванна")){
            v.setVillagerType(Villager.Type.SAVANNA);
        }
        else if (type.equals("снег")){
            v.setVillagerType(Villager.Type.SNOW);
        }
        else if (type.equals("болото")){
            v.setVillagerType(Villager.Type.SWAMP);
        }
        else if (type.equals("тайга")){
            v.setVillagerType(Villager.Type.TAIGA);
        }
    }
    public static void setProfVillager(Villager v, String prof){
        if (prof.equals("бронник")){
            v.setProfession(Villager.Profession.ARMORER);
        }
        else if (prof.equals("пастух")){
            v.setProfession(Villager.Profession.SHEPHERD);
        }
        else if (prof.equals("мясник")){
            v.setProfession(Villager.Profession.BUTCHER);
        }
        else if (prof.equals("картограф")){
            v.setProfession(Villager.Profession.CARTOGRAPHER);
        }
        else if (prof.equals("священник")){
            v.setProfession(Villager.Profession.CLERIC);
        }
        else if (prof.equals("фермер")){
            v.setProfession(Villager.Profession.FARMER);
        }
        else if (prof.equals("рыбак")){
            v.setProfession(Villager.Profession.FISHERMAN);
        }
        else if (prof.equals("fletcher")){
            v.setProfession(Villager.Profession.FLETCHER);
        }
        else if (prof.equals("кожевник")){
            v.setProfession(Villager.Profession.LEATHERWORKER);
        }
        else if (prof.equals("библиотекарь")){
            v.setProfession(Villager.Profession.LIBRARIAN);
        }
        else if (prof.equals("каменщик")){
            v.setProfession(Villager.Profession.MASON);
        }
        else if (prof.equals("бомж")){
            v.setProfession(Villager.Profession.NITWIT);
        }
        else if (prof.equals("инструментальщик")){
            v.setProfession(Villager.Profession.TOOLSMITH);
        }
        else if (prof.equals("оружейник")){
            v.setProfession(Villager.Profession.WEAPONSMITH);
        }

    }

    public static void create (String []args, Player p){
        if (p.hasPermission("ss.create") || p.hasPermission("ss.edit")) {
            if (args.length > 1) {
                if (args[1].length() <= 3 || args[1].length() > 20){
                    p.sendMessage(Message.prefix +"Название магазина должно быть больше чем 3 символа и меньше чем 20");
                    return ;
                }
                else if (!args[1].matches("[a-zA-Zа-яА-Я0-9_]+")){
                    p.sendMessage(Message.prefix +"Не правильное название магазина");
                    return ;
                }
                if (!SmartStore.stores.containsKey(args[1])){
                    double capital = 0;
                    if (args.length > 2){
                        int size = Integer.parseInt(args[2]);
                        if (size <= 5 && size >= 1)
                        {
                            Location loc = p.getLocation();
                            Villager v = (Villager) p.getWorld().spawnEntity(loc, EntityType.VILLAGER);
                            v.setCustomName(args[1]);
                            v.setAI(false);
                            if (args.length > 3)
                                setTypeVillager(v, args[3]);
                            if (args.length > 4)
                                setProfVillager(v, args[4]);
                            if (args.length > 5)
                                capital = Integer.parseInt(args[5]);
                            v.setInvulnerable(true);
//                            v.setGravity(false);
                            UUID u = v.getUniqueId();
                            SmartStore.stores.put(args[1], new Stores(size, args[1], p, u, capital));
                        }
                        else
                            p.sendMessage(Message.prefix +"Не верное количество строк в магазине, нужно от 1 до 5");

                    }
                    else
                        p.sendMessage(Message.prefix +"Нужно указать количество строк в магазинае от 1 до 5");
                }
                else
                    p.sendMessage(Message.prefix +"Этот магазин уже существует");
            }
            else
                p.sendMessage(Message.prefix +"/st create [name]");
        }
        else
            p.sendMessage( Message.prefix +"§cУ тебя нет прав");
    }

}
