package dev.blockeed.replica.entities.game;

import dev.blockeed.replica.ReplicaPlugin;
import dev.blockeed.replica.events.handler.GeneralHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class GameState extends BukkitRunnable implements Listener {

    @Getter
    private int time;
    private GeneralHandler generalHandler = new GeneralHandler();

    public GameState(int time) {
        this.time = time;
    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, ReplicaPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(generalHandler, ReplicaPlugin.getInstance());
        this.runTaskTimer(ReplicaPlugin.getInstance(), 0, 20);
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
        HandlerList.unregisterAll(generalHandler);
        if (!this.isCancelled()) this.cancel();
    }

    @Override
    public void run() {
        if (time == 0) {
            onEnd();
            this.cancel();
            return;
        }

        if (time > 0) time--;
    }

    protected abstract void onEnd();

}
