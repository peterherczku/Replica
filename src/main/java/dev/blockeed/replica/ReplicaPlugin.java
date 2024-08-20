package dev.blockeed.replica;

import dev.blockeed.replica.commands.ReplicaCommand;
import dev.blockeed.replica.events.handler.SelectionHandler;
import dev.blockeed.replica.managers.*;
import dev.jorel.commandapi.CommandAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ReplicaPlugin extends JavaPlugin {

    @Getter
    private static ReplicaPlugin instance;

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
        GameManager.init();

        registerCommands();
        registerHandlers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerHandlers() {
        Bukkit.getPluginManager().registerEvents(new SelectionHandler(), this);
    }

    private void registerCommands() {
        CommandAPI.registerCommand(ReplicaCommand.class);
    }

}
