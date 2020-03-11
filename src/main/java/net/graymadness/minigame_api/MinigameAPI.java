package net.graymadness.minigame_api;

import net.graymadness.minigame_api.api.IMinigame;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MinigameAPI extends JavaPlugin implements Listener
{
    @NotNull
    public static MinigameAPI getInstance()
    {
        return instance;
    }
    private static MinigameAPI instance;


    @Override
    public void onEnable()
    {
        instance = this;

        {
            saveDefaultConfig();
            reloadConfig();

            FileConfiguration config = getConfig();
            try
            {
                openConnection(config.getConfigurationSection("database"));
            }
            catch(Exception ex)
            {
                System.err.println("Failed to create connection to database");
                this.setEnabled(false);
                return;
            }
        }

        PluginManager pm = Bukkit.getPluginManager();
        {
            pm.registerEvents(this, this);
        }
    }

    @Override
    public void onDisable()
    {
        instance = null;
    }

    private Connection connection;

    public void openConnection(@NotNull String host, int port, @NotNull String database, @NotNull String username, @NotNull String password) throws SQLException, ClassNotFoundException
    {
        if (connection != null && !connection.isClosed())
            return;

        synchronized (this)
        {
            if (connection != null && !connection.isClosed())
                return;

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host+ ":" + port + "/" + database, username, password);
        }
    }

    public void openConnection(@NotNull ConfigurationSection config) throws SQLException, ClassNotFoundException
    {
        @NotNull
        String host = config.getString("host", "localhost");

        int port = config.getInt("port", 3306);

        @NotNull
        String database = config.getString("database", "minigames");

        @NotNull
        String username = config.getString("username", "root");

        @NotNull
        String password = config.getString("password");

        openConnection(host, port, database, username, password);
    }

    private IMinigame minigame;
    public IMinigame getMinigame()
    {
        return this.minigame;
    }
    public void setMinigame(IMinigame minigame)
    {
        this.minigame = minigame;
    }

    private Map<@NotNull Player, PlayerBuffer> bufferMap = new HashMap<>();

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent event)
    {
        @NotNull
        Player player = event.getPlayer();

        //TODO Create PlayerBuffer
    }

    @EventHandler(priority = EventPriority.LOW)
    private void onPlayerQuit(PlayerQuitEvent event)
    {
        @NotNull
        Player player = event.getPlayer();
        @NotNull
        PlayerBuffer buffer = getPlayerBuffer(player);

        //TODO Save PlayerBuffer

        bufferMap.remove(player);
    }

    @NotNull
    public PlayerBuffer getPlayerBuffer(Player player)
    {
        return bufferMap.get(player);
    }

    public static class PlayerBuffer
    {
        @NotNull
        public final Map<@Nullable String, @NotNull Long> Currency = new HashMap<>();
        public void addCurrency(@NotNull String currency, int amount)
        {
            Currency.put(currency, Currency.getOrDefault(currency, (long)0) + amount);
        }
        public long getCurrency(@NotNull String currency)
        {
            return Currency.getOrDefault(currency, (long)0);
        }

        @NotNull
        public List<@NotNull String> Kits = new ArrayList<>();
        @Nullable
        public String SelectedKit = null;
    }
}
