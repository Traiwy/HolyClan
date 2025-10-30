package ru.traiwy.inv;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public interface ClanMenu extends InventoryHolder{
    void open(Player player);
    void build();
    void onClick(InventoryClickEvent event);
}
