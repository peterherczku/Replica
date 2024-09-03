package dev.blockeed.replica;

import dev.blockeed.replica.commands.ReplicaCommand;
import dev.blockeed.replica.events.handler.SelectionHandler;
import dev.blockeed.replica.managers.*;
import dev.blockeed.replica.utils.Settings;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ReplicaPlugin extends JavaPlugin {

    @Getter
    private static ReplicaPlugin instance;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        ConfigManager.init();
        MessageManager.init();
        TitleManager.init();
        ImageManager.init();
        HotbarManager.init();
        ScoreboardManager.loadScoreboards();
        ScoreboardManager.initScoreboard();
        Settings.init();
        GameManager.init();

        registerCommands();
        registerHandlers();
    }

    @Override
    public void onDisable() {
        if (GameManager.isRunning()) GameManager.stop();
        CommandAPI.onDisable();
    }

    private void registerHandlers() {
        Bukkit.getPluginManager().registerEvents(new SelectionHandler(), this);
    }

    private void registerCommands() {
        CommandAPI.onEnable();
        CommandAPI.registerCommand(ReplicaCommand.class);
    }

}
