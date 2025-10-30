package ru.traiwy.inv.choose;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.economy.impl.VaultEco;
import ru.traiwy.inv.ClanMenu;
import ru.traiwy.manager.config.ConfigManager;
import ru.traiwy.storage.MySqlStorage;
import ru.traiwy.util.ItemUtil;

import java.util.List;

@AllArgsConstructor
public class ChooseMenu implements ClanMenu {
    private final static int CREATE_CLAN_PRICE = 1250000;

    private final VaultEco vaultEco;
    final Inventory inventory = Bukkit.createInventory(this, 45, "Выберите тип клана");

    @Override
    public void open(Player player) {
        build();
        player.openInventory(inventory);
    }

    @Override
    public void build() {
        for (ConfigManager.GuiItem item : ConfigManager.GUI.CHOOSE.item){
            final Material material = item.getMaterial();
            final String name = item.getName();
            final int slot = item.getSlot();
            final List<String> lore = item.getLore();

            ItemStack itemResult = ItemUtil.ItemStack(material, name, lore);
            inventory.setItem(slot, itemResult);
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        final var item = event.getCurrentItem();
        final Player player = (Player) event.getWhoClicked();

        switch(item.getType()){
            case IRON_SWORD:
                takeMoneyForClanCreation(player);
                break;
            case IRON_AXE:
                takeMoneyForClanCreation(player);
                break;

        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    private void takeMoneyForClanCreation(Player player) {
        double bal = vaultEco.getBalance(player);
        if (bal < CREATE_CLAN_PRICE) {
            player.closeInventory();
            player.sendMessage("У вас недостаточно монет для создание клана");
            return;
        }
        vaultEco.takeBalance(player, CREATE_CLAN_PRICE);
    }
}
