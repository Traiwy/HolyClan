package ru.traiwy.service;

import org.bukkit.entity.Player;
import ru.traiwy.data.ClanData;
import ru.traiwy.economy.impl.VaultEco;
import ru.traiwy.enums.TypeClan;
import ru.traiwy.inv.menu.PveMenu;
import ru.traiwy.inv.menu.PvpMenu;
import ru.traiwy.storage.cache.ClanCache;
import ru.traiwy.storage.database.MySqlStorage;

public class ClanService {
    private final MySqlStorage storage;
    private final VaultEco vaultEco;

    private final PveMenu pveMenu;
    private final PvpMenu pvpMenu;

    private final ClanCache cache;

    public ClanService(MySqlStorage storage, VaultEco vaultEco, PveMenu pveMenu, PvpMenu pvpMenu, ClanCache cache) {
        this.storage = storage;
        this.vaultEco = vaultEco;
        this.pveMenu = pveMenu;
        this.pvpMenu = pvpMenu;
        this.cache = cache;
    }

    public void createClan(Player player, TypeClan type) {
        if (!vaultEco.takeMoneyForClanCreation(player)) {
            player.sendMessage("§cНедостаточно средств для создания клана!");
            return;
        }

        ClanData clan = new ClanData(player.getName(), 1, type);
        storage.addClan(clan);
        openClan(player, type);
        cache.put(clan);

        player.sendMessage("§aКлан успешно создан! Тип: " + type);

    }

    private void openClan(Player player, TypeClan clan){
        switch (clan){
            case PVP: pvpMenu.open(player);
            case PVE: pveMenu.open(player);
        }
    }
}
