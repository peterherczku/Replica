package dev.blockeed.replica.entities.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Location;

import java.util.List;

@Getter @AllArgsConstructor @Builder(builderClassName = "MapBuilder")
public class Map {

    private String id;
    private String name;
    private List<Island> islands;
    private Location lobbyLocation;
    private Location spectatorLocation;

    public static class MapBuilder {

        public Location getLobbyLocation() {
            return lobbyLocation;
        }

        public Location getSpectatorLocation() {
            return spectatorLocation;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public List<Island> getIslands() {
            return islands;
        }
    }

}
