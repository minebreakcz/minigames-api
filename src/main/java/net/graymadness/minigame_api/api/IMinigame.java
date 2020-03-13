package net.graymadness.minigame_api.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface IMinigame
{
    @NotNull
    String getCodename();
    @NotNull
    Map<String, List<Player>> getRoles();

    @NotNull
    MinigameState getState();
    boolean canJoinDuringWarmup();

    int getMinPlayers();
    int getMaxPlayers();

    void start();
    void stop();
}
