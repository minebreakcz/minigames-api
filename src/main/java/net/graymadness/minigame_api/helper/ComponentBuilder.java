package net.graymadness.minigame_api.helper;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ComponentBuilder
{
    @NotNull
    public static ComponentBuilder empty() {
        return new ComponentBuilder(new TextComponent());
    }
    @NotNull
    public static ComponentBuilder text(@NotNull String text) {
        return new ComponentBuilder(new TextComponent(text));
    }
    @NotNull
    public static ComponentBuilder translate(@NotNull String key) {
        return new ComponentBuilder(new TranslatableComponent(key));
    }
    @NotNull
    public static ComponentBuilder translate(@NotNull String key, @NotNull BaseComponent... with) {
        TranslatableComponent component = new TranslatableComponent(key);
        for(BaseComponent w : with)
            component.addWith(w);
        return new ComponentBuilder(component);
    }
    @NotNull
    public static ComponentBuilder legacy(@NotNull String plainText) {
        TextComponent component = new TextComponent();
        for(BaseComponent w : TextComponent.fromLegacyText(plainText))
            if(w != null)
                component.addExtra(w);
        return new ComponentBuilder(component);
    }
    @NotNull
    public static ComponentBuilder item(@NotNull ItemStack is) {
        String name_s = Nbt.getNbt_String(is, "display.Name");
        @NotNull
        BaseComponent[] name_bc = ComponentSerializer.parse(name_s);

        BaseComponent component = name_bc.length == 0 || name_bc[0] == null ? new TranslatableComponent("item." + is.getType().getKey().getNamespace() + "." + is.getType().getKey().getKey()) : name_bc[0];
        for(int i = 1; i < name_bc.length; i++)
            component.addExtra(name_bc[i]);

        component.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_ITEM,
                new BaseComponent[] { new TextComponent(convertItemStackToJson(is)) }
                ));

        return new ComponentBuilder(component);
    }
    @NotNull
    public static ComponentBuilder key(@NotNull String key) {
        return new ComponentBuilder(new KeybindComponent(key));
    }


    public static String convertItemStackToJson(ItemStack itemStack) {
        net.minecraft.server.v1_15_R1.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
        return "{ \"id\":\"" + itemStack.getType().getKey().toString() + "\" \"tag\":" + (nms.getTag() == null ? "{}" : nms.getTag().asString()) + "}";
    }


    public ComponentBuilder(@NotNull BaseComponent component) {
        this.component = component;
    }

    @NotNull
    private BaseComponent component;

    public ComponentBuilder color(@Nullable ChatColor color) {
        component.setColor(color);
        return this;
    }
    public ComponentBuilder color(@Nullable org.bukkit.ChatColor color) {
        component.setColor(color == null ? null : color.asBungee());
        return this;
    }

    public ComponentBuilder bold(boolean value) {
        component.setBold(value);
        return this;
    }
    public ComponentBuilder italic(boolean value) {
        component.setItalic(value);
        return this;
    }
    public ComponentBuilder underlined(boolean value) {
        component.setUnderlined(value);
        return this;
    }
    public ComponentBuilder strikethrough(boolean value) {
        component.setStrikethrough(value);
        return this;
    }

    public ComponentBuilder insertion(String value) {
        component.setInsertion(value);
        return this;
    }

    public ComponentBuilder click(ClickEvent event) {
        component.setClickEvent(event);
        return this;
    }
    public ComponentBuilder hover(HoverEvent event) {
        component.setHoverEvent(event);
        return this;
    }

    public ComponentBuilder extra(BaseComponent extraComponent) {
        component.addExtra(extraComponent);
        return this;
    }

    public BaseComponent build() {
        return component;
    }
}
