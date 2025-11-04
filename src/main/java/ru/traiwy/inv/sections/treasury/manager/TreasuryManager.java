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
        int amount = type.getAmount();

        if (vaultEco.getBalance(player) < amount) {
            player.sendMessage("§cУ вас недостаточно денег!");
            return;
        }

        ClanData clanData = clanCache.get(player.getName());
        if (clanData != null) {
            processInvestment(player, clanData, amount);
        } else {
            mySqlStorage.getClan(player).thenAccept(loadedClan -> {
                if (loadedClan != null) {
                    clanCache.put(loadedClan);
                    processInvestment(player, loadedClan, amount);
                } else {
                    player.sendMessage("§cВаш клан не найден!");
                }
            });
        }
    }

        public void takeBalanceTreasury(Player player, InvestmentType type){
        int amount = type.getAmount();

        ClanData clanData = clanCache.get(player.getName());
        if(clanData != null){
            processTakeBalanceTreasury(player, clanData, amount);
        } else {
            mySqlStorage.getClan(player).thenAccept(loadedClan -> {
                if (loadedClan != null) {
                    clanCache.put(loadedClan);
                    processTakeBalanceTreasury(player, loadedClan, amount);
                } else {
                    player.sendMessage("§cВаш клан не найден!");
                }
            });
        }

    }

    private void processTakeBalanceTreasury(Player player, ClanData clanData, int amount){
        long balance = clanData.getBalance();

        if(balance < amount){
            player.sendMessage("§cВ казне недостаточно монеток");
            return;
        }

        clanData.setBalance(balance - amount);
        vaultEco.addBalanse(player, amount);
        clanCache.put(clanData);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            mySqlStorage.updateClan(clanData);
        });

        player.sendMessage("§aВы сняли с казны: " + amount + " монеток.");
    }


    private void processInvestment(Player player, ClanData clanData, int amount) {
        if (!vaultEco.withdraw(player, amount)) {
            player.sendMessage("§cОшибка снятия денег!");
            return;
        }

        clanData.setBalance(clanData.getBalance() + amount);
        clanCache.put(clanData);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            mySqlStorage.updateClan(clanData);
        });

        player.sendMessage("§aВы вложили " + amount + " в казну!");
    }

}


