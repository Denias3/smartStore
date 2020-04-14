package russian.denis;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class Handler implements Listener {
    private final smartStore plugin;

    public Handler(smartStore plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void shopClick(InventoryClickEvent e) {
        Inventory i = e.getInventory();
        Player p =  (Player) e.getView().getPlayer();

        for (int j = 0; j < plugin.stores.length;j++)
        {
            if(i.equals(plugin.stores[j]))
            {

                if (i.equals(e.getClickedInventory()))
                {
                    e.setCancelled(true);
                    if (e.isLeftClick() && e.isShiftClick())
                        Utils.sellAll(p, e.getCurrentItem(), plugin.stores[j]);
                    else if (e.isLeftClick())
                        Utils.sellOne(p, e.getCurrentItem(), plugin.stores[j]);

                }
            }
        }

    }
    @EventHandler
    public void shopMove(InventoryDragEvent e) {
        Inventory de = e.getInventory();
        for (int j = 0; j < plugin.stores.length;j++)
        {
            if(de.equals(plugin.stores[j]))
                e.setCancelled(true);
        }


    }
}
