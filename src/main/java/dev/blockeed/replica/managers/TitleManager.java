package dev.blockeed.replica.managers;

import dev.blockeed.replica.enums.ConfigType;
import dev.blockeed.replica.enums.Titles;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TitleManager {

    public static void init() {
        FileConfiguration configuration = ConfigManager.getConfig(ConfigType.TITLES);
        for (String titles : configuration.getConfigurationSection("titles").getKeys(false)) {
            try {
                Titles title = Titles.valueOf(titles);
                title.setTitle(configuration.getConfigurationSection("titles").getConfigurationSection(titles).getString("title").replaceAll("&", "§"));
                title.setSubtitle(configuration.getConfigurationSection("titles").getConfigurationSection(titles).getString("subtitle").replaceAll("&", "§"));
            } catch (Exception ex) {
                System.out.println("Couldn't load title with name " + titles +". Title name not found.");
            }
        }
    }

    public static Builder sendTitle(Titles titles) {
        return new Builder(titles);
    }

    public static class Builder {

        private String title;
        private String subtitle;

        public Builder(Titles titles) {
            this.title = titles.getTitle();
            this.subtitle = titles.getSubtitle();
        }

        public Builder setValue(String placeholder, String value) {
            this.title = this.title.replaceAll("%" + placeholder + "%", value);
            this.subtitle = this.subtitle.replaceAll("%" + placeholder + "%", value);
            return this;
        }

        public void send(Player player) {
            player.sendTitle(title, subtitle, 10, 20, 10);
        }

    }


}
