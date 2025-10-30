package ru.traiwy.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.data.ClanData;
import ru.traiwy.enums.TypeClan;
import ru.traiwy.manager.config.ConfigDBManager;

import java.sql.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MySqlStorage implements Storage {
    private final JavaPlugin plugin;

    private HikariDataSource dataSource;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public MySqlStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            setupDataSource();
        });
    }

    public void setupDataSource() {
        final String url = "jdbc:mysql://" + ConfigDBManager.MySQL.HOST + ":" +
                ConfigDBManager.MySQL.PORT + "/" + ConfigDBManager.MySQL.DATABASE;

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(ConfigDBManager.MySQL.USER);
        config.setPassword(ConfigDBManager.MySQL.PASSWORD);
        config.setMaximumPoolSize(3);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
        initDatabase();
    }

    private void initDatabase() {
        CompletableFuture.runAsync(() -> {
            final String clanTable = "CREATE TABLE IF NOT EXISTS clans (" +
                    "id int AUTO_INCREMENT PRIMARY KEY, " +
                    "owner VARCHAR(15) UNIQUE NOT NULL, " +
                    "level INT DEFAULT 1, " +
                    "typeClan VARCHAR(3) NOT NULL)";

            final String memberTable = "CREATE TABLE IF NOT EXISTS members(" +
                    "member  VARCHAR(15) UNIQUE NOT NULL, " +
                    "clan_id int NOT NULL," +
                    "FOREIGN KEY (clan_id)  REFERENCES clans(id))";

            try (final Connection conn = dataSource.getConnection();
                 final Statement st = conn.createStatement()) {
                st.executeUpdate(clanTable);
                st.executeUpdate(memberTable);
            } catch (SQLException e) {
                Bukkit.getLogger().warning("DataBase is not created!!");
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<ClanData> getClan(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            final String sql = "SELECT * FROM clans WHERE owner = ?";

            try (final Connection connection = dataSource.getConnection();
                 final PreparedStatement st = connection.prepareStatement(sql)) {

                st.setString(1, player.getName());

                try (final ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        return new ClanData(
                                rs.getInt("id"),
                                rs.getString("owner"),
                                rs.getInt("level"),
                                TypeClan.valueOf(rs.getString("typeClan"))
                        );
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return null;
        }, executorService);
    }

    @Override
    public void addClan(ClanData clan) {
        CompletableFuture.runAsync(() -> {
            final String sql = "INSERT INTO clans(owner, level, typeClan) VALUES(?, ?, ?)";
            try (final Connection connection = dataSource.getConnection();
                 final PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, clan.getOwner());
                ps.setInt(2, clan.getLevel());
                ps.setString(3, clan.getTypeClan().name());
                ps.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }


    @Override
    public void removeClan(int id) {
        CompletableFuture.runAsync(() -> {
            final String sql = "DELETE FROM clans WHERE id = ?";
            try (final Connection connection = dataSource.getConnection();
                 final PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }

    @Override
    public void updateClan(ClanData clan) {
        CompletableFuture.runAsync(() -> {
            final String sql = "UPDATE clans SET owner = ?, level = ?, typeClan = ? WHERE id = ?";
            try (final Connection connection = dataSource.getConnection();
                 final PreparedStatement st = connection.prepareStatement(sql)) {
                st.setString(1, clan.getOwner());
                st.setInt(2, clan.getLevel());
                st.setString(3, clan.getTypeClan().name());
                st.setInt(4, clan.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }

    @Override
    public void addMember(Player player) {

    }
}
