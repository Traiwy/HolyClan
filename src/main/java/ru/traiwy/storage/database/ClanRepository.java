package ru.traiwy.storage.database;

import ru.traiwy.data.ClanData;
import ru.traiwy.enums.TypeClan;

import java.sql.*;



public class ClanRepository {
     private final MySqlConnectionManager connectionManager;

    public ClanRepository(MySqlConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }



    public ClanData getByOwner(String owner) {
        final String sql = "SELECT * FROM clans WHERE owner = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, owner);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ClanData clan = new ClanData(
                            rs.getString("owner"),
                            rs.getString("clanName"),
                            rs.getLong("balance"),
                            rs.getInt("point"),
                            rs.getInt("level"),
                            TypeClan.valueOf(rs.getString("typeClan"))
                    );
                    clan.setId(rs.getInt("id"));
                    return clan;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(ClanData clan) {
        final String sql = "INSERT INTO clans(owner, clanName ,balance, point, level, typeClan) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            ps.setString(1, clan.getOwner());
            ps.setString(2, clan.getClanName());
            ps.setLong(3, clan.getBalance());
            ps.setInt(4, clan.getPoint());
            ps.setInt(5, clan.getLevel());
            ps.setString(6, clan.getTypeClan().name());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                clan.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ClanData clan) {
        final String sql = "UPDATE clans SET owner=?, clanName=?, balance=?, point =?, level=?, typeClan=? WHERE id=?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clan.getOwner());
            ps.setString(2, clan.getClanName());
            ps.setLong(3, clan.getBalance());
            ps.setInt(4, clan.getPoint());
            ps.setInt(5, clan.getLevel());
            ps.setString(6, clan.getTypeClan().name());
            ps.setInt(7, clan.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        final String sql = "DELETE FROM clans WHERE id=?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

