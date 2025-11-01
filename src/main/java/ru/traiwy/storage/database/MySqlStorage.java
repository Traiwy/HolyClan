package ru.traiwy.storage.database;

import org.bukkit.entity.Player;
import ru.traiwy.data.ClanData;
import ru.traiwy.storage.cache.ClanCache;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MySqlStorage implements Storage {
    private final ClanCache clanCache = new ClanCache();
    private final ClanRepository clanRepository;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    public MySqlStorage() {
        MySqlConnectionManager connectionManager = new MySqlConnectionManager();
        this.clanRepository = new ClanRepository(connectionManager);
    }

    @Override
    public CompletableFuture<ClanData> getClan(Player player) {
        ClanData cached = clanCache.get(player.getName());
        if (cached != null) {
            return CompletableFuture.completedFuture(cached);
        }

        return CompletableFuture.supplyAsync(() -> {
            ClanData clan = clanRepository.getByOwner(player.getName());
            if (clan != null) clanCache.put(clan);
            return clan;
        }, executor);
    }

    @Override
    public void addClan(ClanData clan) {
        CompletableFuture.runAsync(() -> {
            clanRepository.insert(clan);
            clanCache.put(clan);
        }, executor);
    }

    @Override
    public void removeClan(int id) {
        CompletableFuture.runAsync(() -> {
            clanRepository.delete(id);
            clanCache.remove(id);
        }, executor);
    }

    @Override
    public void updateClan(ClanData clan) {
        CompletableFuture.runAsync(() -> {
            clanRepository.update(clan);
            clanCache.put(clan);
        }, executor);
    }

    @Override
    public void addMember(Player player) {
    }
}