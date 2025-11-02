package ru.traiwy.command.clan.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.traiwy.command.SubCommand;
import ru.traiwy.inv.choose.StartMenu;

import java.util.List;

public class TakeSubCommand implements SubCommand {

    @Override
    public void onExecute(CommandSender sender, String[] args) {
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
