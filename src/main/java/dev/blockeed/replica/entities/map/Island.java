package dev.blockeed.replica.entities.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Location;

import java.util.UUID;

@Getter @AllArgsConstructor @Builder(builderClassName = "IslandBuilder")
public class Island {

    private final UUID uuid;
    private final Location spawnLocation;
    private final Location frameTop;
    private final Location frameBottom;
    private final Location buildingFrameTop;
    private final Location buildingFrameBottom;
    private final Location islandTop;
    private final Location islandBottom;

    public static class IslandBuilder {
        public Location getSpawnLocation() {
            return spawnLocation;
        }

        public UUID getUuid() {
            return uuid;
        }

        public Location getBuildingFrameBottom() {
            return buildingFrameBottom;
        }

        public Location getBuildingFrameTop() {
            return buildingFrameTop;
        }

        public Location getFrameBottom() {
            return frameBottom;
        }

        public Location getFrameTop() {
            return frameTop;
        }

        public Location getIslandBottom() {
            return islandBottom;
        }

        public Location getIslandTop() {
            return islandTop;
        }
    }

}
