package ru.traiwy.inv.sections.shop;

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

public class ShopMenu implements ClanMenu {
    private final static int[] GRAY_PANEL = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,47,48,49,50,51,52};

    private final Inventory inventory = Bukkit.createInventory(this, 54, "Клановый магазин");
    private final Map<Integer, MenuAction> actions = new HashMap<>();

    public ShopMenu(){
        build();
    }

    @Override
    public void open(Player player) {
        build();
        player.openInventory(inventory);
    }

    @Override
    public void build() {
        for(ConfigManager.GuiItem item : ConfigManager.GUI.SHOP.item){
            final Material material = item.getMaterial();
            final String name = item.getName();
            final List<String> lore = item.getLore();
            final int slot = item.getSlot();

            ItemStack result = ItemUtil.ItemStack(material, name, lore);
            inventory.setItem(slot, result);

            actions.put(slot, getActionForItem(item));
        }
        fillPanel(Material.GRAY_STAINED_GLASS_PANE, GRAY_PANEL);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void fillPanel(Material material, int[] panel){
        for(int slot : panel){
            ItemStack item = new ItemStack(material);

            inventory.setItem(slot, item);
        }
    }

    public MenuAction getActionForItem(ConfigManager.GuiItem item){
        return switch (item.getId()){
            case "treasury_back" -> player -> player.closeInventory();
            default -> player -> {};
        };
    }
}
