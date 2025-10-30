package ru.traiwy.command;

import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.traiwy.inv.choose.StartMenu;
import ru.traiwy.inv.menu.PveMenu;
import ru.traiwy.inv.menu.PvpMenu;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OpenCommand implements CommandExecutor{
    private final List<String> SUBCOMMAND = Arrays.asList("pvp", "pve");

    private final PvpMenu pvpMenu;
    private final PveMenu pveMenu;
    private final StartMenu startMenu;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof  Player player)) return false;

        startMenu.open(player);
        return true;
    }

    //@Override
    //public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    //    if(args.length == 1){
    //        String current = args[0];
//
    //        return SUBCOMMAND.stream()
    //                .filter(s -> s.startsWith(current))
    //                .collect(Collectors.toList());
    //    }
//
    //    return Collections.emptyList();
    //}
}
