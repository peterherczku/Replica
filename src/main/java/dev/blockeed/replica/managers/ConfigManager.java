package dev.blockeed.replica.managers;

import dev.blockeed.replica.entities.CustomConfiguration;
import dev.blockeed.replica.enums.ConfigType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private static Map<ConfigType, CustomConfiguration> configs = new HashMap<>();

    public static void init() {
        for (ConfigType type : ConfigType.values()) {
            System.out.println("Loading " + type.name());
            CustomConfiguration customConfiguration = new CustomConfiguration(type);
            customConfiguration.createCustomConfig();
            configs.put(type, customConfiguration);
        }
        System.out.println("Loaded " + configs.size() + " config files.");
    }

    public static FileConfiguration getConfig(ConfigType type) {
        if (!configs.containsKey(type)) return null;
        return configs.get(type).getCustomConfig();
    }

    public static void saveConfig(ConfigType type) {
        if (!configs.containsKey(type)) return;
        configs.get(type).save();
    }

    public static void saveLocation(Location location, ConfigurationSection section) {
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());
        section.set("world", location.getWorld().getName());
    }

    public static Location readLocation(ConfigurationSection section) {
        String worldName = section.getString("world");
        if (Bukkit.getWorld(worldName) == null) return null;
        World world = Bukkit.getWorld(worldName);
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        float yaw = (float) section.getDouble("yaw");
        float pitch = (float) section.getDouble("pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

}
