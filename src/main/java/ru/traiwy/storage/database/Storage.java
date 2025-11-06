package ru.traiwy.storage.database;

import org.bukkit.entity.Player;
import ru.traiwy.data.ClanData;

import java.util.concurrent.CompletableFuture;

public interface Storage {
    CompletableFuture<ClanData> getClan(Player player);
    void addData(ClanData clan);
    void removeData(int id);
    void updateData(ClanData clan);
}
