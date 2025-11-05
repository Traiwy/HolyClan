package ru.traiwy.storage.cache;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.traiwy.data.EffectData;
import ru.traiwy.enums.EffectType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EffectCache {
    private final Map<Integer, EffectData> cache = new ConcurrentHashMap<>();


    public EffectData get(int clanId) {
        return cache.get(clanId);
    }

    public void put(EffectData effect) {
        cache.put(effect.getClanId(), effect);
    }

    public void remove(int id) {
        cache.values().removeIf(effect -> effect.getClanId() == id);
    }

    public void removeQuit(int clanId){
        Bukkit.getLogger().info("Кэш очищен");
        cache.remove(clanId);
    }

    public boolean contains(int clanId) {
        return cache.containsKey(clanId);
    }
}
