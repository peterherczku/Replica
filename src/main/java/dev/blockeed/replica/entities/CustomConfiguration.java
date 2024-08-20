package dev.blockeed.replica.entities;

import dev.blockeed.replica.ReplicaPlugin;
import dev.blockeed.replica.enums.ConfigType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomConfiguration {

    private final ConfigType configType;
    private File customConfigFile;
    @Getter
    private FileConfiguration customConfig;

    public void createCustomConfig() {
        customConfigFile = new File(ReplicaPlugin.getInstance().getDataFolder(), configType.name().toLowerCase()+".yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            ReplicaPlugin.getInstance().saveResource(configType.name().toLowerCase()+".yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
