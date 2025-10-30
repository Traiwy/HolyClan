package ru.traiwy.inv.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.inv.ClanMenu;
import ru.traiwy.manager.config.ConfigManager;
import ru.traiwy.util.ItemUtil;

import java.util.List;

public class PveMenu implements ClanMenu {
    private final Inventory inventory;

    public PveMenu() {
        this.inventory = Bukkit.createInventory(this, 54, "PVE клан");
    }

    @Override
    public void open(Player player) {
        build();
        player.openInventory(inventory);
    }

    @Override
    public void build() {
        for(ConfigManager.GuiItem item : ConfigManager.GUI.PVE.item) {
            final Material material = item.getMaterial();
            final String name = item.getName();
            final List<String> lore = item.getLore();
            final int slot = item.getSlot();

            ItemStack result = ItemUtil.ItemStack(material, name, lore);
            inventory.setItem(slot, result);
        }

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
