package ru.traiwy.economy;

import org.bukkit.entity.Player;

public interface EconomyEditor {
    double getBalance(Player player);
    void takeBalance(Player player, int amount);
}
