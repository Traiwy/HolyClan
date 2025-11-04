package ru.traiwy.economy.impl;

import lombok.AllArgsConstructor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ru.traiwy.economy.EconomyEditor;

@AllArgsConstructor
public class VaultEco implements EconomyEditor {
    private final static int CREATE_CLAN_PRICE = 1250000;

    private final Economy economy;

    @Override
    public double getBalance(Player player) {
        if (economy == null) return 0;
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getName());
        return economy.getBalance(offlinePlayer);
    }

    @Override
    public boolean takeMoneyForClanCreation(Player player) {
        double balance = getBalance(player);
        if (balance < CREATE_CLAN_PRICE) {
            return false;
        }
        withdraw(player, CREATE_CLAN_PRICE);
        return true;
    }

    @Override
    public boolean withdraw(Player player, double amount) {
        EconomyResponse response = economy.withdrawPlayer(player, amount);
        return response.transactionSuccess();
    }

    @Override
    public boolean addBalanse(Player player, double amount) {
        EconomyResponse response = economy.depositPlayer(player, amount);
        return response.transactionSuccess();
    }
}



