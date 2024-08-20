package dev.blockeed.replica.utils;

import dev.blockeed.replica.ReplicaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportUtil {

    public static void teleportOnJoin(Player player, Location location) {
        Bukkit.getServer().getScheduler().runTaskLater(ReplicaPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.teleport(location);
            }
        }, 10);
    }

}
