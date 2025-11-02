package ru.traiwy.inv.sections.treasury.manager;

import lombok.AllArgsConstructor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import ru.traiwy.economy.impl.VaultEco;

@AllArgsConstructor
public class TreasuryManager {

    private static int INVESTMENT_1M = 1000000;
    private static int INVESTMENT_100k = 100000;
    private static int INVESTMENT_10k = 10000;
    private static int INVESTMENT_1k = 1000;


    private final Economy economy;
    private final VaultEco vaultEco;


    public boolean takeInvestment1M(Player player){
        final double balance = vaultEco.getBalance(player);
        if (balance < INVESTMENT_1M) return false;

        vaultEco.withdraw(player, INVESTMENT_1M);
        return true;
    }

    public boolean takeInvestment100k(Player player){
        final double balance = vaultEco.getBalance(player);
        if (balance < INVESTMENT_100k) return false;

        vaultEco.withdraw(player, INVESTMENT_100k);
        return true;
    }

    public boolean takeInvestment10k(Player player){
        final double balance = vaultEco.getBalance(player);
        if (balance < INVESTMENT_10k) return false;

        vaultEco.withdraw(player, INVESTMENT_10k);
        return true;
    }

    public boolean takeInvestment1k(Player player){
        final double balance = vaultEco.getBalance(player);
        if (balance < INVESTMENT_1k) return false;

        vaultEco.withdraw(player, INVESTMENT_1k);
        return true;
    }

}
