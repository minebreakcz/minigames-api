package net.graymadness.minigame_api.api;

import net.graymadness.minigame_api.MinigameAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class API
{
    private API() {}

    public static void addCurrency(@NotNull Player player, @NotNull String type, int amount)
    {
        if(amount <= 0)
            throw new IllegalArgumentException("Argument <amount> out of bounds.");

        MinigameAPI.getInstance().getPlayerBuffer(player).addCurrency(type, amount);
    }
    public static void addMinigameCurrency(@NotNull Player player, int amount)
    {
        addCurrency(player, getMinigame().getCodename(), amount);
    }

    public static long getCurrency(@NotNull Player player, @NotNull String type)
    {
        return MinigameAPI.getInstance().getPlayerBuffer(player).getCurrency(type);
    }
    public static long getMinigameCurrency(@NotNull Player player)
    {
        return getCurrency(player, getMinigame().getCodename());
    }

    public static void registerMinigame(@NotNull IMinigame minigame)
    {
        MinigameAPI api = MinigameAPI.getInstance();

        if(api.getMinigame() != null)
            throw new RuntimeException("Only one minigame is allowed");

        api.setMinigame(minigame);
    }

    @NotNull
    public static IMinigame getMinigame()
    {
        return MinigameAPI.getInstance().getMinigame();
    }
}
