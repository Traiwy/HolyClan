package ru.traiwy.inv.sections.treasury.manager;

import com.mysql.cj.BindValue;
import lombok.AllArgsConstructor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;
import ru.traiwy.data.ClanData;
import ru.traiwy.economy.impl.VaultEco;
import ru.traiwy.enums.InvestmentType;
import ru.traiwy.storage.cache.ClanCache;
import ru.traiwy.storage.database.MySqlStorage;

@AllArgsConstructor
public class TreasuryManager {

    private final VaultEco vaultEco;
    private final MySqlStorage mySqlStorage;
    private final JavaPlugin plugin;
    private final ClanCache clanCache;


    public void takeInvestment(Player player, InvestmentType type) {
        double amount = type.getAmount();

        if (vaultEco.getBalance(player) < amount) {
            player.sendMessage("§cУ вас недостаточно денег!");
            return;
        }

        ClanData clanData = clanCache.get(player.getName());
        if (clanData != null) {
            player.sendMessage("id " + clanData.getId());
            processInvestment(player, clanData, amount);
        } else {
            mySqlStorage.getClan(player).thenAccept(loadedClan -> {
                if (loadedClan != null) {
                    clanCache.put(loadedClan);
                    player.sendMessage("id " + clanData.getId());
                    processInvestment(player, loadedClan, amount);
                } else {
                    player.sendMessage("§cВаш клан не найден!");
                }
            });
        }
    }

    private void processInvestment(Player player, ClanData clanData, double amount) {
        if (!vaultEco.withdraw(player, amount)) {
            player.sendMessage("§cОшибка снятия денег!");
            return;
        }

        clanData.setBalance(clanData.getBalance() + (long) amount);
        clanCache.put(clanData);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            mySqlStorage.updateClan(clanData);
        });

        player.sendMessage("§aВы вложили " + (int) amount + " в казну!");
    }

}


