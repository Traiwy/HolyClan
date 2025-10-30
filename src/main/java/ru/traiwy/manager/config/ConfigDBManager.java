package ru.traiwy.manager.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigDBManager {
    private final JavaPlugin plugin;
    private File file;
    private FileConfiguration config;

    public ConfigDBManager(JavaPlugin plugin){
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "bd.yml");
        try{
            load();
        } catch (Exception e) {
            Bukkit.getLogger().warning("Config bd not load");
        }
    }

    public void load(){
        if(!file.exists()){
            plugin.saveResource("bd.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        parse();
    }

    public void parse(){
        ConfigurationSection section = config.getConfigurationSection("mysql");

        MySQL.HOST = section.getString("host");
        MySQL.PORT = section.getInt("port");
        MySQL.USER = section.getString("user");
        MySQL.PASSWORD = section.getString("password");
        MySQL.DATABASE =  section.getString("database");
    }


    public static class MySQL{
        public static String HOST;
        public static int PORT;
        public static String USER;
        public static String PASSWORD;
        public static String DATABASE;
    }
}
