package ru.owopeef.owolevels.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.owopeef.owolevels.utils.Config;

import java.sql.*;
import java.util.Objects;

public class Commands implements CommandExecutor
{
    String name;
    String url;
    String user;
    String password;
    Connection con;
    Statement stmt;
    ResultSet rs;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // OTHER CODE
        if (!(sender instanceof Player)) { return true; }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("level"))
        {
            if (args.length == 0) {
                sender.sendMessage("Player with nickname " + player.getDisplayName() + " has " + player.getLevel() + " level");
                return true;
            }
            if (args.length == 1)
            {
                String playerNick = args[0];
                String query;
                Player playerArgs = Bukkit.getPlayer(playerNick);
                try {
                    stmt = con.createStatement();
                    try
                    {
                        query = "SELECT COUNT(*) AS row_count FROM users WHERE uuid='"+playerArgs.getUniqueId()+"' OR nickname='"+playerArgs.getDisplayName().toLowerCase()+"'";
                    }
                    catch (Exception e)
                    {
                        query = "SELECT COUNT(*) AS row_count FROM users WHERE nickname='"+playerNick.toLowerCase()+"'";
                    }
                    rs = stmt.executeQuery(query);
                    rs.next();
                    int count = rs.getInt("row_count");
                    if (count == 0)
                    {
                        sender.sendMessage("Player with nickname " + playerNick.toLowerCase() + " not found");
                    }
                    if (count == 1)
                    {
                        sender.sendMessage("Player with nickname " + playerArgs.getDisplayName() + " has " + playerArgs.getLevel() + " level");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
