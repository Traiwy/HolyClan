package ru.traiwy.event;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.traiwy.data.ClanData;
import ru.traiwy.storage.cache.ClanCache;

@AllArgsConstructor
public class PlayerQuitListener implements Listener {
    private final ClanCache cache;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        final Player player = event.getPlayer();

        ClanData clanData = cache.get(player.getName());

        if(clanData != null){
            cache.removeQuit(player.getName());
        }
    }
}
