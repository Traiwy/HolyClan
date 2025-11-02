package ru.traiwy.event;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.traiwy.storage.cache.ClanCache;

@AllArgsConstructor
public class PlayerQuitListener implements Listener {
    private final ClanCache cache;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        final Player player = event.getPlayer();

        cache.removeQuit(player.getName());
    }
}
