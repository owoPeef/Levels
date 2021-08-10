package ru.owopeef.owolevels;

import org.bukkit.plugin.java.JavaPlugin;
import ru.owopeef.owolevels.commands.Commands;
import ru.owopeef.owolevels.utils.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends JavaPlugin {
    String name;
    String url;
    String user;
    String password;
    Connection con;
    @Override
    public void onEnable()
    {
        Config.loadConfig();
        // MySQL
        name = Config.readConfig("mysql", "name");
        url = "jdbc:mysql://" + Config.readConfig("mysql", "host") + ":3306/" + name + "?useUnicode=true&characterEncoding=utf8";
        user = Config.readConfig("mysql", "user");
        if (!Objects.equals(Config.readConfig("mysql", "password"), "empty"))
        {
            password = Config.readConfig("mysql", "password");
        }
        try {
            con = DriverManager.getConnection(url, user, password);
            this.getLogger().info("[MySQL] Connected to MySQL.");
            // Commands and Events
            getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
            getCommand("level").setExecutor(new Commands());
        } catch (SQLException sqlEx) {
            this.getLogger().warning("[MySQL] Connection Error: " + sqlEx.getMessage());
            this.getServer().shutdown();
        }
    }
}
