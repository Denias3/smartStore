package mc.smartStore.handlers.inventoryStore;

import mc.smartStore.StoreItems;
import mc.smartStore.Stores;
import mc.smartStore.handlers.MenuElement;
import org.bukkit.Material;

public class Move extends MenuElement {
    public Move(Stores stores, int id) {
        super(stores, id);
    }

    @Override
    protected void leftClick() {
        if (!stores.items.containsKey(id)){
            if (e.getCursor() != null){
                if(!e.getCursor().getType().equals(Material.AIR) &&
                        e.getCurrentItem() == null &&
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
    protected void rightClick() {
        if (!stores.items.containsKey(id)){
            if (e.getCursor() != null){
                if(!e.getCursor().getType().equals(Material.AIR) &&
                        e.getCurrentItem() == null &&
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
    protected void shiftRightClick() {
        if (!stores.items.containsKey(id)){
            if (e.getCursor() != null){

                if(!e.getCursor().getType().equals(Material.AIR) &&
                        e.getCurrentItem() == null &&
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
    protected void shiftLeftClick() {
        if (!stores.items.containsKey(id)){
            if (e.getCursor() != null){
                if(!e.getCursor().getType().equals(Material.AIR) &&
                        e.getCurrentItem() == null &&
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
}
