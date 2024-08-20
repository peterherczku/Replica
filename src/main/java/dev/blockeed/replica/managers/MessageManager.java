package dev.blockeed.replica.managers;

import dev.blockeed.replica.enums.ConfigType;
import dev.blockeed.replica.enums.MessageType;
import dev.blockeed.replica.enums.Messages;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class MessageManager {

    public static void init() {
        FileConfiguration configuration = ConfigManager.getConfig(ConfigType.MESSAGES);
        ConfigurationSection allMessagesSection = configuration.getConfigurationSection("messages");
        for (String messages : allMessagesSection.getKeys(false)) {
            try {
                Messages message = Messages.valueOf(messages);
                if (allMessagesSection.isList(messages)) {
                    List<String> lines = allMessagesSection.getStringList(messages);
                    lines.replaceAll(s -> s.replaceAll("&", "§"));
                    message.setType(MessageType.MULTIPLE_LINE);
                    message.setLines(lines);
                } else {
                    message.setType(MessageType.SINGLE_LINE);
                    message.setText(allMessagesSection.getString(messages).replaceAll("&", "§"));
                }
            } catch (Exception ex) {
                System.out.println("Couldn't load message with name " + messages +". Message name not found.");
            }
        }
    }

    public static Builder sendMessage(Messages messages) {
        return new Builder(messages);
    }

    public static class Builder {

        private String text;
        private List<String> lines;
        private MessageType type;

        public Builder(Messages messages) {
            this.type = messages.getType();
            if (messages.getType() == MessageType.SINGLE_LINE) this.text = messages.getText();
            if (messages.getType() == MessageType.MULTIPLE_LINE) this.lines = messages.getLines();
        }

        public Builder setValue(String placeholder, String value) {
            if (type == MessageType.SINGLE_LINE) this.text = this.text.replaceAll("%" + placeholder + "%", value);
            if (type == MessageType.MULTIPLE_LINE) lines.replaceAll((string) -> string.replaceAll("%" + placeholder + "%", value));
            return this;
        }

        public void send(Player player) {
            if (type == MessageType.SINGLE_LINE) player.sendMessage(Messages.PREFIX.getText() + this.text);
            if (type == MessageType.MULTIPLE_LINE) {
                lines.forEach(line -> player.sendMessage(Messages.PREFIX.getText() + line));
            }
        }

    }

}
