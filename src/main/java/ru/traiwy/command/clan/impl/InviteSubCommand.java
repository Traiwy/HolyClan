package ru.traiwy.command.clan.impl;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.traiwy.command.SubCommand;
import ru.traiwy.data.ClanData;
import ru.traiwy.manager.InviteManager;
import ru.traiwy.manager.config.ConfigManager;
import ru.traiwy.storage.cache.ClanCache;
import ru.traiwy.storage.database.clans.ClanRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
public class InviteSubCommand implements SubCommand {
    private final ClanRepository clanRepository;
    private final ClanCache clanCache;
    private final JavaPlugin plugin;
    private final InviteManager inviteManager;

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (args.length < 2) {
            player.sendMessage("§cИспользование: /clan invite <игрок>");
            return;
        }

        final String playerName = args[1];
        final Player target = Bukkit.getPlayer(playerName);

        final ClanData clan = clanCache.get(player.getName());

        if(clan == null){
            player.sendMessage("Перезайдите на сервер и пропишите команду еще раз");
            return;
        }

        if (playerName == null) {
            player.sendMessage("§cИгрок " + playerName + " не найден или оффлайн");
            return;
        }

        player.sendMessage(ConfigManager.MESSAGE.inviteSentMessage
                .replace("%name%", target.getName()));

        target.sendMessage(ConfigManager.MESSAGE.inviteMessage
                .replace("%name%", player.getName())
                .replace("%nameClan%", clan.getClanName()));

        inviteManager.addInvite(player, target, clan.getClanName());


        new BukkitRunnable() {
            int seconds = 300;

            @Override
            public void run() {
                seconds--;
                if (seconds <= 0) {
                    player.sendMessage("§cПриглашение игроку " + target.getName() + " истекло");
                    target.sendMessage("§cПриглашение в клан истекло");
                    inviteManager.removeInvite(player);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                .toList();

    }
}
