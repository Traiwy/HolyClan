package ru.traiwy.inv.sections.treasury;

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
import ru.traiwy.inv.sections.treasury.manager.TreasuryManager;
import ru.traiwy.manager.config.ConfigManager;
import ru.traiwy.util.ItemUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class TreasuryMenu implements ClanMenu {
    private final Inventory inventory = Bukkit.createInventory(this, 54, "Казна клана");
    private final Map<Integer, MenuAction> actions = new HashMap<>();

    private final TreasuryManager treasuryManager;

    @Override
    public void open(Player player) {
        build();
        player.openInventory(inventory);
    }

    @Override
    public void build() {
        for(ConfigManager.GuiItem item : ConfigManager.GUI.TREASURY.item){
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

        if(event.getCurrentItem() == null) return;
        final int slot = event.getSlot();

        final MenuAction action = actions.get(slot);
        if(action != null && event.getWhoClicked() instanceof Player player){
            action.execute(player);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    private MenuAction getActionForItem(ConfigManager.GuiItem item){
        return switch (item.getId()){
            case "treasury_invest_1m" -> player -> treasuryManager.takeInvestment1M(player);
            case "treasury_invest_100k" -> player -> treasuryManager.takeInvestment100k(player);
            case "treasury_invest_10k" -> player -> treasuryManager.takeInvestment10k(player);
            case "treasury_invest_1k" -> player -> treasuryManager.takeInvestment1k(player);
            case "treasury_balance" -> player -> testKazna();
            case "treasury_withdraw_1k" -> player -> testKazna();
            case "treasury_withdraw_10k" -> player -> testKazna();
            case "treasury_withdraw_100k" -> player -> testKazna();
            case "treasury_withdraw_1m" -> player -> testKazna();
            case "treasury_back" -> player -> testKazna();

            default -> player -> {};
        };
    }

    public void testKazna(){
        //реализовать действия казны
    }
}
