package net.graymadness.minigame_api.api;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;

public enum MinigameState
{
    Lobby("lobby"),
    Warmup("warmup"),
    InProgress("progress"),
    PostGame("post")
    ;

    public final String key;
    MinigameState(String key)
    {
        this.key = key;
    }

    public String getLocalizationName()
    {
        return "minigame.state." + key + ".post";
    }

    public BaseComponent getChat()
    {
        return new TranslatableComponent(getLocalizationName());
    }
}
