package ru.traiwy.manager;

import org.bukkit.entity.Player;
import ru.traiwy.data.InviteData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InviteManager {
     private final Map<UUID, InviteData> invites = new HashMap<>();

    public void addInvite(Player inviter, Player target, String clanName) {
        invites.put(target.getUniqueId(), new InviteData(inviter.getUniqueId(), clanName, System.currentTimeMillis() + 300_000)); // 5 минут
    }

     public InviteData getInvite(Player player) {
        InviteData invite = invites.get(player.getUniqueId());
        if (invite == null) return null;

        if (System.currentTimeMillis() > invite.getExpireTime()) {
            invites.remove(player.getUniqueId());
            return null;
        }

        return invite;
    }

    public void removeInvite(Player player) {
        invites.remove(player.getUniqueId());
    }
}
