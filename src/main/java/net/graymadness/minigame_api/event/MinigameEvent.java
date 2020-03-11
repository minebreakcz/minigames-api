package net.graymadness.minigame_api.event;

import net.graymadness.minigame_api.api.IMinigame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MinigameEvent extends Event
{
    public MinigameEvent(@NotNull IMinigame minigame)
    {
        this.minigame = minigame;
    }

    private IMinigame minigame;
    public IMinigame getMinigame()
    {
        return this.minigame;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
