package ru.traiwy.inv.menu;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.inv.ClanMenu;
import ru.traiwy.inv.MenuAction;
import ru.traiwy.inv.sections.effects.UniqueEffectsMenu;
import ru.traiwy.inv.sections.treasury.TreasuryMenu;
import ru.traiwy.inv.sections.update.UpdateMenu;
import ru.traiwy.manager.config.ConfigManager;
import ru.traiwy.util.ItemUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class PvpMenu implements ClanMenu {
    private final UpdateMenu updateMenu;
    private final UniqueEffectsMenu uniqueEffectsMenu;
    private final TreasuryMenu treasuryMenu;
    private final Inventory inventory = Bukkit.createInventory(this, 54, "PVP клан");

    private final Map<Integer, MenuAction> actions = new HashMap<>();

    @Override
    public void open(Player player) {
        build();
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
        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        final int slot = event.getSlot();

        final MenuAction action = actions.get(slot);
        if (action != null && event.getWhoClicked() instanceof Player player) {
            action.execute(player);
        }
    }

    private MenuAction getActionForItem(ConfigManager.GuiItem item) {
        return switch (item.getId()){
            case "pvp_help" -> player -> player.sendMessage("help");
            case "pvp_clan_info" -> player -> testPvpMenu();
            case "pvp_clan_list" -> player -> testPvpMenu();
            case "pvp_disabled_section" -> player -> testPvpMenu();
            case "pvp_stack_potions" -> player -> testPvpMenu();
            case "pvp_random_event" -> player -> testPvpMenu();
            case "pvp_upgrade_clan" -> player -> updateMenu.open(player);
            case "pvp_unique_effects" -> player -> uniqueEffectsMenu.open(player);
            case "pvp_clan_shop" -> player -> testPvpMenu();
            case "pvp_treasury" -> player -> treasuryMenu.open(player);
            case "pvp_duel" -> player -> testPvpMenu();
            case "pvp_captures" -> player -> testPvpMenu();
            case "pvp_exit" -> player -> testPvpMenu();
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
