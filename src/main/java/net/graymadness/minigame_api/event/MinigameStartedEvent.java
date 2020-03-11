package net.graymadness.minigame_api.event;

import net.graymadness.minigame_api.api.IMinigame;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MinigameStartedEvent extends MinigameEvent
{
    public MinigameStartedEvent(@NotNull IMinigame minigame)
    {
        super(minigame);
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
