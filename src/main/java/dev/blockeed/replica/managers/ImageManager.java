package dev.blockeed.replica.managers;

import dev.blockeed.replica.entities.images.Image;
import dev.blockeed.replica.enums.ConfigType;
import dev.blockeed.replica.utils.FrameUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ImageManager {

    private static Map<UUID, Location> topSelection = new HashMap<>();
    private static Map<UUID, Location> bottomSelection = new HashMap<>();
    private static Map<UUID, Image> images = new HashMap<>();

    public static void init() {
        FileConfiguration configuration = ConfigManager.getConfig(ConfigType.IMAGES);
        if (!configuration.isConfigurationSection("images")) return;
        for (String key : configuration.getConfigurationSection("images").getKeys(false)) {
            ConfigurationSection imageSection = configuration.getConfigurationSection("images").getConfigurationSection(key);
            List<Material> blocks = new ArrayList<>();
            ConfigurationSection  blocksSection = imageSection.getConfigurationSection("blocks");
            for (String blockSectionkey : blocksSection.getKeys(false)) {
                blocks.add(Material.valueOf(blocksSection.getString(blockSectionkey)));
            }
            int size = imageSection.getInt("size");
            UUID uuid = UUID.fromString(key);
            Image image = new Image(uuid, blocks, size);
            images.put(uuid, image);
        }
    }

    public static void saveImage(Location frameTop, Location frameBottom) {
        if (!FrameUtil.isOneBlockWide(frameTop, frameBottom)) return;
        int size = FrameUtil.getSize(frameTop, frameBottom);
        FileConfiguration configuration = ConfigManager.getConfig(ConfigType.IMAGES);
        ConfigurationSection imagesSection;
        if (configuration.isConfigurationSection("images")) imagesSection = configuration.getConfigurationSection("images");
        else imagesSection = configuration.createSection("images");
        ConfigurationSection section = imagesSection.createSection(UUID.randomUUID().toString());
        section.set("size", size);
        ConfigurationSection blocksSection = section.createSection("blocks");
        List<Material> blocks = FrameUtil.getBlocksInCuboid(frameTop, frameBottom);
        for (int i = 0; i < blocks.size(); i++) {
            blocksSection.set(i + "", blocks.get(i).toString());
        }
        ConfigManager.saveConfig(ConfigType.IMAGES);
    }

    public static Image getImage(UUID uuid) {
        return images.get(uuid);
    }

    public static Image getRandomImage(List<UUID> previousImages) {
        List<Image> imagesList = new ArrayList<>();
        images.forEach(((uuid, image) -> {
            if (!previousImages.contains(uuid)) imagesList.add(image);
        }));
        if (imagesList.isEmpty()) return null;
        return imagesList.get(ThreadLocalRandom.current().nextInt(imagesList.size()));
    }

    public static Location getTopSelection(Player player) {
        if (!topSelection.containsKey(player.getUniqueId())) return null;
        return topSelection.get(player.getUniqueId());
    }

    public static void setTopSelection(Player player, Location location) {
        topSelection.put(player.getUniqueId(), location);
    }

    public static void setBottomSelection(Player player, Location location) {
        bottomSelection.put(player.getUniqueId(), location);
    }

    public static Location getBottomSelection(Player player) {
        if (!bottomSelection.containsKey(player.getUniqueId())) return null;
        return bottomSelection.get(player.getUniqueId());
    }

}
