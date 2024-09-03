package dev.blockeed.replica.managers;

import dev.blockeed.replica.entities.game.GameState;
import dev.blockeed.replica.entities.game.states.EndingState;
import dev.blockeed.replica.entities.game.states.LobbyState;
import dev.blockeed.replica.entities.game.states.WaitingState;
import dev.blockeed.replica.entities.images.Image;
import dev.blockeed.replica.entities.map.Island;
import dev.blockeed.replica.entities.map.Map;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameManager {

    @Getter
    @Setter
    private static Map map;
    @Setter
    private static java.util.Map<UUID, Island> players;
    @Getter
    private static GameState currentState;
    @Getter
    private static List<Image> previousImages = new ArrayList<>();
    @Getter @Setter
    private static int buildingStage = 0;

    public static void init() {
        map = MapManager.loadMap("replica");
        if (map == null) return;
        players = new HashMap<>();
        setState(new LobbyState());
    }

    public static void stop() {
        if (!isRunning()) return;
        getOnlinePlayers().forEach(player -> player.kick(Component.text("Az aréna újraindul")));
        map = null;
        players.clear();
        previousImages.clear();
        buildingStage = 0;
        setState(null);
        PlayerManager.clearSpectators();
        PlayerManager.clearDonePlayers();
    }

    public static void restart() {
        stop();
        init();
    }

    public static boolean isRunning() {
        return currentState != null;
    }

    public static void setState(GameState state) {
        if (currentState != null) currentState.onDisable();
        currentState = state;
        if (currentState != null) currentState.onEnable();
    }

    public static List<Player> getOnlinePlayers() {
        return players.keySet().stream().filter(uuid -> Bukkit.getPlayer(uuid) != null).map(Bukkit::getPlayer).toList();
    }

    public static List<Player> getOnlineAlivePlayers() {
        return getOnlinePlayers().stream().filter(player -> !PlayerManager.isSpectator(player)).toList();
    }

    public static List<Island> getIslands() {
        return players.values().stream().toList();
    }

    public static Island getIsland(Player player) {
        return getIsland(player.getUniqueId());
    }

    public static Island getIsland(UUID uuid) {
        return players.get(uuid);
    }

    public static Island getFreeIsland() {
        return map.getIslands().stream().filter(island -> {
            return !players.values().stream().map(Island::getUuid).toList().contains(island.getUuid());
        }).findAny().orElse(null);
    }

    public static void addPlayer(Player player, Island island) {
        players.put(player.getUniqueId(), island);
    }

    public static void removePlayer(Player player) {
        if (!players.containsKey(player.getUniqueId())) return;

    }

    public static void addPreviousImage(Image image) {
        previousImages.add(image);
    }

    public static void nextRound() {
        if (PlayerManager.getDonePlayers().size() == 1) {
            GameManager.setState(new EndingState(Bukkit.getPlayer(PlayerManager.getDonePlayers().keySet().stream().toList().get(0))));
            return;
        }
        if (GameManager.getOnlineAlivePlayers().size() == 1) {
            GameManager.setState(new EndingState(GameManager.getOnlineAlivePlayers().get(0)));
            return;
        }
        GameManager.setState(new WaitingState());
    }

    public static java.util.Map<Player, Island> getPlayerIslands() {
        java.util.Map<Player, Island> islands = new HashMap<>();
        players.forEach(((uuid, island) -> {
            if (Bukkit.getPlayer(uuid) != null) islands.put(Bukkit.getPlayer(uuid), island);
        }));
        return islands;
    }

}
