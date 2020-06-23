package mc.smartStore.handlers;

import mc.smartStore.Stores;
import mc.smartStore.utils.StatusStore;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public abstract class MenuElement {
    protected InventoryClickEvent e;
    protected Stores stores;
    protected Inventory i;
    public int id;
    protected Player p;

    public MenuElement(Stores stores, int id) {
        this.i = stores.getInventory();
        this.stores = stores;
        this.id = id;

    }

    private void clickChoice(ClickType click){
        if (click == ClickType.LEFT)
            leftClick();
        else if (click == ClickType.RIGHT)
            rightClick();
        else if (click == ClickType.SHIFT_LEFT)
            shiftLeftClick();
        else if (click == ClickType.SHIFT_RIGHT)
            shiftRightClick();
    }

    public void click(Player p, InventoryClickEvent e){
        this.p = p;
        this.e = e;
        if (id >= i.getSize() - 9 | stores.getStatus() != StatusStore.MOVE)
            e.setCancelled(true);
        clickChoice(e.getClick());

    }
    protected abstract void leftClick();
    protected abstract void rightClick();
    protected abstract void shiftRightClick();
    protected abstract void shiftLeftClick();
}
