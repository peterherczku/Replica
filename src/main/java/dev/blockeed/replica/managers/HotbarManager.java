package dev.blockeed.replica.managers;

import dev.blockeed.replica.ReplicaPlugin;
import dev.blockeed.replica.entities.hotbars.Hotbar;
import dev.blockeed.replica.entities.hotbars.HotbarItem;
import dev.blockeed.replica.enums.ConfigType;
import dev.blockeed.replica.enums.HotbarItemType;
import dev.blockeed.replica.enums.HotbarType;
import dev.blockeed.replica.events.handler.HotbarHandler;
import dev.blockeed.replica.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotbarManager {

    private static Map<HotbarType, Hotbar> hotbars = new HashMap<>();

    public static void init() {
        FileConfiguration configuration = ConfigManager.getConfig(ConfigType.HOTBARS);
        ConfigurationSection allHotbarsSection = configuration.getConfigurationSection("hotbars");
        for (String hotbarName : allHotbarsSection.getKeys(false)) {
            try {
                HotbarType hotbarType = HotbarType.valueOf(hotbarName);
                ConfigurationSection hotbarSection = allHotbarsSection.getConfigurationSection(hotbarName);
                ConfigurationSection allItemsSection = hotbarSection.getConfigurationSection("items");
                List<HotbarItem> hotbarItems = new ArrayList<>();
                for (String hotbarItemId : allItemsSection.getKeys(false)) {
                    ConfigurationSection itemSection = allItemsSection.getConfigurationSection(hotbarItemId);
                    String itemTypeName = itemSection.getString("itemType");
                    try {
                        HotbarItemType itemType = HotbarItemType.valueOf(itemTypeName);
                        Material material = Material.valueOf(itemSection.getString("material"));
                        String displayname = itemSection.getString("displayname").replaceAll("&", "§");
                        List<String> lore = itemSection.getStringList("lore");
                        lore.replaceAll(string -> string.replaceAll("&", "§"));
                        int position = itemSection.getInt("position");
                        ItemStack itemStack = new ItemBuilder(material).setDisplayname(displayname).setLore(lore).build();
                        HotbarItem hotbarItem = new HotbarItem(itemType, itemStack, position);
                        hotbarItems.add(hotbarItem);
                    } catch (Exception ex) {
                        System.out.println("Invalid hotbar item type - " + itemTypeName);
                    }
                }
                hotbars.put(hotbarType, new Hotbar(hotbarItems));
            } catch (Exception ex) {
                System.out.println("Invalid hotbar name " + hotbarName);
            }
        }

        Bukkit.getPluginManager().registerEvents(new HotbarHandler(), ReplicaPlugin.getInstance());
    }

    public static Hotbar getHotbar(HotbarType type) {
        return hotbars.get(type);
    }

    public static List<Hotbar> getHotbars() {
        return hotbars.values().stream().toList();
    }

}
