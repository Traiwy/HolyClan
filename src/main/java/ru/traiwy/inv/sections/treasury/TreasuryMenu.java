package ru.traiwy.inv.sections.treasury;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.data.ClanData;
import ru.traiwy.enums.InvestmentType;
import ru.traiwy.inv.ClanMenu;
import ru.traiwy.inv.MenuAction;
import ru.traiwy.inv.sections.treasury.manager.TreasuryManager;
import ru.traiwy.manager.config.ConfigManager;
import ru.traiwy.util.ItemUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreasuryMenu implements ClanMenu {
    private final Inventory inventory = Bukkit.createInventory(this, 54, "Казна клана");
    private final Map<Integer, MenuAction> actions = new HashMap<>();

    private final TreasuryManager treasuryManager;

    public TreasuryMenu(TreasuryManager treasuryManager) {
        this.treasuryManager = treasuryManager;
        build();
    }

    @Override
    public void open(Player player) {
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
        return switch (item.getSlot()){
            case 27 -> player -> treasuryManager.takeInvestment(player, InvestmentType.ONE_MILLION);
            case 28 -> player -> treasuryManager.takeInvestment(player, InvestmentType.ONE_HUNDRED_K);
            case 29 -> player -> treasuryManager.takeInvestment(player, InvestmentType.TEN_K);
            case 30 -> player -> treasuryManager.takeInvestment(player, InvestmentType.ONE_K);
            case 13 -> player ->  {};
            case 32 -> player -> treasuryManager.takeBalanceTreasury(player, InvestmentType.ONE_K);
            case 33 -> player -> treasuryManager.takeBalanceTreasury(player, InvestmentType.TEN_K);
            case 34 -> player -> treasuryManager.takeBalanceTreasury(player, InvestmentType.ONE_HUNDRED_K);
            case 35 -> player -> treasuryManager.takeBalanceTreasury(player, InvestmentType.ONE_MILLION);
            case 53 -> player -> player.closeInventory();

            default -> player -> {};
        };
    }

    public void testKazna(){
        //реализовать действия казны
    }
}
