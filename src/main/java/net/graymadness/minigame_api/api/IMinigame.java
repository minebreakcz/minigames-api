package net.graymadness.minigame_api.api;

import org.jetbrains.annotations.NotNull;

public interface IMinigame
{
    @NotNull
    String getCodename();
    @NotNull
    String[] getRoles();
    @NotNull
    MinigameState getState();
}
