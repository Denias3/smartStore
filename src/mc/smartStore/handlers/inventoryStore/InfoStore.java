package mc.smartStore.handlers.inventoryStore;

import mc.smartStore.Message;
import mc.smartStore.Stores;
import mc.smartStore.handlers.MenuElement;

public class InfoStore extends MenuElement {
    public InfoStore(Stores stores,  int id) {
        super(stores, id);
    }

    @Override
    protected void leftClick() {
        p.closeInventory();
        p.sendMessage(Message.infoStore(stores));
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
