package ru.traiwy;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.command.OpenCommand;
import ru.traiwy.economy.impl.VaultEco;
import ru.traiwy.inv.choose.ChooseMenu;
import ru.traiwy.inv.choose.StartMenu;
import ru.traiwy.inv.menu.PveMenu;
import ru.traiwy.inv.menu.PvpMenu;
import ru.traiwy.manager.config.ConfigDBManager;
import ru.traiwy.manager.config.ConfigManager;
import ru.traiwy.service.GuiService;
import ru.traiwy.storage.MySqlStorage;

public final class HolyClan extends JavaPlugin {

    @Getter
    private Economy economy;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        reloadConfig();
        setupEconomy();
        final ConfigManager configManager = new ConfigManager(this);
        final ConfigDBManager configDBManager = new ConfigDBManager(this);

        final MySqlStorage mySqlStorage = new MySqlStorage(this);
        final VaultEco vaultEco = new VaultEco(economy);
        configManager.load();
        configDBManager.load();

        final PvpMenu pvpMenu = new PvpMenu();
        final PveMenu pveMenu = new PveMenu();
        final ChooseMenu chooseMenu = new ChooseMenu(vaultEco);
        final StartMenu startMenu = new StartMenu(chooseMenu);

        getCommand("clan").setExecutor(new OpenCommand(pvpMenu, pveMenu, startMenu));
        getServer().getPluginManager().registerEvents(new GuiService(), this);

    }

    @Override
    public void onDisable() {

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
