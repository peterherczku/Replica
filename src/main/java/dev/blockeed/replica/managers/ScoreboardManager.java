package dev.blockeed.replica.managers;


import dev.blockeed.replica.ReplicaPlugin;
import dev.blockeed.replica.entities.game.states.BuildingState;
import dev.blockeed.replica.entities.game.states.LobbyState;
import dev.blockeed.replica.entities.game.states.WaitingState;
import dev.blockeed.replica.enums.ConfigType;
import dev.blockeed.replica.enums.ScoreboardType;
import dev.blockeed.replica.utils.TimeUtil;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ScoreboardManager {

    private static Map<UUID, FastBoard> boards = new HashMap<>();

    public static void loadScoreboards() {
        FileConfiguration configuration = ConfigManager.getConfig(ConfigType.SCOREBOARD);
        ConfigurationSection scoreboards = configuration.getConfigurationSection("scoreboards");
        for (String scoreboardName : scoreboards.getKeys(false)) {
            ConfigurationSection scoreboardSection = scoreboards.getConfigurationSection(scoreboardName);
            try {
                ScoreboardType type = ScoreboardType.valueOf(scoreboardName);
                String title = scoreboardSection.getString("title");
                List<String> lines = scoreboardSection.getStringList("lines");
                type.setLines(lines);
                type.setTitle(title);
            } catch (Exception ex) {
                System.out.println("Invaid scoreboard name " + scoreboardName);
            }
        }
    }

    public static void initScoreboard() {
        Bukkit.getOnlinePlayers().forEach(ScoreboardManager::addToScoreboard);
        updateTask();
    }

    public static void addToScoreboard(Player player) {
        FastBoard fastBoard = new FastBoard(player);
        fastBoard.updateTitle("§a§lREPLICA");
        boards.put(player.getUniqueId(), fastBoard);
    }

    public static void removeFromScoreboard(UUID uuid) {
        FastBoard fastBoard = boards.remove(uuid);
        if (fastBoard!=null) fastBoard.delete();
    }

    private static void updateTask() {
        Bukkit.getScheduler().runTaskTimer(ReplicaPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                boards.forEach((uuid, fastBoard) -> {

                    if(GameManager.getCurrentState() == null) {
                        fastBoard.updateLines(
                                "§a",
                                "§a§lJELENLEG OFFLINE",
                                "§c",
                                "§7 www.blockeed.dev"
                        );
                        return;
                    }

                    if (GameManager.getCurrentState() instanceof LobbyState) {
                        fastBoard.updateTitle(getLobbyTitle());
                        fastBoard.updateLines(getLobbyLines());
                        return;
                    }

                    if (GameManager.getCurrentState() instanceof WaitingState) {
                        fastBoard.updateTitle(getIngameTitle(ScoreboardType.WAITING));
                        fastBoard.updateLines(getIngameLines(Bukkit.getPlayer(uuid), ScoreboardType.WAITING));
                        return;
                    }

                    fastBoard.updateTitle(getIngameTitle(ScoreboardType.INGAME));
                    fastBoard.updateLines(getIngameLines(Bukkit.getPlayer(uuid), ScoreboardType.INGAME));
                    return;


                });
            }
        },0,20);
    }

    private static String getLobbyTitle() {
        return ScoreboardType.LOBBY.getTitle().replaceAll("&", "§");
    }

    private static String getIngameTitle(ScoreboardType type) {
        return type.getTitle()
                .replaceAll("%time%", TimeUtil.getFormattedTime(GameManager.getCurrentState().getTime()))
                .replaceAll("&", "§");
    }

    private static List<String> getLobbyLines() {
        List<String> lines = new ArrayList<>(ScoreboardType.LOBBY.getLines());
        lines.replaceAll(string -> {
            return string
                    .replaceAll("&", "§")
                    .replaceAll("%date%", TimeUtil.getFormattedDate())
                    .replaceAll("%time%", GameManager.getCurrentState().getTime()+"")
                    .replaceAll("%onlinePlayers%", GameManager.getOnlinePlayers().size()+"");
        });
        return lines;
    }

    private static List<String> getIngameLines(Player player, ScoreboardType type) {
        List<String> lines = new ArrayList<>(type.getLines());
        lines.replaceAll(string -> {
            String s = string
                    .replaceAll("&", "§")
                    .replaceAll("%time%", TimeUtil.getFormattedTime(GameManager.getCurrentState().getTime()))
                    .replaceAll("%arena%", GameManager.getMap().getName())
                    .replaceAll("%date%", TimeUtil.getFormattedDate());
            for(int i = 0; i < 8; i++) {
                if (!s.contains("%island" + i + "%")) continue;
                if (GameManager.getOnlineAlivePlayers().size() < i + 1) {
                    s = s.replaceAll("%island" + i + "%", "§7-");
                    continue;
                }
                List<Player> donePlayers = GameManager.getOnlineAlivePlayers().stream().filter(PlayerManager::isPlayerDone).toList();
                List<Player> buildingPlayers = GameManager.getOnlineAlivePlayers().stream().filter(onlinePlayer -> !PlayerManager.isPlayerDone(onlinePlayer)).toList();
                Player ingamePlayer;
                if (donePlayers.size() >= i + 1) {
                    ingamePlayer = donePlayers.get(i);
                } else {
                    ingamePlayer = buildingPlayers.get(i - donePlayers.size());
                }
                s = s.replaceAll("%island" + i + "%", "§f" + ingamePlayer.getName()+": " + getIslandStatus(ingamePlayer));
            }
            return s;
        });
        return lines;
    }

    private static String getIslandStatus(Player player) {
        if (PlayerManager.isPlayerDone(player)) {
            return "§a" + TimeUtil.convertMillisToTimeFormat(PlayerManager.getCompletionTime(player));
        } else {
            return "§c" + "✖";
        }
    }

}

