package mc.smartStore.handlers.inventoryStore;

import mc.smartStore.Message;
import mc.smartStore.SmartStore;
import mc.smartStore.Stores;
import mc.smartStore.handlers.MenuElement;

public class DeleteStore extends MenuElement {
    public DeleteStore(Stores stores, int id) {
        super(stores, id);
    }

    @Override
    protected void leftClick() {
        if (p.hasPermission("ss.edit") || p.hasPermission("ss.delete")){
            p.closeInventory();
            SmartStore.stores.remove(stores.getName());
            p.sendMessage(Message.deleteStore(stores.getName()));
        }
        else {
            p.closeInventory();
            p.sendMessage(Message.deleteStoreNotPerms());
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
