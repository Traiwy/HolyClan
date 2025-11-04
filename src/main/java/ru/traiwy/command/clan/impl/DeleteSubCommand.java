package ru.traiwy.command.clan.impl;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.traiwy.command.SubCommand;
import ru.traiwy.data.ClanData;
import ru.traiwy.manager.ChatInputManager;
import ru.traiwy.storage.cache.ClanCache;
import ru.traiwy.storage.database.MySqlStorage;
import ru.traiwy.util.ClanPromptText;

import java.util.List;

public class DeleteSubCommand implements SubCommand {
    private final ChatInputManager chatInputManager;
    private final MySqlStorage mySqlStorage;
    private final ClanCache clanCache;

    public DeleteSubCommand(ChatInputManager chatInputManager, MySqlStorage mySqlStorage, ClanCache clanCache) {
        this.chatInputManager = chatInputManager;
        this.mySqlStorage = mySqlStorage;
        this.clanCache = clanCache;

    }


    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        final ClanData clanData = clanCache.get(player.getName());
        if(clanData == null) {
            player.sendMessage("Вы не состоите в клане");
            return;
        }

        chatInputManager.waitForInput(player, ClanPromptText.onDeleteClanText(), clanDelete -> {
            switch (clanDelete){
                case "ПОДТВЕРДИТЬ" -> deleteClan(player, clanData);
                case "ОТМЕНА" -> chatInputManager.cancelPrompt(player);
            }

        });
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }

    private void deleteClan(Player player, ClanData clanData){
        mySqlStorage.removeClan(clanData.getId());
        clanCache.remove(clanData.getId());
        player.sendMessage("Клан успешно удалён");
    }

}
