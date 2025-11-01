package ru.traiwy.service;

import org.bukkit.entity.Player;
import ru.traiwy.data.ClanData;
import ru.traiwy.economy.impl.VaultEco;
import ru.traiwy.enums.TypeClan;
import ru.traiwy.storage.database.MySqlStorage;

public class ClanService {
    private final MySqlStorage storage;
    private final VaultEco vaultEco;

    public ClanService(MySqlStorage storage, VaultEco vaultEco) {
        this.storage = storage;
        this.vaultEco = vaultEco;
    }

    public void createClan(Player player, TypeClan type) {
        if (!vaultEco.takeMoneyForClanCreation(player)) {
            player.sendMessage("§cНедостаточно средств для создания клана!");
            return;
        }

        ClanData clan = new ClanData(player.getName(), 1, type);
        storage.addClan(clan);

        player.sendMessage("§aКлан успешно создан! Тип: " + type);
    }
}
