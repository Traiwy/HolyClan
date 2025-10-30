package ru.traiwy.builder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ItemStackBuilder {
    private ItemStack item;

    public ItemStackBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    public ItemStackBuilder(ItemStack item) {
        this.item = item.clone();
    }

    public ItemStackBuilder name(Component name) {
        return editMeta(meta -> meta.displayName(name));
    }

    public ItemStackBuilder lore(List<Component> lore) {
        return editMeta(meta -> meta.lore(lore));
    }

    public ItemStackBuilder lore(Component... lines) {
        return lore(Arrays.asList(lines));
    }

    public ItemStackBuilder glow() {
        return enchant(Enchantment.DURABILITY, 1)
                .flags(ItemFlag.HIDE_ENCHANTS);
    }

    public ItemStackBuilder enchant(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemStackBuilder flags(ItemFlag flags) {
        return editMeta(meta -> meta.addItemFlags(flags));
    }

    public Component format(String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }


    private ItemStackBuilder editMeta(Consumer<ItemMeta> consumer) {
        ItemMeta meta = item.getItemMeta();
        consumer.accept(meta);
        item.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return item;
    }

    public static ItemStackBuilder of(Material material) {
        return new ItemStackBuilder(material);
    }

    public static ItemStackBuilder of(ItemStack item) {
        return new ItemStackBuilder(item);
    }


}
