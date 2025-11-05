package ru.traiwy.event;

import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.traiwy.data.EffectData;
import ru.traiwy.storage.cache.ClanCache;
import ru.traiwy.storage.cache.EffectCache;
import ru.traiwy.storage.database.clans.ClanStorage;
import ru.traiwy.storage.database.effect.EffectStorage;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {
    private final EffectStorage effectStorage;
    private final ClanCache clanCache;
    private final EffectCache effectCache;
    private final ClanStorage storage;

    @EventHandler
    public void onPlayerJoinListener(PlayerJoinEvent event){
        final var player = event.getPlayer();

        storage.getClan(player).thenAccept(clanData -> {
            if(clanData != null){
                effectStorage.getEffectPVP(clanData.getId()).thenAccept(effectData -> {
                    if(effectData != null){
                        effectCache.put(effectData);
                    }
                    return;
                });
               clanCache.put(clanData);
            }
            return;
        });
    }
}
