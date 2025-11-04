package ru.traiwy;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.command.clan.ClanCommand;
import ru.traiwy.economy.impl.VaultEco;
import ru.traiwy.event.PlayerQuitListener;
import ru.traiwy.inv.choose.ChooseMenu;
import ru.traiwy.inv.choose.StartMenu;
import ru.traiwy.inv.menu.PveMenu;
import ru.traiwy.inv.menu.PvpMenu;
import ru.traiwy.inv.sections.bottled.BottledMenu;
import ru.traiwy.inv.sections.effects.UniqueEffectsMenu;
import ru.traiwy.inv.sections.treasury.TreasuryMenu;
import ru.traiwy.inv.sections.treasury.manager.TreasuryManager;
import ru.traiwy.inv.sections.update.UpdateMenu;
import ru.traiwy.manager.ChatInputManager;
import ru.traiwy.manager.config.ConfigDBManager;
import ru.traiwy.manager.config.ConfigManager;
import ru.traiwy.service.ClanService;
import ru.traiwy.service.GuiService;
import ru.traiwy.storage.cache.ClanCache;
import ru.traiwy.storage.database.ClanRepository;
import ru.traiwy.storage.database.MySqlConnectionManager;
import ru.traiwy.storage.database.MySqlStorage;

public final class HolyClan extends JavaPlugin {

    @Getter
    private Economy economy;
    private MySqlStorage mySqlStorage;
    private MySqlConnectionManager manager;


    @Override
    public void onEnable() {

        saveDefaultConfig();
        reloadConfig();
        setupEconomy();
        final ConfigManager configManager = new ConfigManager(this);
        final ConfigDBManager configDBManager = new ConfigDBManager(this);

        configManager.load();
        configDBManager.load();

        manager = new MySqlConnectionManager(this, manager);
        mySqlStorage = new MySqlStorage(this, manager);
        final ClanRepository clanRepository = new ClanRepository(manager);


        final VaultEco vaultEco = new VaultEco(economy);
        final ClanCache clanCache = new ClanCache();


        TreasuryManager treasuryManager = new TreasuryManager(vaultEco, mySqlStorage, this, clanCache);

        UniqueEffectsMenu uniqueEffectsMenu = new UniqueEffectsMenu();
        TreasuryMenu treasuryMenu = new TreasuryMenu(treasuryManager);
        UpdateMenu updateMenu = new UpdateMenu();
        BottledMenu bottledMenu = new BottledMenu();


        final PvpMenu pvpMenu = new PvpMenu(updateMenu, uniqueEffectsMenu, treasuryMenu, bottledMenu);
        final PveMenu pveMenu = new PveMenu();
        ChatInputManager chatInputManager = new ChatInputManager(this);
        final ClanService clanService = new ClanService(mySqlStorage, vaultEco, pveMenu, pvpMenu, clanCache);
        final ChooseMenu chooseMenu = new ChooseMenu(vaultEco, clanService, this, chatInputManager);
        final StartMenu startMenu = new StartMenu(chooseMenu);

        getCommand("clan").setExecutor(new ClanCommand(this, clanCache, mySqlStorage, startMenu, pveMenu, pvpMenu));
        getServer().getPluginManager().registerEvents(new GuiService(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(clanCache), this);

        getServer().getPluginManager().registerEvents(chatInputManager, this);

    }

    @Override
    public void onDisable() {
        if (mySqlStorage != null) {
            manager.shutdown();
        }
    }



    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> provider = getServer().getServicesManager().getRegistration(Economy.class);
        if (provider == null) {
            return false;
        }
        economy = provider.getProvider();
        return economy != null;
    }
}

