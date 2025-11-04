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
import ru.traiwy.inv.sections.bottled.BottledMenu;
import ru.traiwy.inv.sections.effects.UniqueEffectsMenu;
import ru.traiwy.inv.sections.treasury.TreasuryMenu;
import ru.traiwy.inv.sections.update.UpdateMenu;
import ru.traiwy.manager.config.ConfigManager;
import ru.traiwy.util.ItemUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PvpMenu implements ClanMenu {
    private final UpdateMenu updateMenu;
    private final UniqueEffectsMenu uniqueEffectsMenu;
    private final TreasuryMenu treasuryMenu;
    private final BottledMenu bottledMenu;
    private final Inventory inventory = Bukkit.createInventory(this, 54, "PVP клан");

    private final Map<Integer, MenuAction> actions = new HashMap<>();

    public PvpMenu(UpdateMenu updateMenu, UniqueEffectsMenu uniqueEffectsMenu, TreasuryMenu treasuryMenu, BottledMenu bottledMenu) {
        this.updateMenu = updateMenu;
        this.uniqueEffectsMenu = uniqueEffectsMenu;
        this.treasuryMenu = treasuryMenu;
        this.bottledMenu = bottledMenu;
        build();
    }

    @Override
    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public void build() {
        for(ConfigManager.GuiItem item : ConfigManager.GUI.PVP.item) {
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
        if (event.getView().getTopInventory() != inventory) return;
        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        final int slot = event.getSlot();

        final MenuAction action = actions.get(slot);
        if (action != null && event.getWhoClicked() instanceof Player player) {
            action.execute(player);
        }
    }

    private MenuAction getActionForItem(ConfigManager.GuiItem item) {
        return switch (item.getSlot()){
            case 23 -> player -> player.sendMessage("help");
            case 21 -> player -> testPvpMenu();
            case 19 -> player -> testPvpMenu();
            case 10 -> player -> testPvpMenu();
            case 28 -> player -> bottledMenu.open(player);
            case 25 -> player -> testPvpMenu();
            case 2 -> player -> updateMenu.open(player);
            case 6 -> player -> uniqueEffectsMenu.open(player);
            case 42 -> player -> testPvpMenu();
            case 38 -> player -> treasuryMenu.open(player);
            case 16 -> player -> testPvpMenu();
            case 34 -> player -> testPvpMenu();
            case 53 -> player -> testPvpMenu();
            default -> player -> {};
        };
    }

    public void testPvpMenu(){
        //реализация методов которых еще нет
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
