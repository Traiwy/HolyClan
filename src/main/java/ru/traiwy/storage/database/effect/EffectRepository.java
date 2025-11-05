package ru.traiwy.storage.database.effect;

import ru.traiwy.data.EffectData;
import ru.traiwy.enums.EffectType;
import ru.traiwy.storage.database.MySqlConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EffectRepository {
    private final MySqlConnectionManager connectionManager;

    public EffectRepository(MySqlConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public EffectData getEffectPVP(int clanId) {
        final String sql = "SELECT * FROM effectPvpTable WHERE clan_id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clanId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EffectData(
                            rs.getInt("clan_id"),
                            EffectType.valueOf(rs.getString("effectType")),
                            rs.getInt("active"),
                            rs.getInt("level"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time") != null ? rs.getTimestamp("end_time").toLocalDateTime() : null
                    );

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении эффекта: " + e.getMessage(), e);
        }
        return null;
    }

    public void insertPVP(EffectData effect) {
        final String sql = """
                INSERT INTO effectPvpTable (clan_id, effectType, active, level, start_time, end_time)
                VALUES (?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    effectType = VALUES(effectType),
                    active = VALUES(active),
                    level = VALUES(level),
                    start_time = VALUES(start_time),
                    end_time = VALUES(end_time)
                """;

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, effect.getClanId());
            ps.setString(2, effect.getEffectType().name());
            ps.setInt(3, effect.getActive());
            ps.setInt(4, effect.getLevel());
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(effect.getStartDateTime()));
            ps.setTimestamp(6, java.sql.Timestamp.valueOf(effect.getEndDataTime()));

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении эффекта: " + e.getMessage(), e);
        }
    }

    public void deletePVP(int clanId) {
        final String sql = "DELETE FROM effectPvpTable WHERE clan_id = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, clanId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении эффекта: " + e.getMessage(), e);
        }
    }


    public void updatePVP(EffectData effect) {
        final String sql = """
                    UPDATE effectPvpTable
                    SET effectType = ?, active = ?, level = ?, start_time = ?, end_time = ?
                    WHERE clan_id = ?
                """;

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, effect.getEffectType().name());
            ps.setInt(2, effect.getActive());
            ps.setInt(3, effect.getLevel());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(effect.getStartDateTime()));
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(effect.getEndDataTime()));
            ps.setInt(6, effect.getClanId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении эффекта: " + e.getMessage(), e);
        }
    }


    public EffectData getEffectPVE(int clanId) {
        final String sql = "SELECT * FROM effectPveTable WHERE clan_id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clanId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EffectData(
                            rs.getInt("clan_id"),
                            EffectType.valueOf(rs.getString("effectType")),
                            rs.getInt("active"),
                            rs.getInt("level"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time") != null ? rs.getTimestamp("end_time").toLocalDateTime() : null
                    );

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении эффекта: " + e.getMessage(), e);
        }
        return null;
    }

    public void insertPVE(EffectData effect) {
        final String sql = """
                INSERT INTO effectPveTable (clan_id, effectType, active, level, start_time, end_time)
                VALUES (?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    effectType = VALUES(effectType),
                    active = VALUES(active),
                    level = VALUES(level),
                    start_time = VALUES(start_time),
                    end_time = VALUES(end_time)
                """;

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, effect.getClanId());
            ps.setString(2, effect.getEffectType().name());
            ps.setInt(3, effect.getActive());
            ps.setInt(4, effect.getLevel());
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(effect.getStartDateTime()));
            ps.setTimestamp(6, java.sql.Timestamp.valueOf(effect.getEndDataTime()));

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении эффекта: " + e.getMessage(), e);
        }
    }

    public void deletePVE(int clanId) {
        final String sql = "DELETE FROM effectPveTable WHERE clan_id = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, clanId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении эффекта: " + e.getMessage(), e);
        }
    }


    public void updatePVE(EffectData effect) {
        final String sql = """
                    UPDATE effectPveTable
                    SET effectType = ?, active = ?, level = ?, start_time = ?, end_time = ?
                    WHERE clan_id = ?
                """;

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, effect.getEffectType().name());
            ps.setInt(2, effect.getActive());
            ps.setInt(3, effect.getLevel());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(effect.getStartDateTime()));
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(effect.getEndDataTime()));
            ps.setInt(6, effect.getClanId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении эффекта: " + e.getMessage(), e);
        }
    }
}
