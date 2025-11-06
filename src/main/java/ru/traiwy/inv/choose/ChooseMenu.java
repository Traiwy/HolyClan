package ru.traiwy.inv.choose;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.economy.impl.VaultEco;
import ru.traiwy.enums.TypeClan;
import ru.traiwy.inv.ClanMenu;
import ru.traiwy.inv.MenuAction;
import ru.traiwy.manager.ChatInputManager;
import ru.traiwy.manager.config.ConfigManager;
import ru.traiwy.service.ClanService;
import ru.traiwy.util.ItemUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseMenu implements ClanMenu {
    private final VaultEco vaultEco;
    private final ClanService clanService;
    private final JavaPlugin plugin;
    private final ChatInputManager chatInputManager;

    final Inventory inventory = Bukkit.createInventory(this, 45, "Выберите тип клана");
    private final Map<Integer, MenuAction> actions = new HashMap<>();

    public ChooseMenu(VaultEco vaultEco, ClanService clanService, JavaPlugin plugin, ChatInputManager chatInputManager) {
        this.vaultEco = vaultEco;
        this.clanService = clanService;
        this.plugin = plugin;
        this.chatInputManager = chatInputManager;
        build();
    }

    @Override
    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public void build() {
        for (ConfigManager.GuiItem item : ConfigManager.GUI.CHOOSE.item){
            final Material material = item.getMaterial();
            final String name = item.getName();
            final int slot = item.getSlot();
            final List<String> lore = item.getLore();

            ItemStack itemResult = ItemUtil.ItemStack(material, name, lore);
            inventory.setItem(slot, itemResult);
            actions.put(slot, getActionForItem(item));
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        final int slot = event.getSlot();
        event.setCancelled(true);
        final MenuAction action = actions.get(slot);
        if (action != null && event.getWhoClicked() instanceof Player player) {
            action.execute(player);
        }

    }

     private MenuAction getActionForItem(ConfigManager.GuiItem item) {
        return switch (item.getSlot()){
            case 20 -> player ->{
                player.closeInventory();
                    chatInputManager.waitForInput(player, ConfigManager.MESSAGE.createClan, clanName -> {
                    clanService.createClan(player, TypeClan.PVP, clanName);
                });
            };
            case 24 -> player ->{
                player.closeInventory();
                chatInputManager.waitForInput(player, ConfigManager.MESSAGE.createClan, clanName -> {
                    clanService.createClan(player, TypeClan.PVE, clanName);
                });
            };
            case 44 -> player -> player.closeInventory();
            default -> player -> {};
        };
     }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

}
