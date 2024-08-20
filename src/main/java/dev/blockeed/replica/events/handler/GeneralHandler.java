package dev.blockeed.replica.events.handler;

import dev.blockeed.replica.entities.game.states.LobbyState;
import dev.blockeed.replica.entities.map.Island;
import dev.blockeed.replica.managers.GameManager;
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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
        if (PlayerManager.isSpectator(event.getPlayer()) || GameManager.getIsland(player) == null) return;
        Island island = GameManager.getIsland(player);
        if (FrameUtil.isInCube(player.getLocation(), island.getIslandTop(), island.getIslandBottom())) return;
        TeleportUtil.teleportOnJoin(player, island.getSpawnLocation());
    }




}
