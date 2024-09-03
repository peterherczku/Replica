package dev.blockeed.replica.events.handler;

import dev.blockeed.replica.entities.game.states.EndingState;
import dev.blockeed.replica.entities.game.states.LobbyState;
import dev.blockeed.replica.entities.map.Island;
import dev.blockeed.replica.enums.Messages;
import dev.blockeed.replica.managers.GameManager;
import dev.blockeed.replica.managers.MessageManager;
import dev.blockeed.replica.managers.PlayerManager;
import dev.blockeed.replica.managers.ScoreboardManager;
import dev.blockeed.replica.utils.FrameUtil;
import dev.blockeed.replica.utils.TeleportUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

public class GeneralHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        ScoreboardManager.addToScoreboard(player);
        if (GameManager.getCurrentState() instanceof LobbyState) return;
        PlayerManager.setSpectator(player);
        TeleportUtil.teleportOnJoin(player, GameManager.getMap().getSpectatorLocation());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        ScoreboardManager.removeFromScoreboard(event.getPlayer().getUniqueId());
        if (GameManager.getCurrentState() instanceof LobbyState || GameManager.getCurrentState() instanceof EndingState) return;
        PlayerManager.setSpectator(event.getPlayer().getUniqueId());
        if (GameManager.getOnlineAlivePlayers().size() == 1) {
            GameManager.nextRound();
            return;
        }
        if (PlayerManager.getDonePlayers().size() != GameManager.getOnlineAlivePlayers().size() - 1) return;
        Player eliminatedPlayer = GameManager.getOnlineAlivePlayers().stream().filter(p -> !PlayerManager.isPlayerDone(p)).findAny().get();
        PlayerManager.setSpectator(eliminatedPlayer);
        MessageManager.sendMessage(Messages.YOU_HAVE_BEEN_ELIMINATED).send(eliminatedPlayer);
        GameManager.getOnlineAlivePlayers().forEach(alivePlayer -> MessageManager.sendMessage(Messages.PLAYER_HAS_BEEN_ELIMINATED).setValue("player", eliminatedPlayer.getName()).send(alivePlayer));
        GameManager.nextRound();
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickUpItem(EntityPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        event.setCancelled(true);
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.VOID || !PlayerManager.isSpectator(player)) return;
        TeleportUtil.teleportOnJoin(player, GameManager.getMap().getSpectatorLocation());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (GameManager.getCurrentState() instanceof LobbyState || GameManager.getCurrentState() instanceof EndingState) return;
        if (PlayerManager.isSpectator(event.getPlayer()) || GameManager.getIsland(player) == null) return;
        Island island = GameManager.getIsland(player);
        if (FrameUtil.isInCube(player.getLocation(), island.getIslandTop(), island.getIslandBottom())) return;
        TeleportUtil.teleportOnJoin(player, island.getSpawnLocation());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (PlayerManager.isSpectator(player)) {
            PlayerManager.getSpectators().forEach(spectator -> {
                spectator.sendMessage("§8§o" + player.getName() + " §7» " + message);
            });
        } else {
            GameManager.getOnlinePlayers().forEach(onlinePlayer -> {
                onlinePlayer.sendMessage("§f" + player.getName() +" §7» " + message);
            });
        }
    }




}
