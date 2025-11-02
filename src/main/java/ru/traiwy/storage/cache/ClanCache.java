package ru.traiwy.storage.cache;

import ru.traiwy.data.ClanData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClanCache {
     private final Map<String, ClanData> cache = new ConcurrentHashMap<>();

    public ClanData get(String owner) {
        return cache.get(owner);
    }

    public void put(ClanData clan) {
        cache.put(clan.getOwner(), clan);
    }

    public void remove(int id) {
        cache.values().removeIf(clan -> clan.getId() == id);
    }

    public void removeQuit(String owner){
        cache.remove(owner);
    }

    public boolean contains(String owner) {
        return cache.containsKey(owner);
    }
}
