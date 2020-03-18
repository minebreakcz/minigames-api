package net.graymadness.minigame_api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
                System.err.println(ex.toString());
                this.setEnabled(false);
                return;
            }
        }

        PluginManager pm = Bukkit.getPluginManager();
        {
            pm.registerEvents(this, this);
        }

        new MinigameCommand(this, getCommand("minigame"));

        try
        {
            currencyTypes.clear();

            ResultSet result = statement_select_currency_type.executeQuery();
            while(result.next())
            {
                long id = result.getLong(1);
                String name = result.getString(2);
                currencyTypes.put(name, id);
            }
        }
        catch(Exception ex)
        {
            System.err.println(ex.toString());
        }
    }

    @Override
    public void onDisable()
    {
        instance = null;
    }

    private Connection connection;

    private PreparedStatement statement_select_player;
    private PreparedStatement statement_select_general;
    private PreparedStatement statement_select_minigame_id;
    private PreparedStatement statement_select_currency_type;

    private PreparedStatement statement_insert_player;

    private PreparedStatement statement_update_general;
    private PreparedStatement statement_update_currency;

    public void openConnection(@NotNull String host, int port, @NotNull String database, @Nullable String username, @Nullable String password) throws SQLException, ClassNotFoundException
    {
        if (connection != null && !connection.isClosed())
            return;

        synchronized (this)
        {
            if (connection != null && !connection.isClosed())
                return;

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host+ ":" + port + "/" + database, username, password);


            statement_select_player = connection.prepareStatement("SELECT id FROM players WHERE uuid = UUID_TO_BIN(?) LIMIT 1");
            statement_select_general = connection.prepareStatement("SELECT statistics, kit_unlocked, kit_active FROM player_minigame WHERE player_id = ? AND minigame_id = ? LIMIT 1");
            statement_select_minigame_id = connection.prepareStatement("SELECT id FROM minigames WHERE name = ?");
            statement_select_currency_type = connection.prepareStatement("SELECT id, name FROM currency");

            statement_insert_player = connection.prepareStatement("INSERT INTO players (uuid, last_name) VALUES (UUID_TO_BIN(?), ?) ON DUPLICATE KEY UPDATE last_name=?");

            statement_update_general = connection.prepareStatement("INSERT INTO player_minigame (player_id, minigame, statistics, kits, active_kit) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE statistics = ?, kits = ?, active_kit = ?");
            statement_update_currency = connection.prepareStatement("INSERT INTO player_currency (player_id, currency_id, amount) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE amount = amount + ?");
        }
    }

    public void openConnection(@NotNull ConfigurationSection config) throws SQLException, ClassNotFoundException
    {
        @NotNull
        String host = config.getString("host");

        int port = config.getInt("port");

        @NotNull
        String database = config.getString("database");

        @Nullable
        String username = config.getString("username");

        @Nullable
        String password = config.getString("password");

        openConnection(host, port, database, username, password);
    }

    private IMinigame minigame;
    private int minigame_id;
    public IMinigame getMinigame()
    {
        return this.minigame;
    }
    public int getMinigameId()
    {
        return minigame_id;
    }
    public void setMinigame(@NotNull IMinigame minigame)
    {
        this.minigame = minigame;

        try
        {
            statement_select_minigame_id.setString(1, minigame.getCodename());

            ResultSet result = statement_select_minigame_id.executeQuery();
            if(result.next())
            {
                minigame_id = result.getInt(1);
            }
            else
                throw new RuntimeException();
        }
        catch (Exception ex)
        {
            minigame_id = 0;
            this.setEnabled(false);

            System.err.println(ex.toString());
        }
    }

    private final Map<@NotNull Player, Long> playerIds = new HashMap<>();
    public long getPlayerId(@NotNull Player player)
    {
        return playerIds.get(player);
    }

    private final Map<@NotNull String, Long> currencyTypes = new HashMap<>();

    private final Map<@NotNull Player, PlayerBuffer> bufferMap = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent event)
    {
        @NotNull
        Player player = event.getPlayer();

        //TODO async?
        {
            PlayerBuffer buffer;
            try
            {
                // Load ID
                {
                    statement_select_player.setString(1, player.getUniqueId().toString()); // player_id (UUID)

                    ResultSet result = statement_select_player.executeQuery();
                    if(result.next())
                        playerIds.put(player, result.getLong(1));
                    else
                        player.kickPlayer("Server closed - database problems");
                }

                // Register player
                // This will later be on lobby
                {
                    statement_insert_player.setString(1, player.getUniqueId().toString()); // player_id (UUID)

                    // last_name (string)
                    statement_insert_player.setString(2, player.getName());
                    statement_insert_player.setString(3, player.getName());

                    statement_insert_player.execute();
                }

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

        //TODO async?
        try
        {
            buffer.save(player);
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            return;
        }
        finally
        {
            bufferMap.remove(player);
            playerIds.remove(player);
        }
    }

    @NotNull
    public PlayerBuffer getPlayerBuffer(Player player)
    {
        return bufferMap.get(player);
    }

    public class PlayerBuffer
    {
        @NotNull
        public final Map<@NotNull String, @NotNull Long> Currency = new HashMap<>();
        public void addCurrency(@NotNull String currency, int amount)
        {
            Currency.put(currency, Currency.getOrDefault(currency, (long)0) + amount);
        }

        @NotNull
        public JsonElement Statistics = new JsonObject();

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
                    statement_update_general.setLong(1, getPlayerId(player)); // player_id (UUID)
                    statement_update_general.setString(2, getMinigame().getCodename()); // minigame (varchar(32))
                    statement_update_general.setString(3, Statistics.toString()); // statistics (JSON)
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

                        statement_update_currency.setLong(2, currencyTypes.get(key)); // currency (varchar(32))

                        // amount (long)
                        statement_update_currency.setLong(3, amount);
                        statement_update_currency.setLong(4, amount);

                        // Update in database
                        statement_update_currency.execute();
                    }

                    Currency.clear();
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
            statement_select_general.setLong(1, getPlayerId(player)); // player_id (long)
            statement_select_general.setInt(2, getMinigameId()); // minigame_id (integer)

            ResultSet result = statement_select_general.executeQuery();
            if(result.next())
            {
                try
                {
                    String statisticsJson = result.getString(1);
                    buffer.Statistics = new JsonParser().parse(statisticsJson);
                }
                catch (Exception ex)
                {
                    buffer.Statistics = new JsonObject();
                }

                @Nullable
                String kits = result.getString(2);
                if(kits != null)
                    Collections.addAll(buffer.UnlockedKits, kits.split(";"));

                buffer.SelectedKit = result.getString(3);
            }
            else
                return null;
        }

        return buffer;
    }
}
