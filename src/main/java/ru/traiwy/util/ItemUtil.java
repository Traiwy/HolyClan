package ru.traiwy.util;

import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.traiwy.builder.ItemStackBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemUtil {

    public static ItemStack ItemStack(Material material, String name, List<String> lore) {

        if (material == null) material = Material.BARRIER;
        if (name == null) name = " ";
        if (lore == null) lore = Collections.emptyList();

        List<Component> loreComponents = lore.stream()
                .map(line -> LegacyComponentSerializer.legacySection().deserialize(line).decoration(TextDecoration.ITALIC, false))
                .collect(Collectors.toList());

        return ItemStackBuilder.of(material)
                .name(LegacyComponentSerializer.legacyAmpersand().deserialize(name).decoration(TextDecoration.ITALIC, false))
                .lore(loreComponents)
                .build();
    }
}

