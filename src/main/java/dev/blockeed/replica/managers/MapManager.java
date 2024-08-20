package dev.blockeed.replica.managers;

import dev.blockeed.replica.entities.map.Island;
import dev.blockeed.replica.entities.map.Map;
import dev.blockeed.replica.enums.ConfigType;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MapManager {

    public static Map loadMap(String id) {
        FileConfiguration configuration = ConfigManager.getConfig(ConfigType.MAPS);
        if (!configuration.isConfigurationSection("maps")) return null;
        ConfigurationSection allMapsSection = configuration.getConfigurationSection("maps");
        ConfigurationSection mapSection = allMapsSection.getConfigurationSection(id);

        String mapName = mapSection.getString("name");
        List<Island> islands = new ArrayList<>();
        Location spectatorLocation = ConfigManager.readLocation(mapSection.getConfigurationSection("spectatorLocation"));
        for (String islandKey : mapSection.getConfigurationSection("islands").getKeys(false)) {
            ConfigurationSection islandSection = mapSection.getConfigurationSection("islands").getConfigurationSection(islandKey);
            UUID uuid = UUID.fromString(islandKey);
            Location spawnLocation = ConfigManager.readLocation(islandSection.getConfigurationSection("spawnLocation"));
            Location frameTopLocation = ConfigManager.readLocation(islandSection.getConfigurationSection("frameTopLocation"));
            Location frameBottomLocation = ConfigManager.readLocation(islandSection.getConfigurationSection("frameBottomLocation"));
            Location buildingFrameTopLocation = ConfigManager.readLocation(islandSection.getConfigurationSection("buildingFrameTopLocation"));
            Location buildingFrameBottomLocation = ConfigManager.readLocation(islandSection.getConfigurationSection("buildingFrameBottomLocation"));
            Location islandTopLocation = ConfigManager.readLocation(islandSection.getConfigurationSection("islandTopLocation"));
            Location islandBottomLocation = ConfigManager.readLocation(islandSection.getConfigurationSection("islandBottomLocation"));
            Island island = new Island(uuid, spawnLocation, frameTopLocation, frameBottomLocation, buildingFrameTopLocation, buildingFrameBottomLocation, islandTopLocation, islandBottomLocation);
            islands.add(island);
        }
        return new Map(id, mapName, islands, spectatorLocation);
    }

    public static void writeMap(Map map) {
        FileConfiguration configuration = ConfigManager.getConfig(ConfigType.MAPS);

        ConfigurationSection allMapsSection;
        if (configuration.isConfigurationSection("maps")) allMapsSection = configuration.getConfigurationSection("maps");
        else allMapsSection = configuration.createSection("maps");
        ConfigurationSection mapSection = allMapsSection.createSection(map.getId());
        mapSection.set("name", map.getName());
        ConfigManager.saveLocation(map.getSpectatorLocation(), mapSection.createSection("spectatorLocation"));
        ConfigurationSection allIslandSection = mapSection.createSection("islands");
        map.getIslands().forEach(island -> {
            ConfigurationSection islandSection = allIslandSection.createSection(island.getUuid().toString());
            ConfigManager.saveLocation(island.getSpawnLocation(), islandSection.createSection("spawnLocation"));
            ConfigManager.saveLocation(island.getFrameTop(), islandSection.createSection("frameTopLocation"));
            ConfigManager.saveLocation(island.getFrameBottom(), islandSection.createSection("frameBottomLocation"));
            ConfigManager.saveLocation(island.getBuildingFrameTop(), islandSection.createSection("buildingFrameTopLocation"));
            ConfigManager.saveLocation(island.getBuildingFrameBottom(), islandSection.createSection("buildingFrameBottomLocation"));
            ConfigManager.saveLocation(island.getIslandTop(), islandSection.createSection("islandTopLocation"));
            ConfigManager.saveLocation(island.getIslandBottom(), islandSection.createSection("islandBottomLocation"));
        });
        ConfigManager.saveConfig(ConfigType.MAPS);
    }

}
