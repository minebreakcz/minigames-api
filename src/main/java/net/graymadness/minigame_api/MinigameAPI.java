package net.graymadness.minigame_api;

import net.graymadness.minigame_api.api.IMinigame;
import net.graymadness.minigame_api.command.MinigameCommand;
import net.graymadness.minigame_api.helper.Translate;
import net.graymadness.minigame_api.helper.item.ToolItemEvents;
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

import java.sql.*;
import java.util.*;

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

        Translate.reloadLocales();

        new ToolItemEvents(this);

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

        new MinigameCommand(this, getCommand("minigame"));
    }

    @Override
    public void onDisable()
    {
        instance = null;
    }

    private Connection connection;

    private PreparedStatement statement_select_general;
    private PreparedStatement statement_select_allCurrency;

    private PreparedStatement statement_update_general;
    private PreparedStatement statement_update_currency;

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


            statement_select_general = connection.prepareStatement("SELECT statistics, kits, active_kit FROM minigame_players WHERE player_id = UUID_TO_BIN(?) AND minigame = ? LIMIT 1");
            statement_select_allCurrency = connection.prepareStatement("SELECT currency, amount FROM minigame_currency WHERE player_id = UUID_TO_BIN(?) LIMIT 1");

            statement_update_general = connection.prepareStatement("INSERT INTO minigame_players (player_id, minigame, statistics, kits, active_kit) VALUES (UUID_TO_BIN(?), ?, ?, ?, ?) ON DUPLICATE KEY UPDATE statistics = ?, kits = ?, active_kit = ?");
            statement_update_currency = connection.prepareStatement("INSERT INTO minigame_currency (player_id, currency, amount) VALUES (UUID_TO_BIN(?), ?, ?) ON DUPLICATE KEY UPDATE amount = ?");
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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent event)
    {
        @NotNull
        Player player = event.getPlayer();

        {
            PlayerBuffer buffer;
            try
            {
                //TODO async?
                buffer = load(player);
            }
            catch (Exception ex)
            {
                System.err.println(ex);
                player.kickPlayer("Server closed - database problems");
                return;
            }

            if (buffer == null)
                buffer = new PlayerBuffer();

            bufferMap.put(player, buffer);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerQuit(PlayerQuitEvent event)
    {
        @NotNull
        Player player = event.getPlayer();
        @NotNull
        PlayerBuffer buffer = getPlayerBuffer(player);

        try
        {
            //TODO async?
            buffer.save(player);
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            return;
        }

        bufferMap.remove(player);
    }

    @NotNull
    public PlayerBuffer getPlayerBuffer(Player player)
    {
        return bufferMap.get(player);
    }

    public class PlayerBuffer
    {
        @NotNull
        public final Map<@NotNull String, @NotNull Long> Currency_Old = new HashMap<>();
        @NotNull
        public final Map<@NotNull String, @NotNull Long> Currency = new HashMap<>();
        public void addCurrency(@NotNull String currency, int amount)
        {
            Currency.put(currency, Currency.getOrDefault(currency, (long)0) + amount);
        }
        public long getCurrency(@NotNull String currency)
        {
            return Currency.getOrDefault(currency, (long)0);
        }

        @NotNull
        public final List<@NotNull String> UnlockedKits = new ArrayList<>();
        @Nullable
        public String SelectedKit = null;

        public boolean save(@NotNull Player player)
        {
            try
            {
                // Main info
                {
                    statement_update_general.setString(1, player.getUniqueId().toString()); // player_id (UUID)
                    statement_update_general.setString(2, getMinigame().getCodename()); // minigame (varchar(32))
                    statement_update_general.setString(3, "{}"); // statistics (JSON)
                    statement_update_general.setString(4, String.join(";", UnlockedKits)); // kits (TEXT, String array separated by ';')
                    statement_update_general.setString(5, SelectedKit); // active_kit (varchar(32))

                    statement_update_general.execute();
                }

                // Currency
                {
                    statement_update_currency.setString(1, player.getUniqueId().toString()); // player_id (UUID)

                    for(Map.Entry<@NotNull String, @NotNull Long> kvp : Currency.entrySet())
                    {
                        String key = kvp.getKey();
                        long amount = kvp.getValue();
                        if(Currency_Old.getOrDefault(key, (long)0) == amount)
                            continue;

                        statement_update_currency.setString(2, key); // currency (varchar(32))

                        // amount (long)
                        statement_update_currency.setLong(3, amount);
                        statement_update_currency.setLong(4, amount);

                        // Update in database
                        statement_update_currency.execute();
                        // Update in cache
                        Currency_Old.put(key, amount);
                    }
                }

                return true;
            }
            catch (Exception ex)
            {
                return false;
            }
        }
    }

    public PlayerBuffer load(@NotNull Player player) throws SQLException
    {
        PlayerBuffer buffer = new PlayerBuffer();

        // Main info
        {
            statement_select_general.setString(1, player.getUniqueId().toString()); // player_id (UUID)
            statement_select_general.setString(2, getMinigame().getCodename()); // minigame (varchar(32))

            ResultSet result = statement_select_general.executeQuery();
            if(result.next())
            {
                String statisticsJson = result.getString(1); //TODO Store statistics

                @Nullable
                String kits = result.getString(2);
                if(kits != null)
                    Collections.addAll(buffer.UnlockedKits, kits.split(";"));

                buffer.SelectedKit = result.getString(3);
            }
            else
                return null;
        }

        // Currency
        {
            statement_select_allCurrency.setString(1, player.getUniqueId().toString()); // player_id (UUID)

            ResultSet result = statement_select_allCurrency.executeQuery();
            while(result.next())
            {
                String key = result.getString(1);
                long amount = result.getLong(2);

                buffer.Currency_Old.put(key, amount);
            }

            buffer.Currency.putAll(buffer.Currency_Old);
        }

        return buffer;
    }
}
