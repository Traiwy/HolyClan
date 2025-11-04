package ru.traiwy.command.clan;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.traiwy.command.LongCommandExecutor;
import ru.traiwy.command.clan.impl.*;
import ru.traiwy.command.clan.impl.DeleteSubCommand;
import ru.traiwy.data.ClanData;
import ru.traiwy.enums.TypeClan;
import ru.traiwy.inv.choose.StartMenu;
import ru.traiwy.inv.menu.PveMenu;
import ru.traiwy.inv.menu.PvpMenu;
import ru.traiwy.manager.ChatInputManager;
import ru.traiwy.storage.cache.ClanCache;
import ru.traiwy.storage.database.MySqlStorage;

import java.util.ArrayList;
import java.util.List;

public class ClanCommand extends LongCommandExecutor {
    private final JavaPlugin plugin;
    private final ClanCache cache;
    private final MySqlStorage mySqlStorage;
    private final StartMenu startMenu;
    private final ChatInputManager chatInputManager;

    private final PveMenu pveMenu;
    private final PvpMenu pvpMenu;

    public ClanCommand(JavaPlugin plugin, ClanCache cache, MySqlStorage mySqlStorage, StartMenu startMenu, ChatInputManager chatInputManager, PveMenu pveMenu, PvpMenu pvpMenu) {
        this.plugin = plugin;
        this.cache = cache;
        this.mySqlStorage = mySqlStorage;
        this.startMenu = startMenu;
        this.chatInputManager = chatInputManager;
        this.pveMenu = pveMenu;
        this.pvpMenu = pvpMenu;
        addSubCommand(new CapturesSubCommand(), new String[]{"captures"}, new Permission("clan.captures"));
        addSubCommand(new ChatSubCommand(), new String[]{"chat"}, new Permission("clan.chat"));
        addSubCommand(new DeleteSubCommand(chatInputManager, mySqlStorage, cache), new String[]{"delete"}, new Permission("clan.delete"));
        addSubCommand(new GlowSubCommand(), new String[]{"glow"}, new Permission("clan.glow"));
        addSubCommand(new HomeSubCommand(), new String[]{"home"}, new Permission("clan.home"));
        addSubCommand(new InvestSubCommand(), new String[]{"invest"}, new Permission("clan.invest"));
        addSubCommand(new KickSubCommand(), new String[]{"kick"}, new Permission("clan.kick"));
        addSubCommand(new LeaveSubCommand(), new String[]{"leave"}, new Permission("clan.leave"));
        addSubCommand(new ListSubCommand(), new String[]{"list"}, new Permission("clan.list"));
        addSubCommand(new MemberSubCommand(), new String[]{"member"}, new Permission("clan.member"));
        addSubCommand(new MoneySubCommand(), new String[]{"money"}, new Permission("clan.money"));
        addSubCommand(new NewLeaderSubCommand(), new String[]{"newleader"}, new Permission("clan.newleader"));
        addSubCommand(new PvpSubCommand(), new String[]{"pvp"}, new Permission("clan.pvp"));
        addSubCommand(new SethomeSubCommand(), new String[]{"sethome"}, new Permission("clan.sethome"));
        addSubCommand(new TakeSubCommand(), new String[]{"take"}, new Permission("clan.take"));
        addSubCommand(new WithdrawSubCommand(), new String[]{"withdraw"}, new Permission("clan.withdraw"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length == 0) {
            ClanData clan = cache.get(player.getName());
            if (clan == null) {
                Bukkit.getLogger().info("Отправляем запрос в БД....");
                mySqlStorage.getClan(player).thenAccept(clanData -> {
                    if (clanData != null) {
                        cache.put(clanData);

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            TypeClan typeClan = clanData.getTypeClan();
                            if (typeClan == TypeClan.PVP) {
                                pvpMenu.open(player);
                            } else if (typeClan == TypeClan.PVE) {
                                pveMenu.open(player);
                            }
                        });

                    } else {
                        Bukkit.getScheduler().runTask(plugin, () -> startMenu.open(player));
                    }

                });
                return true;
            }
            Bukkit.getLogger().info("Получаем данные из кэша...");
            TypeClan typeClan = clan.getTypeClan();
            if (typeClan == TypeClan.PVP) {
                pvpMenu.open(player);
            } else if (typeClan == TypeClan.PVE) {
                pveMenu.open(player);
            }
            return true;
        }

        final SubCommandWrapper wrapper = getWrapperFromLabel(args[0]);

        if (wrapper == null) return true;

        wrapper.getSubCommand().onExecute(sender, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            String current = args[0].toLowerCase();
            List<String> result = new ArrayList<>();
            for (String alias : getFirstAliases()) {
                if (alias.toLowerCase().startsWith(current)) {
                    result.add(alias);
                }
            }
            return result;
        }
        SubCommandWrapper wrapper = getWrapperFromLabel(args[0]);
        if (wrapper == null) return null;
        return wrapper.getSubCommand().onTabComplete(sender, args);
    }
}
