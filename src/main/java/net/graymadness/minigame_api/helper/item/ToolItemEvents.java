package net.graymadness.minigame_api.helper.item;

import net.graymadness.minigame_api.MinigameAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToolItemEvents implements Listener
{
    @NotNull
    private final MinigameAPI plugin;
    public ToolItemEvents(@NotNull MinigameAPI plugin)
    {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onInventoryClick(InventoryClickEvent event) {
        ItemStack is = event.getCurrentItem();
        if(is == null || is.getType() == Material.AIR)
            return;

        HumanEntity entity = event.getWhoClicked();
        if(!(entity instanceof Player))
            return;
        Player player = (Player) entity;

        for (ToolItem tool : ToolItem.getAllItems()) {
            if (tool.isThisItem(is)) {
                tool.onUse(player);
                return;
            }
        }
    }

    private boolean processEvent(@NotNull Player player, @Nullable EquipmentSlot slot, @Nullable Entity entity, @Nullable Block block) {
        if(slot == null)
            return false;
        EntityEquipment equipment = player.getEquipment();
        if(equipment == null)
            return false;

        ItemStack is;
        switch (slot) {
            case HAND:
                is = equipment.getItemInMainHand();
                break;
            case OFF_HAND:
                is = equipment.getItemInOffHand();
                break;
            default:
                return false;
        }
        if(is == null || is.getType() == Material.AIR)
            return false;

        for (ToolItem tool : ToolItem.getAllItems()) {
            if (tool.isThisItem(is)) {
                if(entity != null)
                    tool.onUseAtEntity(player, entity);
                else if(block != null)
                    tool.onUseAtBlock(player, block);
                else
                    tool.onUse(player);
                return true;
            }
        }

        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onItemUse(PlayerInteractAtEntityEvent event) {
        if(processEvent(event.getPlayer(), event.getHand(), event.getRightClicked(), null))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onItemUse(PlayerInteractEvent event) {
        if(processEvent(event.getPlayer(), event.getHand(), null, event.getClickedBlock()))
            event.setCancelled(true);
    }

    private boolean isToolItem(@NotNull ItemStack is) {
        if(is.getType() == Material.AIR)
            return false;

        for (ToolItem tool : ToolItem.getAllItems())
            if (tool.isThisItem(is))
                return true;
        return false;
    }

    private boolean isToolItem(@NotNull Item item) {
        return isToolItem(item.getItemStack());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onItemDropped(PlayerDropItemEvent event) {
        if(isToolItem(event.getItemDrop()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onItemDropped(EntityDropItemEvent event) {
        if(isToolItem(event.getItemDrop()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onItemDropped(EntitySpawnEvent event) {
        if(event.getEntityType() == EntityType.DROPPED_ITEM && isToolItem((Item) event.getEntity()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onItemPicked(InventoryCreativeEvent event) {
        if(event.getClick() == ClickType.MIDDLE && isToolItem(event.getCursor()))
            event.setCancelled(true);
    }
}
