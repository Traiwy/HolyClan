package ru.traiwy.storage.database.effect;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.traiwy.data.EffectData;
import ru.traiwy.storage.cache.ClanCache;
import ru.traiwy.storage.cache.EffectCache;
import ru.traiwy.storage.database.Storage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AllArgsConstructor
public class EffectStorage implements StorageEffect {
    private final EffectCache effectCache = new EffectCache();

    private final EffectRepository effectRepository;
     private final ExecutorService executor = Executors.newFixedThreadPool(2);


    @Override
    public CompletableFuture<EffectData> getEffectPVP(int clanId) {
        EffectData cached = effectCache.get(clanId);
        if(cached != null){
            Bukkit.getLogger().info("Эффект найден в БД: " + cached.getEffectType());
            return CompletableFuture.completedFuture(cached);
        }

        return CompletableFuture.supplyAsync(() -> {
            EffectData effect = effectRepository.getEffectPVP(clanId);
            if(effect != null) effectCache.put(effect);
            return effect;
        }, executor);
    }

    @Override
    public void addEffectPVP(EffectData effect) {
        CompletableFuture.runAsync(() -> {
            effectRepository.insertPVP(effect);
            effectCache.put(effect);
        }, executor);
    }

    @Override
    public void removeEffectPVP(int id) {
         CompletableFuture.runAsync(() -> {
            effectRepository.deletePVP(id);
            effectCache.remove(id);
        }, executor);
    }

    @Override
    public void updateEffectPVP(EffectData effect) {
        CompletableFuture.runAsync(() -> {
            effectRepository.updatePVP(effect);
            effectCache.put(effect);
        }, executor);
    }

    @Override
    public CompletableFuture<EffectData> getEffectPVE(int clanId) {
        EffectData cached = effectCache.get(clanId);
        if(cached != null){
            Bukkit.getLogger().info("Эффект найден в БД: " + cached.getEffectType());
            return CompletableFuture.completedFuture(cached);
        }

        return CompletableFuture.supplyAsync(() -> {
            EffectData effect = effectRepository.getEffectPVE(clanId);
            if(effect != null) effectCache.put(effect);
            return effect;
        }, executor);
    }

    @Override
    public void addEffectPVE(EffectData effect) {
        CompletableFuture.runAsync(() -> {
            effectRepository.insertPVE(effect);
            effectCache.put(effect);
        }, executor);
    }

    @Override
    public void removeEffectPVE(int id) {
        CompletableFuture.runAsync(() -> {
            effectRepository.deletePVE(id);
            effectCache.remove(id);
        }, executor);
    }

    @Override
    public void updateEffectPVE(EffectData effect) {
        CompletableFuture.runAsync(() -> {
            effectRepository.updatePVE(effect);
            effectCache.put(effect);
        }, executor);
    }
}
