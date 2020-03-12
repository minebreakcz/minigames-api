package net.graymadness.minigame_api.event;

import net.graymadness.minigame_api.api.IMinigame;
import net.graymadness.minigame_api.api.MinigameState;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MinigameStateChangedEvent extends MinigameEvent
{
    public MinigameStateChangedEvent(@NotNull IMinigame minigame, @NotNull MinigameState state)
    {
        super(minigame);
        this.state = state;
    }

    @NotNull
    private MinigameState state;
    @NotNull
    public MinigameState getState()
    {
        return state;
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
