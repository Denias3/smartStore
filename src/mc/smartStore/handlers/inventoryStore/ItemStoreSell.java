package mc.smartStore.handlers.inventoryStore;

import mc.smartStore.Message;
import mc.smartStore.StoreItems;
import mc.smartStore.Stores;
import mc.smartStore.handlers.MenuElement;
import mc.smartStore.utils.StatusStore;
import org.bukkit.Material;

import java.util.ArrayList;

public class ItemStoreSell extends MenuElement {
    public ItemStoreSell(Stores stores, int id) {
        super(stores, id);
    }

    private boolean move(){
        if (stores.items.containsKey(id)){
            if (stores.getStatus() == StatusStore.MOVE){
                if (stores.temp == -1){
                    stores.temp = id;
                }
                else{
                    e.setCancelled(true);
                }
                return true;
            }
        }
        else {
            if (e.getCursor() != null){
                if(!e.getCursor().getType().equals(Material.AIR) &&
                        e.getCurrentItem() == null &&
                        stores.temp != -1){
                    StoreItems temp = stores.items.remove(stores.temp);
                    temp.setPlace(id);
                    stores.items.put(id, temp);
                    stores.temp = -1;
                    stores.updateStore();
                    return true;
                }
            }
        }
        return false;
    }
    private boolean moveShift(){
        if (stores.items.containsKey(id)){
            if (stores.getStatus() == StatusStore.MOVE){
                if (stores.temp == -1){
                    stores.items.remove(id);
                    e.getCurrentItem().setLore(new ArrayList<>());
                    stores.temp = -1;
                }
                else{
                    e.setCancelled(true);
                }
                return true;
            }
        }
        else {
            if (e.getCursor() != null){
                if(!e.getCursor().getType().equals(Material.AIR) &&
                        e.getCurrentItem() ==null &&
                        stores.temp != -1){
                    StoreItems temp = stores.items.remove(stores.temp);
                    temp.setPlace(id);
                    stores.items.put(id, temp);
                    stores.temp = -1;
                    stores.updateStore();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void leftClick() {
        if (move())
            return;
//        if (p.hasPermission("ss.sell")){
//            if (stores.getStatus() == StatusStore.SAVE)
//                stores.sellOne(p, stores.items.get(id),stores);
//            else {
//                p.closeInventory();
//                p.sendMessage(Message.editNotSell());
//            }
//        }
//        else {
//            p.closeInventory();
//            p.sendMessage(Message.permsSell());
//        }

    }

    @Override
    protected void rightClick() {
        move();
        if (p.hasPermission("ss.buy")){
            if (stores.getStatus() == StatusStore.SAVE)
                stores.buyOne(p, stores.items.get(id), stores);
            else {
                p.closeInventory();
                p.sendMessage(Message.editNotSell());
            }

        }
        else {
            p.closeInventory();
            p.sendMessage(Message.permsSell());
        }
    }

    @Override
    protected void shiftRightClick() {
        if (moveShift())
            return;
        if (p.hasPermission("ss.buy")){
            if (stores.getStatus() == StatusStore.SAVE)
                stores.buyMore(p, stores.items.get(id), stores);
            else if (stores.getStatus()== StatusStore.MOVE){
                moveShift();
            }
            else {
                p.closeInventory();
                p.sendMessage(Message.editNotSell());
            }

        }
        else {
            p.closeInventory();
            p.sendMessage(Message.permsSell());
        }

    }

    @Override
    protected void shiftLeftClick() {
        if (moveShift())
            return;
        p.closeInventory();
        if (p.hasPermission("ss.info") || p.hasPermission("ss.edit")){
            if (p.hasPermission("ss.edit"))
                stores.updateStats(p, id);
            else if (stores.getStatus()== StatusStore.MOVE){
                moveShift();
            }
            else
                stores.showStats(p, id);
        }
        else
            p.sendMessage(Message.permsInfo());


    }
}
