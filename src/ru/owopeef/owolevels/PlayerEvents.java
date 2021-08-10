package ru.owopeef.owolevels;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.owopeef.owolevels.utils.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class PlayerEvents implements Listener
{
    String query = "";
    Plugin plugin = JavaPlugin.getPlugin(Main.class);
    String name = "";
    String url = "";
    String user = "";
    String password = "";
    Connection con;
    Statement stmt;
    ResultSet rs;
    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws SQLException
    {
        Player player = event.getPlayer();
        event.setJoinMessage("");
        // MySQL
        name = Config.readConfig("mysql", "name");
        url = "jdbc:mysql://" + Config.readConfig("mysql", "host") + ":3306/" + name + "?useUnicode=true&characterEncoding=utf8";
        user = Config.readConfig("mysql", "user");
        if (!Objects.equals(Config.readConfig("mysql", "password"), "empty"))
        {
            password = Config.readConfig("mysql", "password");
        }
        con = DriverManager.getConnection(url, user, password);
        // OTHER CODE
        try
        {
            stmt = con.createStatement();
            query = "SELECT COUNT(*) AS row_count FROM users WHERE uuid='"+player.getUniqueId()+"' OR nickname='"+player.getDisplayName().toLowerCase()+"'";
            rs = stmt.executeQuery(query);
            rs.next();
            int count = rs.getInt("row_count");
            if (count == 0)
            {
                player.setLevel(0);
                player.setExp(0);
                try
                {
                    query = "insert into users (uuid, nickname) values ('"+player.getUniqueId()+"', '"+player.getDisplayName().toLowerCase()+"')";
                    stmt.executeUpdate(query);
                }
                catch (Exception exc)
                {
                    plugin.getLogger().info("[MySQL] Insert Error: " + exc.getMessage());
                }
            }
            if (count != 0)
            {
                query = "SELECT * FROM users WHERE uuid='"+player.getUniqueId()+"' OR nickname='"+player.getDisplayName().toLowerCase()+"'";
                rs = stmt.executeQuery(query);
                rs.next();
                int lvl = rs.getInt(4);
                int exp = rs.getInt(5);
                player.setLevel(lvl);
                player.setExp(0.1f);
            }
        }
        catch (SQLException sqlEx)
        {
            plugin.getLogger().warning("[MySQL] Select Error: " + sqlEx.getMessage());
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        event.setDeathMessage("");
    }
    @EventHandler
    public void onDamage(EntityDamageEvent event)
    {
        event.setCancelled(true);
        event.setDamage(0);
    }
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event)
    {
        event.setCancelled(true);
    }
}
