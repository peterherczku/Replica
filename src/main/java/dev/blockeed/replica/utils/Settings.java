package dev.blockeed.replica.utils;

import dev.blockeed.replica.enums.ConfigType;
import dev.blockeed.replica.managers.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Settings {

    public static int LOBBY_TIME = 10;
    public static int WAITING_TIME = 5;
    public static int INITIAL_BUILDING_TIME = 120;
    public static int BUILDING_TIME_DECREMENT = 10;
    public static int ENDING_TIME = 10;
    public static int MIN_PLAYERS = 2;

    public static void init() {
        FileConfiguration configuration = ConfigManager.getConfig(ConfigType.SETTINGS);
        if (!configuration.isConfigurationSection("settings")) return;
        ConfigurationSection settings = configuration.getConfigurationSection("settings");
        MIN_PLAYERS = settings.getInt("minimumPlayers");
        if (!configuration.isConfigurationSection("time")) return;
        ConfigurationSection time = configuration.getConfigurationSection("time");
        LOBBY_TIME = time.getInt("lobby");
        WAITING_TIME = time.getInt("waiting");
        INITIAL_BUILDING_TIME = time.getInt("initialBuilding");
        BUILDING_TIME_DECREMENT = time.getInt("buildingDecrement");
        ENDING_TIME = time.getInt("ending");
    }

}
