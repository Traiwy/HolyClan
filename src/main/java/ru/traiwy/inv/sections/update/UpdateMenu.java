package ru.traiwy.inv.sections.update;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.inv.ClanMenu;
import ru.traiwy.inv.MenuAction;
import ru.traiwy.manager.config.ConfigManager;
import ru.traiwy.util.ItemUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateMenu implements ClanMenu{
    private final Inventory inventory = Bukkit.createInventory(this, 54, "Улучшение клана");
    private final Map<Integer, MenuAction> actions = new HashMap<>();

    @Override
    public void open(Player player) {
        build();
        player.openInventory(inventory);
    }

    @Override
    public void build() {
        for(ConfigManager.GuiItem item : ConfigManager.GUI.UPDATE.item) {
            final Material material = item.getMaterial();
            final String name = item.getName();
            final List<String> lore = item.getLore();
            final int slot = item.getSlot();

            final ItemStack result = ItemUtil.ItemStack(material, name, lore);
            inventory.setItem(slot, result);

            actions.put(slot, getActionForItem(item));
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        final int slot = event.getSlot();

        final MenuAction action = actions.get(slot);
        if (action != null && event.getWhoClicked() instanceof Player player) {
            action.execute(player);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    private MenuAction getActionForItem(ConfigManager.GuiItem item) {
        return switch (item.getId()){
            case "update_level_up" -> player -> updateTestVoid();
            case "update_points" -> player -> updateTestVoid();
            case "update_back" -> player -> updateTestVoid();
            default -> player -> {};

        };
    }

    public void updateTestVoid(){
        //Реализовать действия повышения уровня клана
    }
}
