package net.graymadness.minigame_api.menu;

import net.graymadness.minigame_api.helper.ChatInfo;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.graymadness.minigame_api.helper.Nbt;
import net.graymadness.minigame_api.helper.PriceHelper;
import net.graymadness.minigame_api.helper.item.ItemBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.ImmutableTriple;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Triple;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class ItemShopMenu extends ItemMenu
{
    public ItemShopMenu(@NotNull Type type, @NotNull BaseComponent title)
    {
        super(type, title);

        items = new ItemStack[type.size];
    }

    @Deprecated
    public ItemShopMenu(@NotNull Type type, @NotNull String title)
    {
        this(type, ComponentBuilder.legacy(title).build());
    }

    public void updateInventory() {
        Inventory inventory = getInventory();

        int i = 0;
        for(ItemStack is : items)
            inventory.setItem(i++, is);
    }

    @Override
    protected void onClick(@NotNull Player player, int slot, @Nullable ItemStack item, @Nullable ItemStack cursor, @NotNull InventoryClickEvent event) {
        if(item == null || item.getType() == Material.AIR)
            return;

        Triple<ItemStack, Material, Integer> shopItem = this.itemMap.getOrDefault(item, null);

        if(shopItem == null)
            return;

        ItemStack shopItemStack = shopItem.getLeft();
        Material moneyType = shopItem.getMiddle();
        int itemPrice = shopItem.getRight();

        if(shopItemStack == null)
            return;

        if(canPay(player, moneyType, itemPrice)) {
            // Pay for the item
            pay(player, moneyType, itemPrice);

            // Git items to player
            for(ItemStack isDrop : player.getInventory().addItem(shopItemStack.clone()).values())
                player.getWorld().dropItemNaturally(player.getLocation(), isDrop); //THINK drop items which cannot be placed to player's inventory

            // Send info message
            ChatInfo.SUCCESS.send(
                    player,
                    ComponentBuilder.translate(
                            "shop.buy.ok",
                            ComponentBuilder.item(item).build(),
                            ComponentBuilder.text(itemPrice + "").build(),
                            PriceHelper.getMoneyComponent(moneyType)
                    ).build()
            );
        }
        else
            ChatInfo.WARNING.send(
                    player,
                    ComponentBuilder.translate(
                            "shop.buy.no_money",
                            PriceHelper.getMoneyComponent(moneyType)
                            ).build()
                    );
    }

    public static boolean canPay(Player player, Material material, int amount)
    {
        PlayerInventory inventory = player.getInventory();
        for(ItemStack is : inventory)
        {
            if(is == null || is.getType() != material)
                continue;
            amount -= is.getAmount();

            if(amount <= 0)
                return true;
        }

        return amount <= 0;
    }

    public static void pay(Player player, Material material, int amount)
    {
        PlayerInventory inventory = player.getInventory();
        for(int i = 0; i < inventory.getSize() && amount > 0; i++)
        {
            ItemStack is = inventory.getItem(i);
            if(is == null || is.getType() != material)
                continue;

            if(amount > is.getAmount())
            {
                inventory.setItem(i, null);
            }
            else
            {
                ItemStack is_clone = is.clone();
                is_clone.setAmount(is.getAmount() - amount);
                inventory.setItem(i, is_clone);
            }

            amount -= is.getAmount();
        }
    }

    // Key = Display item
    // Value:
    // - Item to give
    // - Type of money
    // - Amount of money
    private Map<ItemStack, Triple<ItemStack, Material, Integer>> itemMap = new HashMap<>();
    private ItemStack[] items;

    protected void setItem(int index, @NotNull ItemStack isDisplay, @Nullable ItemStack isGive, @NotNull Material moneyType, int price) {
        int size = getSize();
        if(index < 0 || index >= size)
            return;

        isDisplay = Nbt.setNbt_Bool(isDisplay, "ShopItem", true);

        if(isGive != null)
            this.itemMap.put(isDisplay, new ImmutableTriple<>(isGive, moneyType, price));

        this.items[index] = isDisplay;
    }

    protected int addItem(@NotNull ItemStack isDisplay, @Nullable ItemStack isGive, @NotNull Material moneyType, int price) {
        int index;
        int size = getSize();
        for(index = 0; index < size; index++)
            if(items[index] == null)
                break;
        if(index == size)
            return -1;

        isDisplay = Nbt.setNbt_Bool(isDisplay, "ShopItem", true);

        if(isGive != null)
            this.itemMap.put(isDisplay, new ImmutableTriple<>(isGive, moneyType, price));

        this.items[index] = isDisplay;
        return index;
    }

    protected int addItem(@NotNull ItemStack is, @NotNull Material moneyType, int price) {
        ItemStack isDisplay = new ItemBuilder(is.clone())
                .addLore(PriceHelper.getPriceComponent(moneyType, price))
                .build();
        return addItem(isDisplay, is, moneyType, price);
    }

    protected void setItem(int index, @NotNull ItemStack is, @NotNull Material moneyType, int price) {
        ItemStack isDisplay = new ItemBuilder(is)
                .addLore(PriceHelper.getPriceComponent(moneyType, price))
                .build();
        setItem(index, isDisplay, is, moneyType, price);
    }

    @Override
    protected void onOpen(@NotNull Player player)
    {
        updateInventory();
    }

    @Override
    protected void onClose(@NotNull Player player)
    {

    }
}
