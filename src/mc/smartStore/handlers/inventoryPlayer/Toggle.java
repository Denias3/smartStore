package mc.smartStore.handlers.inventoryPlayer;

import mc.smartStore.Message;
import mc.smartStore.Stores;
import mc.smartStore.handlers.MenuElement;
import mc.smartStore.utils.PermissionClick;
import org.bukkit.event.inventory.ClickType;

public class Toggle extends MenuElement {
    public Toggle(Stores stores, int id) {
        super(stores, id);
    }

    @Override
    protected void leftClick() {
        if (p.hasPermission("ss.edit") || p.hasPermission("ss.toggle")){
            stores.toggleStatus();
            stores.updateStore(p);
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
