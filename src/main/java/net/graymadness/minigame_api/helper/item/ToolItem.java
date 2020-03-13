package net.graymadness.minigame_api.helper.item;

import net.graymadness.minigame_api.helper.Nbt;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ToolItem {

    @NotNull
    private static final List<ToolItem> allItems = new ArrayList<>();

    public static Collection<ToolItem> getAllItems() {
        return allItems;
    }

    public ToolItem(@NotNull Material material, @NotNull UseFunction function, @NotNull BaseComponent name, @NotNull BaseComponent... lore) {
        this.material = material;
        this.useFunction = function;
        this.name = name;
        this.lore = lore;

        allItems.add(this);

        this.uniqueId = getNextUniqueId();
    }
    public ToolItem(@NotNull Material material, @NotNull UseFunction function, @NotNull String localizedDisplayName, @NotNull BaseComponent... lore) {
        this(material, function, new TranslatableComponent(localizedDisplayName), lore);
    }

    @NotNull
    private final Material material;
    @NotNull
    private UseFunction useFunction;
    @NotNull
    private final BaseComponent name;
    @NotNull
    private final BaseComponent[] lore;

    private final long uniqueId;
    public long getUniqueId()
    {
        return uniqueId;
    }
    public boolean isThisItem(@NotNull ItemStack is)
    {
        return Nbt.getNbt_Long(is, "ToolItem", 0) == uniqueId;
    }

    private ItemStack is = null;

    @NotNull
    public ItemStack getItem() {
        if(is == null)
            is = new ItemBuilder(material)
                    .hideAllFlags()
                    .setName(name)
                    .setLore(lore)
                    .setNbt_Long("ToolItem", uniqueId)
                    .build();
        return is;
    }

    private static Random _UniqueIncrementRandom = new Random();

    private static long _LastUniqueId = 0;
    private static long getNextUniqueId()
    {
        _LastUniqueId += 1 + _UniqueIncrementRandom.nextInt(256);
        return _LastUniqueId;
    }

    public static abstract class UseFunction
    {
        /**
         * General use
         * @param player player interacting with the item
         */
        public void onUse(@NotNull Player player) {}

        public boolean onUseAtEntity(@NotNull Player player, @NotNull Entity entity) {
            return false;
        }
        public boolean onUseAtBlock(@NotNull Player player, @NotNull Block block) {
            return false;
        }
    }

    public void onUse(@NotNull Player player) {
        useFunction.onUse(player);
    }

    public void onUseAtEntity(@NotNull Player player, @NotNull Entity entity) {
        if(!useFunction.onUseAtEntity(player, entity))
            useFunction.onUse(player);
    }

    public void onUseAtBlock(@NotNull Player player, @NotNull Block block) {
        if(!useFunction.onUseAtBlock(player, block))
            useFunction.onUse(player);
    }
}
