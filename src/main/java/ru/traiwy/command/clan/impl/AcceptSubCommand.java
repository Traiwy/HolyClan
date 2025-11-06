package ru.traiwy.command.clan.impl;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.traiwy.command.SubCommand;
import ru.traiwy.data.InviteData;
import ru.traiwy.manager.InviteManager;
import ru.traiwy.storage.database.clans.ClanRepository;

import java.util.List;

@AllArgsConstructor
public class AcceptSubCommand implements SubCommand {
    private final InviteManager inviteManager;
    private final ClanRepository clanRepository;

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        InviteData invite = inviteManager.getInvite(player);
        if (invite == null) {
            player.sendMessage("§cУ вас нет активных приглашений.");
            return;
        }

        //Добавить добавление в таблицу мемберов
        inviteManager.removeInvite(player);

        Player inviter = Bukkit.getPlayer(invite.getInviter());
        if (inviter != null) {
            inviter.sendMessage("§aИгрок §e" + player.getName() + " §aпринял приглашение в ваш клан!");
        }

        player.sendMessage("§aВы присоединились к клану §e" + invite.getClanName() + "§a!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
