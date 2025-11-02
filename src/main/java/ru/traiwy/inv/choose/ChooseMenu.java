package ru.traiwy.inv.choose;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.data.ClanData;
import ru.traiwy.economy.impl.VaultEco;
import ru.traiwy.enums.TypeClan;
import ru.traiwy.inv.ClanMenu;
import ru.traiwy.manager.config.ConfigManager;
import ru.traiwy.service.ClanService;
import ru.traiwy.storage.database.MySqlStorage;
import ru.traiwy.util.ItemUtil;

import java.util.List;

@AllArgsConstructor
public class ChooseMenu implements ClanMenu {
    private final VaultEco vaultEco;
    private final ClanService clanService;

    final Inventory inventory = Bukkit.createInventory(this, 45, "Выберите тип клана");

    @Override
    public void open(Player player) {
        build();
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
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        final var item = event.getCurrentItem();
        final Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        switch (item.getType()) {
            case IRON_SWORD -> clanService.createClan(player, TypeClan.PVP);
            case IRON_HOE -> clanService.createClan(player, TypeClan.PVE);
            default -> player.sendMessage("§cНеизвестный тип клана!");
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

}
