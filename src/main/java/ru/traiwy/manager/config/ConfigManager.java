package ru.traiwy.manager.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.spi.MessageFactory2Adapter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ConfigManager {
    private File file;
    private FileConfiguration config;

    private final JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(),"config.yml");
    }


    public void load(){
        if(!file.exists()){
            plugin.saveResource("config.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);

        try {
            parse();
        } catch (Exception e) {
             throw new IllegalStateException("Main config.yml can't load");
        }
    }



    private void parse() {
        ConfigurationSection section = config.getConfigurationSection("message");
        isSectionNull(section);
        try {
            GUI.PVP.item = parseGuiItem("gui.PVP");
            GUI.PVE.item = parseGuiItem("gui.PVE");
            GUI.START.item = parseGuiItem("gui.start");
            GUI.CHOOSE.item = parseGuiItem("gui.choose");
            GUI.EFFECTS.item = parseGuiItem("gui.unique_effects");
            GUI.UPDATE.item = parseGuiItem("gui.update");
            GUI.TREASURY.item = parseGuiItem("gui.treasury");
            GUI.SHOP.item = parseGuiItem("gui.shop");
            GUI.BOTTLED.item = parseGuiItem("gui.bottled");

            MESSAGE.createClan = section.getString("createClan");
            MESSAGE.deleteClan = section.getString("deleteClan");
            MESSAGE.inviteMessage = section.getString("inviteMessage");
            MESSAGE.inviteSentMessage = section.getString("inviteSentMessage");

        } catch (Exception e) {
            Bukkit.getLogger().warning("Config can't parse");
        }

    }

    @Setter
    @Getter
    @RequiredArgsConstructor
    public static class GUI{
        public static class PVP{
           public static List<GuiItem> item;
        }

        public static class PVE{
           public static List<GuiItem> item;
        }

        public static class START{
            public static List<GuiItem> item;
        }

        public static class CHOOSE{
            public static List<GuiItem> item;
        }

        public static class EFFECTS{
            public static List<GuiItem> item;
        }

        public static class UPDATE{
            public static List<GuiItem> item;
        }

        public static class TREASURY{
            public static List<GuiItem> item;
        }

        public static class SHOP{
            public static List<GuiItem> item;
        }
        public static class BOTTLED{
            public static List<GuiItem> item;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class MESSAGE{
        public static  String createClan;
        public static String deleteClan;
        public static String inviteMessage;
        public static String inviteSentMessage;
    }

    @Getter
    @RequiredArgsConstructor
    public static class GuiItem{
        private final String id;
        private final Material material;
        private final int slot;
        private final String name;
        private final List<String> lore;
    }

    public boolean isSectionNull(ConfigurationSection section){
        if(section == null) return false;
        return true;
    }

    private List<GuiItem> parseGuiItem(String path){
         List<Map<?, ?>> rawList = config.getMapList(path);
         List<GuiItem> guiItems = new ArrayList<>();

        for(Map<?, ?> entity : rawList){
            try {
                final String id = (String) entity.get("id");
                final String materialName = (String) entity.get("item");
                final String name = (String) entity.get("name");
                final List<String> lore = (List) entity.get("lore");

                final int slot = (Integer) entity.get("slot");

                if (materialName == null || name == null || slot < 0) continue;

                Material material = Material.matchMaterial(materialName);
                if (material == null) Bukkit.getLogger().warning("Unknown material in " + path + ": " + materialName);

                guiItems.add(new GuiItem(id, material, slot, name, lore));
            } catch (Exception e) {
                Bukkit.getLogger().warning("Error parse");
            }

        }

        return guiItems;
    }
}
