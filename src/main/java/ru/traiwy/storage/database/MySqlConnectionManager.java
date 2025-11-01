package ru.traiwy.storage.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.traiwy.manager.config.ConfigDBManager;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlConnectionManager {
    private final HikariDataSource dataSource;

    public MySqlConnectionManager() {
        final String url = "jdbc:mysql://" + ConfigDBManager.MySQL.HOST + ":" +
                ConfigDBManager.MySQL.PORT + "/" + ConfigDBManager.MySQL.DATABASE;

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(ConfigDBManager.MySQL.USER);
        config.setPassword(ConfigDBManager.MySQL.PASSWORD);

        config.setMaximumPoolSize(3);
        config.setMinimumIdle(1);

        this.dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
