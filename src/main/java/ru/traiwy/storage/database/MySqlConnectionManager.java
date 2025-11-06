package ru.traiwy.storage.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.manager.config.ConfigDBManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlConnectionManager {
    private final JavaPlugin plugin;
    private HikariDataSource dataSource;

    public MySqlConnectionManager(JavaPlugin plugin, MySqlConnectionManager connectionManager) {
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                setup();
                plugin.getLogger().info("Подключено к MySQL!");
            } catch (Exception e) {
                plugin.getLogger().severe("Ошибка MySQL: " + e.getMessage());
            }
        });
    }


    public void setup() {
        final String url = "jdbc:mysql://" + ConfigDBManager.MySQL.HOST + ":" +
                ConfigDBManager.MySQL.PORT + "/" + ConfigDBManager.MySQL.DATABASE;

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(ConfigDBManager.MySQL.USER);
        config.setPassword(ConfigDBManager.MySQL.PASSWORD);

        config.setMaximumPoolSize(3);
        config.setMinimumIdle(1);

         dataSource = new HikariDataSource(config);
        initDatabase();
    }

     public void initDatabase() {
         final String clanTable = """
                 CREATE TABLE IF NOT EXISTS clans (
                 id int AUTO_INCREMENT PRIMARY KEY,
                 owner VARCHAR(15) UNIQUE NOT NULL, 
                 clanName VARCHAR(12) NOT NULL, 
                 balance BIGINT DEFAULT 0,
                 point INT DEFAULT 0,
                 level INT DEFAULT 1, 
                 typeClan VARCHAR(3) NOT NULL)""";

         final String memberTable = """
                 CREATE TABLE IF NOT EXISTS members(
                 member  VARCHAR(15) UNIQUE NOT NULL,
                 clan_id int NOT NULL,
                 FOREIGN KEY (clan_id)  REFERENCES clans(id))""";


         try (final Connection conn = getConnection();
              final Statement st = conn.createStatement()) {
             st.executeUpdate(clanTable);
             st.executeUpdate(memberTable);
         } catch (SQLException e) {
             e.printStackTrace();
             Bukkit.getLogger().warning("DataBase is not created!!");
         }
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

