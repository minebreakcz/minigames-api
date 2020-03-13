package net.graymadness.minigame_api.helper;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;

public class PriceHelper
{
    private PriceHelper() {}

    public static ChatColor getColor(Material material)
    {
        switch (material)
        {
            default:
                return ChatColor.YELLOW;
            case DIAMOND:
                return ChatColor.AQUA;
            case EMERALD:
                return ChatColor.GREEN;
            case GOLD_INGOT:
            case GOLD_NUGGET:
                return ChatColor.GOLD;
            case IRON_INGOT:
            case IRON_NUGGET:
                return ChatColor.WHITE;
            case COAL:
            case CHARCOAL:
                return ChatColor.GRAY;
            case REDSTONE:
                return ChatColor.RED;
            case LAPIS_LAZULI:
                return ChatColor.BLUE;
        }
    }

    public static BaseComponent getPriceComponent(Material material, int amount)
    {
        return ComponentBuilder.text(amount + " ")
                .extra(ComponentBuilder.translate("item." + material.getKey().getNamespace() + "." + material.getKey().getKey()).build())
                .color(getColor(material))
                .build();
    }

    public static BaseComponent getMoneyComponent(Material material)
    {
        return ComponentBuilder.translate("item." + material.getKey().getNamespace() + "." + material.getKey().getKey())
                .color(getColor(material))
                .build();
    }
}
