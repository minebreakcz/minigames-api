package net.graymadness.minigame_api.helper;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorHelper
{
    public static ChatColor colorByPercent(int value) {
        if (value >= 75)
            return ChatColor.RED;
        if (value >= 50)
            return ChatColor.GOLD;
        if (value >= 25)
            return ChatColor.YELLOW;
        if (value >= 0)
            return ChatColor.GREEN;
        return ChatColor.WHITE;
    }

    /**
     * Creates dye item from DyeColor
     *
     * @param color dye color
     * @return new instance of ItemStack with result dye color. May be AIR when dye not recognized or is null.
     */
    @NotNull
    public static ItemStack createDye(@Nullable DyeColor color)
    {
        if (color == null)
            return new ItemStack(Material.AIR);

        switch (color)
        {
            case WHITE:
                return new ItemStack(Material.WHITE_DYE);
            case ORANGE:
                return new ItemStack(Material.ORANGE_DYE);
            case MAGENTA:
                return new ItemStack(Material.MAGENTA_DYE);
            case LIGHT_BLUE:
                return new ItemStack(Material.LIGHT_BLUE_DYE);
            case YELLOW:
                return new ItemStack(Material.YELLOW_DYE);
            case LIME:
                return new ItemStack(Material.LIME_DYE);
            case PINK:
                return new ItemStack(Material.PINK_DYE);
            case GRAY:
                return new ItemStack(Material.GRAY_DYE);
            case LIGHT_GRAY:
                return new ItemStack(Material.LIGHT_GRAY_DYE);
            case CYAN:
                return new ItemStack(Material.CYAN_DYE);
            case PURPLE:
                return new ItemStack(Material.PURPLE_DYE);
            case BLUE:
                return new ItemStack(Material.BLUE_DYE);
            case BROWN:
                return new ItemStack(Material.BROWN_DYE);
            case GREEN:
                return new ItemStack(Material.GREEN_DYE);
            case RED:
                return new ItemStack(Material.RED_DYE);
            case BLACK:
                return new ItemStack(Material.BLACK_DYE);

            default:
                return new ItemStack(Material.AIR);
        }
    }

    @Nullable
    public static DyeColor getDyeColor(@NotNull ChatColor color)
    {
        switch (color)
        {
            case BLACK:
                return DyeColor.BLACK;
            case DARK_BLUE:
                return DyeColor.BLUE;
            case DARK_GREEN:
                return DyeColor.GREEN;
            case DARK_AQUA:
                return null; // No equivalent color
            case DARK_RED:
                return DyeColor.RED;
            case DARK_PURPLE:
                return DyeColor.MAGENTA;
            case GOLD:
                return DyeColor.ORANGE;
            case GRAY:
                return DyeColor.LIGHT_GRAY;
            case DARK_GRAY:
                return DyeColor.GRAY;
            case BLUE:
                return DyeColor.LIGHT_BLUE;
            case GREEN:
                return DyeColor.LIME;
            case AQUA:
                return DyeColor.CYAN;
            case RED:
                return DyeColor.PINK;
            case LIGHT_PURPLE:
                return DyeColor.PURPLE;
            case YELLOW:
                return DyeColor.YELLOW;
            case WHITE:
                return DyeColor.WHITE;
            default:
                return null;
            //MISSING: Brown
        }
    }

    @NotNull
    public static Color getColor(@NotNull ChatColor color)
    {
        switch (color)
        {
            default:
            case BLACK:
                return Color.fromRGB(0x000000);
            case DARK_BLUE:
                return Color.fromRGB(0x0000AA);
            case DARK_GREEN:
                return Color.fromRGB(0x00AA00);
            case DARK_AQUA:
                return Color.fromRGB(0x00AAAA);
            case DARK_RED:
                return Color.fromRGB(0xAA0000);
            case DARK_PURPLE:
                return Color.fromRGB(0xAA00AA);
            case GOLD:
                return Color.fromRGB(0xFFAA00);
            case GRAY:
                return Color.fromRGB(0xAAAAAA);
            case DARK_GRAY:
                return Color.fromRGB(0x555555);
            case BLUE:
                return Color.fromRGB(0x5555FF);
            case GREEN:
                return Color.fromRGB(0x55FF55);
            case AQUA:
                return Color.fromRGB(0x55FFFF);
            case RED:
                return Color.fromRGB(0xFF5555);
            case LIGHT_PURPLE:
                return Color.fromRGB(0xFF55FF);
            case YELLOW:
                return Color.fromRGB(0xFFFF55);
            case WHITE:
                return Color.fromRGB(0xFFFFFF);
        }
    }

    @NotNull
    public static Color getBackgroundColor(@NotNull ChatColor color)
    {
        switch (color)
        {
            default:
            case BLACK:
                return Color.fromRGB(0x000000);
            case DARK_BLUE:
                return Color.fromRGB(0x00002A);
            case DARK_GREEN:
                return Color.fromRGB(0x002A00);
            case DARK_AQUA:
                return Color.fromRGB(0x002A2A);
            case DARK_RED:
                return Color.fromRGB(0x2A0000);
            case DARK_PURPLE:
                return Color.fromRGB(0x2A002A);
            case GOLD:
                return Color.fromRGB(0x2A2A00);
            case GRAY:
                return Color.fromRGB(0x2A2A2A);
            case DARK_GRAY:
                return Color.fromRGB(0x151515);
            case BLUE:
                return Color.fromRGB(0x15153F);
            case GREEN:
                return Color.fromRGB(0x153F15);
            case AQUA:
                return Color.fromRGB(0x153F3F);
            case RED:
                return Color.fromRGB(0x3F1515);
            case LIGHT_PURPLE:
                return Color.fromRGB(0x3F153F);
            case YELLOW:
                return Color.fromRGB(0x3F3F15);
            case WHITE:
                return Color.fromRGB(0x3F3F3F);
        }
    }

    @Nullable
    public BarColor getBarColor(@NotNull ChatColor color)
    {
        switch (color)
        {
            case BLUE:
                return BarColor.BLUE;
            case GREEN:
                return BarColor.GREEN;
            case LIGHT_PURPLE:
                return BarColor.PINK;
            case DARK_PURPLE:
                return BarColor.PURPLE;
            case RED:
                return BarColor.RED;
            case WHITE:
                return BarColor.WHITE;
            case YELLOW:
                return BarColor.YELLOW;

            default:
                return null;
        }
    }
}
