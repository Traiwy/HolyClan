package ru.traiwy.inv.choose;

import lombok.AllArgsConstructor;
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

@AllArgsConstructor
public class StartMenu implements ClanMenu {
    private final ChooseMenu chooseMenu;

    final Inventory inventory = Bukkit.createInventory(this, 45, "Кланы");

    @Override
    public void open(Player player) {
        build();
        player.openInventory(inventory);
    }

    @Override
    public void build() {
        for(ConfigManager.GuiItem item : ConfigManager.GUI.START.item){
            final Material material = item.getMaterial();
            final int slot = item.getSlot();
            final String name = item.getName();
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

        if(item.getType() == Material.TARGET){
            chooseMenu.open(player);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
