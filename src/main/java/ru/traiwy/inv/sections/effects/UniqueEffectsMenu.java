package ru.traiwy.inv.sections.effects;

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

public class UniqueEffectsMenu implements ClanMenu {
    private final static int[] GRAY_PANEL = {0,1,2,3,5,6,7,8,9,17,18,26,27,35,36,37,38,39,40,41,42,43,44};
    private final static int[] BLUE_PANEL = {45,47,48,49,50,51};

    private final Inventory inventory = Bukkit.createInventory(this, 54, "Уникальные эффекты");

    public UniqueEffectsMenu(){
        build();
    }
    @Override
    public void open(Player player) {

        player.openInventory(inventory);
    }

    @Override
    public void build() {
        for(ConfigManager.GuiItem item : ConfigManager.GUI.EFFECTS.item) {
            final Material material = item.getMaterial();
            final String name = item.getName();
            final List<String> lore = item.getLore();
            final int slot = item.getSlot();

            ItemStack result = ItemUtil.ItemStack(material, name, lore);
            inventory.setItem(slot, result);
            fillPanelSlots(Material.GRAY_STAINED_GLASS_PANE, GRAY_PANEL);
            fillPanelSlots(Material.LIGHT_BLUE_STAINED_GLASS_PANE, BLUE_PANEL);
        }
    }

    private void fillPanelSlots(Material material, int[] slots){
        for(int slot : slots){
            ItemStack grayPanel = new ItemStack(material);
            inventory.setItem(slot, grayPanel);
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
