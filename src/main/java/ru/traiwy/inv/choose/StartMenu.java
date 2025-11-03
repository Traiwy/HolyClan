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
import ru.traiwy.inv.MenuAction;
import ru.traiwy.manager.config.ConfigManager;
import ru.traiwy.util.ItemUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartMenu implements ClanMenu {
    private final ChooseMenu chooseMenu;

    final Inventory inventory = Bukkit.createInventory(this, 45, "Кланы");
    private final Map<Integer, MenuAction> actions = new HashMap<>();

    public StartMenu(ChooseMenu chooseMenu) {
        this.chooseMenu = chooseMenu;
        build();
    }

    @Override
    public void open(Player player) {
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
            actions.put(slot,getActionForItem(item) );
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        final int slot = event.getSlot();
        event.setCancelled(true);

        MenuAction action = actions.get(slot);
        if(action != null && event.getWhoClicked() instanceof  Player player) {
            action.execute(player);
        }
    }

     private MenuAction getActionForItem(ConfigManager.GuiItem item) {
        return switch (item.getSlot()){
            case 21 -> player -> chooseMenu.open(player);
            case 23 -> player -> player.closeInventory();
            case 44 -> player -> player.closeInventory();
            default -> player -> {};
        };
     }


    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
