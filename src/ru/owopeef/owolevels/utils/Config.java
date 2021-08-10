package ru.owopeef.owolevels.utils;

import org.bukkit.plugin.Plugin;
import ru.owopeef.owolevels.Main;

import java.io.File;

public class Config
{
    public static Plugin plugin = Main.getPlugin(Main.class);
    public static void loadConfig() {
        File currentFile = new File(System.getProperty("user.dir") + "\\plugins\\owoMurderMystery\\config.yml");
        if (!currentFile.exists())
        {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveConfig();
        }
    }
    public static String readConfig(String path)
    {
        return plugin.getConfig().get(path).toString();
    }
    public static String readConfig(String path, String parent1)
    {
        return plugin.getConfig().get(path + "." + parent1).toString();
    }
    public static String readConfig(String path, String parent1, String parent2)
    {
        return plugin.getConfig().get(path + "." + parent1 + "." + parent2).toString();
    }
    public static String readConfig(String path, String parent1, String parent2, String parent3)
    {
        return plugin.getConfig().get(path + "." + parent1 + "." + parent2 + "." + parent3).toString();
    }
    public static String readConfigWithException(String path, String parent1)
    {
        try
        {
            return plugin.getConfig().get(path + "." + parent1).toString();
        }
        catch (Exception e)
        {
            return "break";
        }
    }
}
