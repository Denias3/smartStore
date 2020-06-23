package mc.smartStore.handlers.inventoryStore;

import mc.smartStore.Message;
import mc.smartStore.Stores;
import mc.smartStore.db.ApiDatabase;
import mc.smartStore.handlers.MenuElement;

public class SaveStore extends MenuElement {
    public SaveStore(Stores stores, int id) {
        super(stores, id);
    }

    @Override
    protected void leftClick() {
        if (p.hasPermission("ss.edit") || p.hasPermission("ss.save"))
        if (ApiDatabase.saveShop(stores)) {
            p.sendMessage(Message.saveStore(stores.getName()));
            p.updateInventory();
        } else {
            p.closeInventory();
            p.sendMessage(Message.saveStoreError());
        }
    }

    @Override
    protected void rightClick() {

    }

    @Override
    protected void shiftRightClick() {

    }

    @Override
    protected void shiftLeftClick() {

    }
}
