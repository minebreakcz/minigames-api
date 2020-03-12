package net.graymadness.minigame_api.command;

import net.graymadness.minigame_api.MinigameAPI;
import net.graymadness.minigame_api.api.IMinigame;
import net.graymadness.minigame_api.api.MinigameState;
import net.graymadness.minigame_api.helper.CommandHelper;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinigameCommand implements CommandExecutor, TabCompleter
{
    private final MinigameAPI plugin;
    private final PluginCommand command;

    public MinigameCommand(MinigameAPI plugin, PluginCommand command)
    {
        this.plugin = plugin;
        this.command = command;

        command.setTabCompleter(this);
        command.setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if(args.length == 0 || "help".equals(args[0]))
        {
            sender.sendMessage(ChatColor.GOLD + "/minigame help" + ChatColor.WHITE + "    " + "Show this help");
            sender.sendMessage(ChatColor.GOLD + "/minigame start" + ChatColor.WHITE + "    " + "Force start of the game");
            sender.sendMessage(ChatColor.GOLD + "/minigame stop" + ChatColor.WHITE + "    " + "Make current game end");
            sender.sendMessage(ChatColor.GOLD + "/minigame status" + ChatColor.WHITE + "    " + "Prints status of current game");
            return true;
        }

        switch (args[0])
        {
            case "start":
            {
                IMinigame minigame = plugin.getMinigame();

                switch (minigame.getState())
                {
                    default:
                        break;
                    case Warmup:
                    case InProgress:
                        sender.sendMessage(ChatColor.RED + "Game has already started");
                        return true;
                }

                if(Bukkit.getOnlinePlayers().size() > minigame.getMinPlayers())
                {
                    sender.sendMessage(ChatColor.RED + "Game cannot start right now");
                    return true;
                }

                minigame.start();
                return true;
            }
            case "stop":
            {
                IMinigame minigame = plugin.getMinigame();

                switch (minigame.getState())
                {
                    default:
                        break;
                    case Warmup:
                    case InProgress:
                        sender.sendMessage(ChatColor.RED + "Game is not running");
                        return true;
                }

                minigame.stop();
                return true;
            }
            case "status":
            {
                IMinigame minigame = plugin.getMinigame();

                if(sender instanceof Player)
                    ((Player)sender).spigot().sendMessage(ChatMessageType.SYSTEM, minigame.getState().getChat());
                else
                    sender.sendMessage(minigame.getState().getChat().toLegacyText());
                return true;
            }
        }

        return false;
    }
    
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args)
    {
        if(args.length == 0)
            return new ArrayList<>();

        if(args.length == 1)
        {
            return CommandHelper.filterOnly(args[0], Arrays.asList(
                    "help",
                    "start",
                    "stop"
            ));
        }

        return new ArrayList<>();
    }
}
