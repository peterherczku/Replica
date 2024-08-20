package dev.blockeed.replica.managers;

import dev.blockeed.replica.ReplicaPlugin;
import dev.blockeed.replica.enums.HotbarType;
import dev.blockeed.replica.enums.Messages;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class PlayerManager {

    @Getter
    private static Map<UUID, Long> donePlayers = new HashMap<>();
    private static List<UUID> spectators = new ArrayList<>();

    public static void setSpectator(UUID uuid) {
        spectators.add(uuid);
    }

    public static void setSpectator(Player player) {
        setSpectator(player.getUniqueId());
        resetPlayer(player);
        player.setAllowFlight(true);
        player.setFlying(true);
        HotbarManager.getHotbar(HotbarType.SPECTATOR).setHotbar(player);
        player.getInventory().setArmorContents(null);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000, 1000));
        GameManager.getOnlinePlayers().forEach(otherPlayer -> {
            if (!otherPlayer.getUniqueId().equals(player.getUniqueId())) {
                if (PlayerManager.isSpectator(otherPlayer)) {
                    otherPlayer.showPlayer(ReplicaPlugin.getInstance(), player);
                    player.showPlayer(ReplicaPlugin.getInstance(), otherPlayer);
                } else {
                    otherPlayer.hidePlayer(ReplicaPlugin.getInstance(), player);
                    player.showPlayer(ReplicaPlugin.getInstance(), otherPlayer);
                }
            }
        });
        MessageManager.sendMessage(Messages.SPECTATOR_MODE).send(player);
    }

    public static boolean isSpectator(UUID uuid) {
        return spectators.contains(uuid);
    }

    public static boolean isSpectator(Player player) {
        return isSpectator(player.getUniqueId());
    }

    public static void resetPlayer(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setHealth(20);
        player.setInvisible(false);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.setFoodLevel(20);
        player.setArrowsInBody(0);
        player.setArrowCooldown(0);
        player.setFireTicks(0);
        player.setVisualFire(false);
        player.setExp(0);
        player.setFallDistance(0);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
    }

    public static boolean isPlayerDone(Player player) {
        return donePlayers.containsKey(player.getUniqueId());
    }

    public static Long getCompletionTime(Player player) {
        return donePlayers.get(player.getUniqueId());
    }

    public static void setDonePlayer(Player player, Long completionTime) {
        donePlayers.put(player.getUniqueId(), completionTime);
        player.getInventory().clear();
    }

    public static void clearDonePlayers() {
        donePlayers.clear();
    }

}
