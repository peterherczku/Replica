package dev.blockeed.replica.commands;

import dev.blockeed.replica.entities.map.Island;
import dev.blockeed.replica.entities.map.Map;
import dev.blockeed.replica.enums.Messages;
import dev.blockeed.replica.managers.ImageManager;
import dev.blockeed.replica.managers.MapManager;
import dev.blockeed.replica.managers.MessageManager;
import dev.blockeed.replica.utils.FrameUtil;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

@Command("replica")
@Permission("replica.admin")
public class ReplicaCommand {

    private static java.util.Map<UUID, Map.MapBuilder> mapBuilder = new HashMap<>();
    private static java.util.Map<UUID, List<Island>> islands = new HashMap<>();
    private static java.util.Map<UUID, Island.IslandBuilder> islandBuilder = new HashMap<>();

    @Default
    public static void replica(Player player) {
        sendHelp(player);
    }

    @Subcommand("createarena")
    public static void createArena(Player player, @AStringArgument String arenaId) {
        if (mapBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.YOU_HAVE_ALREADY_STARTED_CREATING_AN_ARENA).send(player);
            return;
        }
        if (arenaId.contains(" ")) {
            MessageManager.sendMessage(Messages.ARENA_ID_CANNOT_CONTAIN_SPACE).send(player);
            return;
        }
        Map.MapBuilder map = Map.builder().id(arenaId);
        mapBuilder.put(player.getUniqueId(), map);
        MessageManager.sendMessage(Messages.SUCCESSFULLY_STARTED_CREATING_ARENA).send(player);
    }

    @Subcommand("setmapname")
    public static void setMapName(Player player, @AStringArgument String mapName) {
        if (!mapBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.YOU_HAVENT_STARTED_CREATING_AN_ARENA).send(player);
            return;
        }

        Map.MapBuilder map = mapBuilder.get(player.getUniqueId());
        map.name(mapName);
        MessageManager.sendMessage(Messages.SUCCESSFULLY_SET_MAP_NAME).send(player);
    }

    @Subcommand("setspectator")
    public static void setSpectator(Player player) {
        if (!mapBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.YOU_HAVENT_STARTED_CREATING_AN_ARENA).send(player);
            return;
        }

        Map.MapBuilder map = mapBuilder.get(player.getUniqueId());
        map.spectatorLocation(player.getLocation());
        MessageManager.sendMessage(Messages.SUCCESSFULY_SET_SPECTATOR).send(player);
    }

    @Subcommand("createisland")
    public static void createIsland(Player player) {
        if (!mapBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.YOU_HAVENT_STARTED_CREATING_AN_ARENA).send(player);
            return;
        }
        if (islandBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.YOU_HAVE_ALREADY_STARTED_CREATING_AN_ISLAND).send(player);
            return;
        }

        Island.IslandBuilder island = Island.builder().spawnLocation(player.getLocation()).uuid(UUID.randomUUID());
        MessageManager.sendMessage(Messages.SUCCESSFULLY_CREATED_ISLAND_AND_SET_SPAWN).send(player);
        islandBuilder.put(player.getUniqueId(), island);
    }

    @Subcommand("setframe")
    public static void setFrame(Player player) {
        if (!mapBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.YOU_HAVENT_STARTED_CREATING_AN_ARENA).send(player);
            return;
        }
        if (!islandBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.YOU_HAVENT_STARTED_CREATING_AN_ISLAND).send(player);
            return;
        }
        Island.IslandBuilder island = islandBuilder.get(player.getUniqueId());
        if (ImageManager.getTopSelection(player) == null || ImageManager.getBottomSelection(player) == null) {
            MessageManager.sendMessage(Messages.HAVENT_SELECTED_AN_EDGE).send(player);
            return;
        }
        Location topSelection = ImageManager.getTopSelection(player);
        Location bottomSelection = ImageManager.getBottomSelection(player);
        if (!FrameUtil.isOneBlockWide(topSelection, bottomSelection)) {
            MessageManager.sendMessage(Messages.YOUR_SELECTION_IS_NOT_IN_ONE_PLANE).send(player);
            return;
        }
        if (!FrameUtil.isSquare(topSelection, bottomSelection)) {
            MessageManager.sendMessage(Messages.YOUR_SELECTION_IS_NOT_A_SQUARE).send(player);
            return;
        }
        island.frameTop(topSelection).frameBottom(bottomSelection);
        MessageManager.sendMessage(Messages.SUCCESSFULY_SET_FRAME).send(player);
    }

    @Subcommand("setbuildingframe")
    public static void setBuildingFrame(Player player) {
        if (!mapBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.YOU_HAVENT_STARTED_CREATING_AN_ARENA).send(player);
            return;
        }
        if (!islandBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.YOU_HAVENT_STARTED_CREATING_AN_ISLAND).send(player);
            return;
        }
        Island.IslandBuilder island = islandBuilder.get(player.getUniqueId());
        if (ImageManager.getTopSelection(player) == null || ImageManager.getBottomSelection(player) == null) {
            MessageManager.sendMessage(Messages.HAVENT_SELECTED_AN_EDGE).send(player);
            return;
        }
        Location topSelection = ImageManager.getTopSelection(player);
        Location bottomSelection = ImageManager.getBottomSelection(player);
        if (!FrameUtil.isOneBlockWide(topSelection, bottomSelection)) {
            MessageManager.sendMessage(Messages.YOUR_SELECTION_IS_NOT_IN_ONE_PLANE).send(player);
            return;
        }
        if (!FrameUtil.isSquare(topSelection, bottomSelection)) {
            MessageManager.sendMessage(Messages.YOUR_SELECTION_IS_NOT_A_SQUARE).send(player);
            return;
        }
        island.buildingFrameTop(topSelection).buildingFrameBottom(bottomSelection);
        MessageManager.sendMessage(Messages.SUCCESSFULY_SET_BUILDING_FRAME).send(player);
    }

    @Subcommand("setisland")
    public static void setIsland(Player player) {
        if (!mapBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.YOU_HAVENT_STARTED_CREATING_AN_ARENA).send(player);
            return;
        }
        if (!islandBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.YOU_HAVENT_STARTED_CREATING_AN_ISLAND).send(player);
            return;
        }
        Island.IslandBuilder island = islandBuilder.get(player.getUniqueId());
        if (ImageManager.getTopSelection(player) == null || ImageManager.getBottomSelection(player) == null) {
            MessageManager.sendMessage(Messages.HAVENT_SELECTED_AN_EDGE).send(player);
            return;
        }
        Location topSelection = ImageManager.getTopSelection(player);
        Location bottomSelection = ImageManager.getBottomSelection(player);
        island.islandTop(topSelection).islandBottom(bottomSelection);
        MessageManager.sendMessage(Messages.SUCCESSFULY_SET_ISLAND_BOUNDARIES).send(player);
    }

    @Subcommand("saveisland")
    public static void saveIsland(Player player) {
        if (!mapBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.YOU_HAVENT_STARTED_CREATING_AN_ARENA).send(player);
            return;
        }
        if (!islandBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.YOU_HAVENT_STARTED_CREATING_AN_ISLAND).send(player);
            return;
        }
        Island.IslandBuilder island = islandBuilder.get(player.getUniqueId());
        if (island.getIslandBottom() == null || island.getIslandTop() == null || island.getFrameBottom() == null || island.getFrameTop() == null || island.getBuildingFrameBottom() == null || island.getBuildingFrameTop() == null) {
            MessageManager.sendMessage(Messages.YOU_HAVENT_SET_UP_THE_ISLAND_YET).send(player);
            return;
        }
        if (islands.containsKey(player.getUniqueId())) {
            List<Island> newIslands = new ArrayList<>(islands.get(player.getUniqueId()));
            newIslands.add(island.build());
            islands.replace(player.getUniqueId(), newIslands);
        } else {
            islands.put(player.getUniqueId(), Collections.singletonList(island.build()));
        }
        MessageManager.sendMessage(Messages.SUCCESSFULLY_SAVED_ISLAND).send(player);
        islandBuilder.remove(player.getUniqueId());
    }

    @Subcommand("savearena")
    public static void saveArena(Player player) {
        if (!mapBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.YOU_HAVENT_STARTED_CREATING_AN_ARENA).send(player);
            return;
        }
        if (islandBuilder.containsKey(player.getUniqueId())) {
            MessageManager.sendMessage(Messages.FIRST_FINISH_SETTING_UP_THE_ISLAND).send(player);
            return;
        }

        Map.MapBuilder map = mapBuilder.get(player.getUniqueId());
        if (!islands.containsKey(player.getUniqueId()) || islands.get(player.getUniqueId()).size() < 2) {
            MessageManager.sendMessage(Messages.YOU_HAVE_TO_ADD_AT_LEAST_TWO_ISLANDS).send(player);
            return;
        }
        map.islands(islands.get(player.getUniqueId()));
        if (map.getName() == null || map.getSpectatorLocation() == null) {
            MessageManager.sendMessage(Messages.YOU_HAVENT_SET_UP_THE_ARENA_YET).send(player);
            return;
        }

        Map gameMap = map.build();
        MapManager.writeMap(gameMap);
        mapBuilder.remove(player.getUniqueId());
        islands.remove(player.getUniqueId());
        MessageManager.sendMessage(Messages.SUCCESSFULLY_SAVED_ARENA).send(player);
    }

    @Subcommand("saveimage")
    public static void saveImage(Player player) {
        if (ImageManager.getTopSelection(player) == null || ImageManager.getBottomSelection(player) == null) {
            MessageManager.sendMessage(Messages.HAVENT_SELECTED_AN_EDGE).send(player);
            return;
        }
        Location topSelection = ImageManager.getTopSelection(player);
        Location bottomSelection = ImageManager.getBottomSelection(player);
        if (!FrameUtil.isOneBlockWide(topSelection, bottomSelection)) {
            MessageManager.sendMessage(Messages.YOUR_SELECTION_IS_NOT_IN_ONE_PLANE).send(player);
            return;
        }
        if (!FrameUtil.isSquare(topSelection, bottomSelection)) {
            MessageManager.sendMessage(Messages.YOUR_SELECTION_IS_NOT_A_SQUARE).send(player);
            return;
        }
        ImageManager.saveImage(topSelection, bottomSelection);
        MessageManager.sendMessage(Messages.SUCCESSFULLY_SAVED_IMAGE).send(player);
    }

    private static void sendHelp(Player player) {
        MessageManager.sendMessage(Messages.HELP).send(player);
    }

}
