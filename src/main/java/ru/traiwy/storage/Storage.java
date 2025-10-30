package ru.traiwy.storage;

import org.bukkit.entity.Player;
import ru.traiwy.data.ClanData;

import java.util.concurrent.CompletableFuture;

public interface Storage {

    CompletableFuture<ClanData> getClan(Player player);
    void addClan(ClanData clan);
    void removeClan(int id);
    void updateClan(ClanData clan);

    void addMember(Player player);

}
