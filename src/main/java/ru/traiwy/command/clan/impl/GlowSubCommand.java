package ru.traiwy.command.clan.impl;

import org.bukkit.command.CommandSender;
import ru.traiwy.command.SubCommand;

import java.util.List;

public class GlowSubCommand implements SubCommand {
    @Override
    public void onExecute(CommandSender sender, String[] args) {

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
