package ru.traiwy;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
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
import ru.traiwy.storage.database.clans.ClanRepository;
import ru.traiwy.storage.database.MySqlConnectionManager;
import ru.traiwy.storage.database.clans.ClanStorage;

public final class HolyClan extends JavaPlugin {

    @Getter
    private Economy economy;
    private ClanStorage clanStorage;
    private MySqlConnectionManager manager;


    @Override
    public void onEnable() {

        saveDefaultConfig();
        reloadConfig();

         if (setupEconomy()) {
             getLogger().info("Economy init");
         }else {
             getLogger().warning("❌ Экономика не инициализирована. Некоторые функции будут отключены.");

         }
        final var configManager = new ConfigManager(this);
        final var configDBManager = new ConfigDBManager(this);

        configManager.load();
        configDBManager.load();

        manager = new MySqlConnectionManager(this, manager);
        clanStorage = new ClanStorage(this, manager);
        final var clanRepository = new ClanRepository(manager);


        final var vaultEco = new VaultEco(economy);
        final var clanCache = new ClanCache();


        final var treasuryManager = new TreasuryManager(vaultEco, clanStorage, this, clanCache);

        final var uniqueEffectsMenu = new UniqueEffectsMenu();
        final var treasuryMenu = new TreasuryMenu(treasuryManager);
        final var updateMenu = new UpdateMenu();
        final var bottledMenu = new BottledMenu();


        final var pvpMenu = new PvpMenu(updateMenu, uniqueEffectsMenu, treasuryMenu, bottledMenu);
        final var pveMenu = new PveMenu();
        final var chatInputManager = new ChatInputManager(this);
        final var clanService = new ClanService(clanStorage, vaultEco, pveMenu, pvpMenu, clanCache);
        final var chooseMenu = new ChooseMenu(vaultEco, clanService, this, chatInputManager);
        final var startMenu = new StartMenu(chooseMenu);

        getCommand("clan").setExecutor(new ClanCommand(this, clanCache, clanStorage, startMenu, chatInputManager, pveMenu, pvpMenu));
        getServer().getPluginManager().registerEvents(new GuiService(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(clanCache), this);

        getServer().getPluginManager().registerEvents(chatInputManager, this);

    }

    @Override
    public void onDisable() {
        if (clanStorage != null) {
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

