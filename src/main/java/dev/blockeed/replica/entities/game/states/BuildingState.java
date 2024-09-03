package dev.blockeed.replica.entities.game.states;

import dev.blockeed.replica.entities.game.GameState;
import dev.blockeed.replica.entities.images.Image;
import dev.blockeed.replica.entities.map.Island;
import dev.blockeed.replica.enums.Messages;
import dev.blockeed.replica.enums.Titles;
import dev.blockeed.replica.managers.*;
import dev.blockeed.replica.utils.FrameUtil;
import dev.blockeed.replica.utils.Settings;
import dev.blockeed.replica.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BuildingState extends GameState {

    private Long startTime;
    private Image currentImage;

    public BuildingState(Image currentImage, int stage) {
        super(Settings.INITIAL_BUILDING_TIME - stage * Settings.BUILDING_TIME_DECREMENT);
        this.currentImage = currentImage;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.startTime = System.currentTimeMillis();
        PlayerManager.clearDonePlayers();
        GameManager.getOnlineAlivePlayers().forEach(player -> {
            player.getInventory().setItem(0, new ItemStack(Material.DIAMOND_PICKAXE));
            List<Material> blocks = new HashSet<>(currentImage.getBlocks()).stream().toList();
            for (int i = 0; i < blocks.size(); i++) {
                player.getInventory().setItem(1 + i, new ItemStack(blocks.get(i), 64));
            }
            MessageManager.sendMessage(Messages.ROUND_HAS_STARTED).send(player);
            TitleManager.sendTitle(Titles.ROUND_HAS_STARTED).send(player);
        });
        GameManager.getIslands().forEach(island -> {
            List<Location> locations = FrameUtil.getLocationsInCuboid(island.getFrameTop(), island.getFrameBottom());
            for (int i = 0; i < locations.size(); i++) {
                locations.get(i).getBlock().setType(currentImage.getBlocks().get(i));
            }
        });
    }

    @Override
    public void onDisable() {
        super.onDisable();
        GameManager.getIslands().forEach(island -> {
            FrameUtil.getLocationsInCuboid(island.getFrameTop(), island.getFrameBottom()).forEach(location -> location.getBlock().setType(Material.AIR));
            FrameUtil.getLocationsInCuboid(island.getBuildingFrameTop(), island.getBuildingFrameBottom()).forEach(location -> location.getBlock().setType(Material.AIR));
        });
        GameManager.getOnlinePlayers().forEach(player -> {
            MessageManager.sendMessage(Messages.ROUND_HAS_ENDED).send(player);
            TitleManager.sendTitle(Titles.ROUND_HAS_ENDED).send(player);
        });
    }

    @Override
    protected void onEnd() {
        List<Player> eliminatedPlayers = GameManager.getOnlineAlivePlayers().stream().filter(p -> !PlayerManager.isPlayerDone(p)).toList();
        eliminatedPlayers.forEach(eliminatedPlayer -> {
            PlayerManager.setSpectator(eliminatedPlayer);
            MessageManager.sendMessage(Messages.YOU_HAVE_BEEN_ELIMINATED).send(eliminatedPlayer);
            GameManager.getOnlineAlivePlayers().forEach(alivePlayer -> MessageManager.sendMessage(Messages.PLAYER_HAS_BEEN_ELIMINATED).setValue("player", eliminatedPlayer.getName()).send(alivePlayer));
        });
        GameManager.nextRound();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (PlayerManager.isSpectator(player) || GameManager.getIsland(player) == null) {
            event.setCancelled(true);
            return;
        }
        if (PlayerManager.isPlayerDone(player)) {
            event.setCancelled(true);
            return;
        }

        Island island = GameManager.getIsland(player);
        Location location = event.getBlock().getLocation();
        if (!FrameUtil.isInCube(location, island.getBuildingFrameTop(), island.getBuildingFrameBottom())) {
            event.setCancelled(true);
            return;
        }
        event.setDropItems(false);
        player.getInventory().addItem(new ItemStack(event.getBlock().getType()));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (PlayerManager.isSpectator(player) || GameManager.getIsland(player) == null) {
            event.setCancelled(true);
            return;
        }
        if (PlayerManager.isPlayerDone(player)) {
            event.setCancelled(true);
            return;
        }

        Island island = GameManager.getIsland(player);
        Location location = event.getBlock().getLocation();
        if (!FrameUtil.isInCube(location, island.getBuildingFrameTop(), island.getBuildingFrameBottom())) {
            event.setCancelled(true);
            return;
        }
        List<Material> placedBlocks = FrameUtil.getBlocksInCuboid(island.getBuildingFrameTop(), island.getBuildingFrameBottom());
        for (int i = 0; i < placedBlocks.size(); i++) {
            if (placedBlocks.get(i) != currentImage.getBlocks().get(i)) return;
        }
        // player done
        Long completionInMillis = System.currentTimeMillis() - startTime;
        String time = TimeUtil.convertMillisToTimeFormat(completionInMillis);
        PlayerManager.setDonePlayer(player, completionInMillis);
        GameManager.getOnlinePlayers().forEach(onlinePlayer -> {
            MessageManager.sendMessage(Messages.PLAYER_COMPLETED_THE_IMAGE).setValue("time", time).setValue("player", player.getName()).send(onlinePlayer);
        });
        TitleManager.sendTitle(Titles.SUCCESSFULLY_COMPLETED_THE_IMAGE).setValue("time", time).send(player);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);

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
}
