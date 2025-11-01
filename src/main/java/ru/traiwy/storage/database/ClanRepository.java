package ru.traiwy.storage.database;

import ru.traiwy.data.ClanData;
import ru.traiwy.enums.TypeClan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClanRepository {
     private final MySqlConnectionManager connectionManager;

    public ClanRepository(MySqlConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public ClanData getByOwner(String owner) {
        final String sql = "SELECT * FROM clans WHERE owner = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, owner);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new ClanData(
                            rs.getString("owner"),
                            rs.getInt("level"),
                            TypeClan.valueOf(rs.getString("typeClan"))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(ClanData clan) {
        final String sql = "INSERT INTO clans(owner, level, typeClan) VALUES(?, ?, ?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, clan.getOwner());
            ps.setInt(2, clan.getLevel());
            ps.setString(3, clan.getTypeClan().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ClanData clan) {
        final String sql = "UPDATE clans SET owner=?, level=?, typeClan=? WHERE id=?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clan.getOwner());
            ps.setInt(2, clan.getLevel());
            ps.setString(3, clan.getTypeClan().name());
            ps.setInt(4, clan.getId());
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

