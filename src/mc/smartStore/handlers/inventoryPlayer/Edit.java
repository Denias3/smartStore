package mc.smartStore.handlers.inventoryPlayer;

import mc.smartStore.Message;
import mc.smartStore.Stores;
import mc.smartStore.db.ApiDatabase;
import mc.smartStore.handlers.MenuElement;
import mc.smartStore.utils.PermissionClick;
import mc.smartStore.utils.StatusStore;
import org.bukkit.event.inventory.ClickType;

public class Edit extends MenuElement {
    public Edit(Stores stores,  int id) {
        super(stores,  id);
    }

    @Override
    protected void leftClick() {
        if (p.hasPermission("ss.edit")){
            ApiDatabase.deleteStore(stores.getName());
            stores.setStatus(StatusStore.PRICE);
            stores.createMenuEdit();
            p.updateInventory();
        }
        else {
            p.closeInventory();
            p.sendMessage(Message.permsEdit());
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
