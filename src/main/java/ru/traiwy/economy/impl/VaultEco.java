package ru.traiwy.economy.impl;

import lombok.AllArgsConstructor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ru.traiwy.economy.EconomyEditor;

@AllArgsConstructor
public class VaultEco implements EconomyEditor {
    private final Economy economy;

    @Override
    public double getBalance(Player player) {
        if(economy == null) return 0;
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getName());
        return economy.getBalance(offlinePlayer);
    }

    @Override
    public void takeBalance(Player player, int amount) {
        if(economy == null || amount <= 0) return;
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getName());
        economy.withdrawPlayer(offlinePlayer, amount);
    }

}
