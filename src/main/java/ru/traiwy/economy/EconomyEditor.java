package ru.traiwy.economy;

import org.bukkit.entity.Player;

public interface EconomyEditor {
    double getBalance(Player player);
    boolean  takeMoneyForClanCreation(Player player);
     void withdraw(Player player, double amount);
}
