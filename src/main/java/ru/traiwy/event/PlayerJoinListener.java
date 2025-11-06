package ru.traiwy.event;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.traiwy.data.ClanData;
import ru.traiwy.storage.cache.ClanCache;
import ru.traiwy.storage.database.clans.ClanRepository;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {
    private final ClanRepository clanRepository;
    private final ClanCache cache;
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        final Player player = event.getPlayer();
        
        final ClanData clanData =  clanRepository.getByOwner(player.getName());
        if(clanData != null){
            cache.put(clanData);
        }
    }
}
