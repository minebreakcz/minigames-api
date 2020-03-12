package net.graymadness.minigame_api.api;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;

public enum MinigameState
{
    Lobby("lobby", ChatColor.GRAY),
    Warmup("warmup", ChatColor.YELLOW),
    InProgress("progress", ChatColor.GREEN),
    PostGame("post", ChatColor.GRAY)
    ;

    public final String key;
    public final ChatColor color;

    MinigameState(String key, ChatColor color)
    {
        this.key = key;
        this.color = color;
    }

    public String getLocalizationName()
    {
        return "minigame.state." + key + ".post";
    }

    public BaseComponent getChat()
    {
        TranslatableComponent component = new TranslatableComponent(getLocalizationName());
        component.setColor(color);
        return component;
    }
}
