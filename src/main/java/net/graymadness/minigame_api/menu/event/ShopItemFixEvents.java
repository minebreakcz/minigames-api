package net.graymadness.minigame_api.menu.event;

import net.graymadness.minigame_api.MinigameAPI;
import net.graymadness.minigame_api.helper.Nbt;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ShopItemFixEvents implements Listener
{
    @NotNull
    private final MinigameAPI plugin;
    public ShopItemFixEvents(@NotNull MinigameAPI plugin)
    {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private boolean isShop(ItemStack is)
    {
        return Nbt.getNbt_Bool(is, "ShopItem", false);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onItemSpawn(ItemSpawnEvent event)
    {
        Item item = event.getEntity();
        ItemStack is = item.getItemStack();

        if(isShop(is))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onItemClick(InventoryClickEvent event)
    {
        if (event.getInventory().getHolder() != event.getWhoClicked())
            return;

        // Current
        {
            ItemStack is = event.getCurrentItem();
            if (isShop(is))
                event.setCurrentItem(null);
        }

        // Cursor
        {
            ItemStack is = event.getCursor();
            if(isShop(is))
                event.getWhoClicked().setItemOnCursor(null);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onItemDrag(InventoryDragEvent event)
    {
        if (event.getInventory().getHolder() != event.getWhoClicked())
            return;

        ItemStack is = event.getOldCursor();
        if(isShop(is))
        {
            event.setCancelled(true);
            event.setCursor(null);
            event.getWhoClicked().setItemOnCursor(null);
        }
    }
}
