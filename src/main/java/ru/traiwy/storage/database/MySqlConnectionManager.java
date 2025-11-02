package ru.traiwy.storage.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.manager.config.ConfigDBManager;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlConnectionManager {
    private final JavaPlugin plugin;
    private HikariDataSource dataSource;

    public MySqlConnectionManager(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                HikariConfig config = setup();
                this.dataSource = new HikariDataSource(config);
                plugin.getLogger().info("Подключено к MySQL!");
            } catch (Exception e) {
                plugin.getLogger().severe("Ошибка MySQL: " + e.getMessage());
            }
        });
    }


    public HikariConfig setup() {
        final String url = "jdbc:mysql://" + ConfigDBManager.MySQL.HOST + ":" +
                ConfigDBManager.MySQL.PORT + "/" + ConfigDBManager.MySQL.DATABASE;

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(ConfigDBManager.MySQL.USER);
        config.setPassword(ConfigDBManager.MySQL.PASSWORD);

        config.setMaximumPoolSize(3);
        config.setMinimumIdle(1);
        return config;
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("База данных ещё не готова!");
        }
        return dataSource.getConnection();
    }

    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}

