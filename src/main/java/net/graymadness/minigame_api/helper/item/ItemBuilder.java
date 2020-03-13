package net.graymadness.minigame_api.helper.item;

import net.graymadness.minigame_api.helper.ColorHelper;
import net.graymadness.minigame_api.helper.Nbt;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("unused")
public class ItemBuilder
{
    @NotNull
    protected ItemStack is;

    /**
     * Create a new ItemBuilder over an existing ItemStack.
     *
     * @param is The ItemStack to create the ItemBuilder over (clone).
     */
    public ItemBuilder(@NotNull ItemStack is)
    {
        this.is = is.clone();
    }

    /**
     * Create a new ItemBuilder from Vanilla Material.
     *
     * @param m      The material of the item.
     * @param amount The amount of the item.
     */
    public ItemBuilder(@NotNull Material m, int amount)
    {
        is = new ItemStack(m, amount);
    }

    /**
     * Create a new ItemBuilder from Vanilla Material.
     *
     * @param m The material to create the ItemBuilder with.
     */
    public ItemBuilder(@NotNull Material m)
    {
        this(m, 1);
    }

    @Deprecated
    public ItemBuilder(@NotNull Material m, short s, @Nullable String name)
    {
        is = new ItemStack(m, s);

        ItemMeta im = is.getItemMeta();
        if (im != null)
        {
            im.setDisplayName(name);
            is.setItemMeta(im);
        }
    }

    @Deprecated
    public ItemBuilder(@NotNull Material m, @Nullable String name)
    {
        is = new ItemStack(m);

        ItemMeta im = is.getItemMeta();
        if (im != null)
        {
            im.setDisplayName(name);
            is.setItemMeta(im);
        }
    }

    /**
     * Add an enchant to the item.
     *
     * @param ench  The enchant to add
     * @param level The level
     */
    @NotNull
    public ItemBuilder addEnchant(@NotNull Enchantment ench, int level)
    {
        ItemMeta im = is.getItemMeta();
        if (im == null)
            return this;

        im.addEnchant(ench, level, true);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add multiple enchants at once.
     *
     * @param enchants The enchants to add.
     */
    @NotNull
    public ItemBuilder addEnchantments(HashMap<String, Integer> enchants)
    {
        if ((enchants == null) || (enchants.size() == 0))
            return this;
        ItemMeta im = is.getItemMeta();
        if (im == null)
            return this;

        for (Map.Entry<String, Integer> entry : enchants.entrySet())
        {
            if (entry.getKey() == null)
                continue;
            Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(entry.getKey()));
            if (ench != null)
                im.addEnchant(ench, entry.getValue(), true);
        }

        is.setItemMeta(im);
        return this;
    }

    /**
     * Add multiple enchants at once.
     *
     * @param enchants The enchants to add.
     */
    @NotNull
    public ItemBuilder addEnchantments(@NotNull Map<Enchantment, Integer> enchants)
    {
        if (enchants.size() == 0)
            return this;
        ItemMeta im = is.getItemMeta();
        if (im == null)
            return this;

        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet())
        {
            if (entry.getKey() == null)
                continue;
            im.addEnchant(entry.getKey(), entry.getValue(), true);
        }

        is.setItemMeta(im);
        return this;
    }

    /**
     * Adds selected ItemFlag on the item
     *
     * @param flag selected ItemFlag
     */
    @NotNull
    public ItemBuilder addItemFlag(@NotNull ItemFlag flag)
    {
        ItemMeta im = is.getItemMeta();
        if (im == null)
            return this;

        im.addItemFlags(flag);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Hides all ItemFlags on the item
     *
     * @return ItemBuilder
     */
    @NotNull
    public ItemBuilder hideAllFlags()
    {
        ItemMeta im = is.getItemMeta();
        if (im == null)
            return this;

        for (ItemFlag flag : ItemFlag.values())
            im.addItemFlags(flag);

        is.setItemMeta(im);
        return this;
    }

    /**
     * Adds selected ItemFlag on the item by name
     *
     * @param flagName selected ItemFlag
     */
    @NotNull
    public ItemBuilder addItemFlag(String flagName)
    {
        if (flagName == null)
            return this;
        ItemFlag flag;
        try
        {
            flag = ItemFlag.valueOf(flagName);
        }
        catch (IllegalArgumentException ex)
        {
            return this;
        }

        return addItemFlag(flag);
    }

    /**
     * Adds Glowing effect on the item
     */
    @NotNull
    public ItemBuilder setGlowing()
    {
        ItemMeta im = is.getItemMeta();
        if (im == null)
            return this;

        im.addEnchant(Enchantment.DURABILITY, 0, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Adds lines of lore on the item
     *
     * @param lines Array of lore
     */
    @NotNull
    @Deprecated
    public ItemBuilder addLore(String... lines)
    {
        if (lines == null || lines.length == 0)
            return this;
        ItemMeta im = is.getItemMeta();
        if (im == null)
            return this;

        List<String> finalLore = im.getLore();
        if (finalLore == null)
            finalLore = new ArrayList<>();

        finalLore.addAll(Arrays.asList(lines));

        im.setLore(finalLore);
        is.setItemMeta(im);
        return this;
    }

    @NotNull
    public ItemBuilder setAmount(int amount)
    {
        is.setAmount(amount);
        return this;
    }

    /**
     * Change the durability of the item.
     *
     * @param dur The durability to set it to.
     */
    @NotNull
    public ItemBuilder setDurability(short dur)
    {
        ItemMeta im = is.getItemMeta();
        if (im instanceof Damageable)
        {
            ((Damageable) im).setDamage(dur);
            is.setItemMeta(im);
        }
        return this;
    }

    /**
     * Sets infinity durability on the item by setting the durability to Short.MAX_VALUE.
     */
    @NotNull
    public ItemBuilder setInfinityDurability()
    {
        ItemMeta im = is.getItemMeta();
        if (im instanceof Damageable)
        {
            ((Damageable) im).setDamage(Short.MAX_VALUE);
            is.setItemMeta(im);
        }
        return this;
    }

    @NotNull
    public ItemBuilder setUnbreakable()
    {
        ItemMeta im = is.getItemMeta();
        if (im == null)
            return this;

        im.setUnbreakable(true);

        is.setItemMeta(im);
        return this;
    }

    /**
     * Sets the armor color of a leather armor piece. Works only on leather armor pieces.
     *
     * @param color The color to set it to.
     */
    @NotNull
    public ItemBuilder setLeatherArmorColor(@NotNull Color color)
    {
        ItemMeta im = is.getItemMeta();
        if (im instanceof LeatherArmorMeta)
        {
            ((LeatherArmorMeta) im).setColor(color);
            is.setItemMeta(im);
        }
        return this;
    }

    @NotNull
    public ItemBuilder setLeatherArmorColor(@NotNull DyeColor color)
    {
        return setLeatherArmorColor(color.getColor());
    }

    @NotNull
    public ItemBuilder setLeatherArmorColor(@NotNull ChatColor color)
    {
        DyeColor dye = ColorHelper.getDyeColor(color);
        if (dye != null)
            return setLeatherArmorColor(dye);
        else
            return this;
    }

    /**
     * Re-sets the lore.
     *
     * @param lore The lore to set it to.
     */
    @NotNull
    @Deprecated
    public ItemBuilder setLore(@Nullable List<String> lore)
    {
        ItemMeta im = is.getItemMeta();
        if (im == null)
            return this;

        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Re-sets the lore.
     *
     * @param lore The lore to set it to.
     */
    @NotNull
    @Deprecated
    public ItemBuilder setLore(@NotNull String... lore)
    {
        return setLore(Arrays.asList(lore));
    }

    /**
     * Set the displayname of the item.
     *
     * @param name The name to change it to.
     */
    @NotNull
    @Deprecated
    public ItemBuilder setName(@Nullable String name)
    {
        ItemMeta im = is.getItemMeta();
        if (im == null)
            return this;

        im.setDisplayName(name);
        is.setItemMeta(im);
        return this;
    }

    @NotNull
    public ItemBuilder setLocalizedName(@NotNull String localizedKey)
    {
        /*
        // OUTDATED:
        ItemMeta im = is.getItemMeta();
        if(im == null)
            return this;

        im.setLocalizedName(localizedName);
        is.setItemMeta(im);
         */

        TranslatableComponent component = new TranslatableComponent(localizedKey);

        return setName(component);
    }

    @NotNull
    public ItemBuilder setLocalizedName(@NotNull String localizedKey, @NotNull String... with)
    {
        TranslatableComponent component = new TranslatableComponent(localizedKey);

        for (String w : with)
            component.addWith(w);

        return setName(component);
    }

    @NotNull
    public ItemBuilder setLocalizedName(@NotNull String localizedKey, @NotNull BaseComponent... with)
    {
        TranslatableComponent component = new TranslatableComponent(localizedKey);

        for (BaseComponent w : with)
        {
            if(w == null)
                component.addWith(new TextComponent(""));
            else
                component.addWith(w);
        }

        return setName(component);
    }

    @NotNull
    public ItemBuilder setLocalizedName(@NotNull String localizedKey, @NotNull ChatColor color)
    {
        TranslatableComponent component = new TranslatableComponent(localizedKey);
        component.setColor(color.asBungee());

        return setName(component);
    }

    @NotNull
    public ItemBuilder setLocalizedName(@NotNull String localizedKey, @NotNull ChatColor color, @NotNull String... with)
    {
        TranslatableComponent component = new TranslatableComponent(localizedKey);
        component.setColor(color.asBungee());

        for (String w : with)
            component.addWith(w);

        return setName(component);
    }

    @NotNull
    public ItemBuilder setLocalizedName(@NotNull String localizedKey, @NotNull ChatColor color, @NotNull BaseComponent... with)
    {
        TranslatableComponent component = new TranslatableComponent(localizedKey);
        component.setColor(color.asBungee());

        for (BaseComponent w : with)
        {
            if(w == null)
                component.addWith(new TextComponent(""));
            else
                component.addWith(w);
        }

        return setName(component);
    }

    @NotNull
    public ItemBuilder setName(@NotNull BaseComponent component)
    {
        is = Nbt.setNbt_String(is, "display.Name", ComponentSerializer.toString(component));

        return this;
    }

    @Nullable
    public BaseComponent getNameComponent()
    {
        BaseComponent[] components = ComponentSerializer.parse(Nbt.getNbt_String(is, "display.Name"));
        if(components.length == 0)
            return null;
        assert components.length == 1;
        return components[0];
    }

    @NotNull
    public ItemBuilder addLore(@NotNull BaseComponent component)
    {
        BaseComponent[] components = getLoreComponents();
        if(components == null)
            return setLore(new BaseComponent[] { component });
        else
        {
            components = Arrays.copyOf(components, components.length + 1);
            components[components.length - 1] = component;
            return setLore(components);
        }
    }

    @NotNull
    public ItemBuilder addLocalizedLore(@NotNull String localizedKey)
    {
        TranslatableComponent component = new TranslatableComponent(localizedKey);
        component.setColor(net.md_5.bungee.api.ChatColor.WHITE);

        return addLore(component);
    }

    @NotNull
    public ItemBuilder addLocalizedLore(@NotNull String localizedKey, @NotNull BaseComponent... with)
    {
        TranslatableComponent component = new TranslatableComponent(localizedKey);
        component.setColor(net.md_5.bungee.api.ChatColor.WHITE);

        for(BaseComponent w : with)
        {
            if(w == null)
                component.addWith(new TextComponent());
            else
                component.addWith(w);
        }

        return addLore(component);
    }

    @NotNull
    public ItemBuilder addLocalizedLore(@NotNull String localizedKey, @NotNull String... with)
    {
        TranslatableComponent component = new TranslatableComponent(localizedKey);
        component.setColor(net.md_5.bungee.api.ChatColor.WHITE);

        for(String w : with)
            component.addWith(w);

        return addLore(component);
    }

    @NotNull
    public ItemBuilder setLore(@NotNull BaseComponent[] components)
    {
        String[] texts = new String[components.length];
        for(int i = 0; i < components.length; i++)
            texts[i] = ComponentSerializer.toString(components[i]);

        is = Nbt.setNbt_StringArray(is, "display.Lore", texts);

        return this;
    }

    @NotNull
    public ItemBuilder setLocalizedLore(@NotNull String[] localizedKeys)
    {
        String[] texts = new String[localizedKeys.length];
        for(int i = 0; i < localizedKeys.length; i++)
        {
            TranslatableComponent component = new TranslatableComponent(localizedKeys[0]);
            component.setColor(net.md_5.bungee.api.ChatColor.WHITE);
            texts[i] = ComponentSerializer.toString(component);
        }

        is = Nbt.setNbt_StringArray(is, "display.Lore", texts);

        return this;
    }

    @Nullable
    public BaseComponent[] getLoreComponents()
    {
        String[] textLore = Nbt.getNbt_StringArray(is, "display.Lore");
        if(textLore == null)
            return new BaseComponent[0];

        BaseComponent[] lore = new BaseComponent[textLore.length];
        for(int i = 0; i < textLore.length; i++)
        {
            BaseComponent[] components = ComponentSerializer.parse(textLore[i]);
            assert components.length <= 1;
            lore[i] = components.length == 0 ? null : components[0];
        }
        return lore;
    }

    @NotNull
    public ItemBuilder setAttackDamageBonus(double amount)
    {
        ItemMeta im = is.getItemMeta();
        if (im == null)
            return this;

        // Remove old
        im.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);

        // Set new
        im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", amount, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        return this;
    }

    @NotNull
    public ItemBuilder setAttackSpeedBonus(double amount)
    {
        ItemMeta im = is.getItemMeta();
        if (im == null)
            return this;

        // Remove old
        im.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);

        // Set new
        im.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", amount, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        return this;
    }

    public static final String Attribute_CanPlaceOn = "CanPlaceOn";

    @NotNull
    public ItemBuilder addCanPlaceOn(Material... materials)
    {
        String[] existing = Nbt.getNbt_StringArray(is, Attribute_CanPlaceOn);

        List<String> newList = new ArrayList<>(Arrays.asList(existing));
        for (Material mat : materials)
            newList.add(mat.name());

        is = Nbt.setNbt_StringArray(is, Attribute_CanPlaceOn, newList);
        return this;
    }

    @NotNull
    public ItemBuilder setCanPlaceOn(Material... materials)
    {
        List<String> list = new ArrayList<>();
        for (Material mat : materials)
            list.add(mat.name());

        is = Nbt.setNbt_StringArray(is, Attribute_CanPlaceOn, list);
        return this;
    }

    public static final String Attribute_CanDestroy = "CanDestroy";

    @NotNull
    public ItemBuilder addCanDestroy(Material... materials)
    {
        String[] existing = Nbt.getNbt_StringArray(is, Attribute_CanDestroy);

        List<String> newList = new ArrayList<>(Arrays.asList(existing));
        for (Material mat : materials)
            newList.add(mat.name());

        is = Nbt.setNbt_StringArray(is, Attribute_CanDestroy, newList);
        return this;
    }

    @NotNull
    public ItemBuilder setCanDestroy(Material... materials)
    {
        List<String> list = new ArrayList<>();
        for (Material mat : materials)
            list.add(mat.name());

        is = Nbt.setNbt_StringArray(is, Attribute_CanDestroy, list);
        return this;
    }

    @NotNull
    public ItemBuilder setArmor(int amount)
    {
        ItemMeta im = is.getItemMeta();
        if (im == null)
            return this;

        // Remove old
        im.removeAttributeModifier(Attribute.GENERIC_ARMOR);

        // Set new
        im.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", amount, AttributeModifier.Operation.ADD_NUMBER));

        return this;
    }

    @NotNull
    public ItemBuilder setArmorToughness(int amount)
    {
        ItemMeta im = is.getItemMeta();
        if (im == null)
            return this;

        // Remove old
        im.removeAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS);

        // Set new
        im.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", amount, AttributeModifier.Operation.ADD_NUMBER));

        return this;
    }

    @NotNull
    public ItemBuilder setNbt_String(String key, String value)
    {
        is = Nbt.setNbt_String(is, key, value);
        return this;
    }

    @NotNull
    public ItemBuilder setNbt_Int(String key, int value)
    {
        is = Nbt.setNbt_Int(is, key, value);
        return this;
    }

    @NotNull
    public ItemBuilder setNbt_Long(String key, long value)
    {
        is = Nbt.setNbt_Long(is, key, value);
        return this;
    }

    @NotNull
    public ItemBuilder setModelId(int modelId)
    {
        is = Nbt.setNbt_Int(is, "CustomModelData", modelId);

        return this;
    }

    public static int getModelId(@NotNull ItemStack is)
    {
        return Nbt.getNbt_Int(is, "CustomModelData", 0);
    }

    /**
     * Build Item from ItemBuilder to ItemStack
     *
     * @return ItemStack of create item
     */
    @NotNull
    public ItemStack build()
    {
        return is;
    }
}
