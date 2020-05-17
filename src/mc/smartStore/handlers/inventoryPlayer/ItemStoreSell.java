package mc.smartStore.handlers.inventoryPlayer;

import mc.smartStore.Message;
import mc.smartStore.StoreItems;
import mc.smartStore.Stores;
import mc.smartStore.handlers.MenuElement;
import mc.smartStore.utils.PermissionClick;
import mc.smartStore.utils.StatusStore;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;

public class ItemStoreSell extends MenuElement {
    public ItemStoreSell(Stores stores, int id) {
        super(stores, id);
    }

    private void move(){
        if (stores.items.containsKey(id)){
            if (stores.temp == -1){
                stores.temp = id;
            }
            else{
                e.setCancelled(true);
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
                }
            }
        }
    }
    private void moveShift(){
        if (stores.items.containsKey(id)){
            if (stores.temp == -1){
                stores.items.remove(id);
                e.getCurrentItem().setLore(new ArrayList<>());
                stores.temp = -1;
            }
            else{
                e.setCancelled(true);
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
                }
            }
        }
    }

    @Override
    protected void leftClick() {
        if (p.hasPermission("ss.sell")){
            if (stores.getStatus() == StatusStore.SAVE)
                stores.sellOne(p, stores.items.get(id));
            else if (stores.getStatus()== StatusStore.MOVE){
               move();
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
    protected void rightClick() {
        if (stores.getStatus()== StatusStore.MOVE){
            move();
        }
    }

    @Override
    protected void shiftRightClick() {
        if (stores.getStatus()== StatusStore.MOVE){
            moveShift();
        }
        else{
            p.closeInventory();
            if (p.hasPermission("ss.info") || p.hasPermission("ss.edit")){
                if (p.hasPermission("ss.edit"))
                    stores.updateStats(p, id);
                else
                    stores.showStats(p, id);
            }
            else
                p.sendMessage(Message.permsInfo());

        }

    }


    @Override
    protected void shiftLeftClick() {

        if (p.hasPermission("ss.sell")){
            if (stores.getStatus() == StatusStore.SAVE)
                stores.sellAll(p, stores.items.get(id));
            if (stores.getStatus()== StatusStore.MOVE){
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
}
