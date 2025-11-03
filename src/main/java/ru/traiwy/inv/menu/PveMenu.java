package ru.traiwy.inv.menu;

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

public class PveMenu implements ClanMenu {
    private final Inventory inventory;

    private final Map<Integer, MenuAction> actions = new HashMap<>();

    public PveMenu() {
        this.inventory = Bukkit.createInventory(this, 54, "PVE клан");
        build();
    }

    @Override
    public void open(Player player) {
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
            case 23 -> player -> player.sendMessage("help");
            case 21 -> player -> testPveMenu();
            case 19 -> player -> testPveMenu();
            case 10 -> player -> testPveMenu();
            case 28 -> player -> testPveMenu();
            case 25 -> player -> testPveMenu();
            case 2 -> player -> testPveMenu();
            case 6 -> player -> testPveMenu();
            case 42 -> player -> testPveMenu();
            case 38 -> player -> testPveMenu();
            case 16 -> player -> testPveMenu();
            case 34 -> player -> testPveMenu();
            case 53 -> player -> testPveMenu();
            default -> player -> {};
        };
     }

     public void testPveMenu(){

     }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
