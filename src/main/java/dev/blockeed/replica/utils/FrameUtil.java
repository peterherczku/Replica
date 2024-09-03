package dev.blockeed.replica.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class FrameUtil {

    public static boolean isOneBlockWide(Location frameTop, Location frameBottom) {
        return ((frameTop.getBlockX() == frameBottom.getBlockX()) || (frameTop.getBlockZ() == frameBottom.getBlockZ()) || (frameTop.getBlockY() == frameBottom.getBlockY()));
    }

    public static int getSize(Location frameTop, Location frameBottom) {
        if (frameTop.getBlock().getX() == frameBottom.getBlock().getX()) {
            return Math.abs(frameTop.getBlockZ() - frameBottom.getBlockZ()) + 1;
        } else {
            return Math.abs(frameTop.getBlockX() - frameBottom.getBlockX()) + 1;
        }
    }

    public static List<Location> getLocationsInCuboid(Location frameTop, Location frameBottom) {
        int size = getSize(frameTop, frameBottom);
        String direction;
        if (frameTop.getBlockY() == frameBottom.getBlockY()) direction = "horizontal";
        else direction = "vertical";

        List<Location> locations = new ArrayList<>();
        int row = -1;
        int xSign = frameTop.getBlockX() > frameBottom.getBlockX() ? -1 : 1;
        int zSign = frameTop.getBlockZ() > frameBottom.getBlockZ() ? -1 : 1;
        for (int i = 0; i < size * size; i++) {
            if (i % size == 0) {
                row++;
            }
            if (direction.equals("horizontal")) {
                if (xSign != zSign) {
                    locations.add(new Location(frameTop.getWorld(), frameTop.getBlockX() + (xSign * row), frameTop.getBlockY(), frameTop.getBlockZ() + (zSign * (i % size))));
                } else {
                    locations.add(new Location(frameTop.getWorld(), frameTop.getBlockX() + (xSign * (i % size)), frameTop.getBlockY(), frameTop.getBlockZ() + (zSign * row)));
                }

            } else {
                if (frameTop.getBlockZ() == frameBottom.getBlockZ()) {
                    locations.add(new Location(frameTop.getWorld(), frameTop.getBlockX() + (xSign * (i%size)), frameTop.getBlockY() - row, frameTop.getBlockZ()));
                } else {
                    locations.add(new Location(frameTop.getWorld(), frameTop.getBlockX(), frameTop.getBlockY() - row, frameTop.getBlockZ() + (zSign * (i%size))));
                }
            }
        }
        return locations;
    }

    public static List<Material> getBlocksInCuboid(Location frameTop, Location frameBottom) {
        List<Material> blocks = new ArrayList<>();
        for (Location location : getLocationsInCuboid(frameTop, frameBottom)) {
            blocks.add(location.getBlock().getType());
        }
        return blocks;
    }

    public static boolean isInCube(Location location, Location frameTop, Location frameBottom) {
        double xmin = Math.min(frameTop.getBlockX(), frameBottom.getBlockX());
        double xmax = Math.max(frameTop.getBlockX(), frameBottom.getBlockX());
        double ymin = Math.min(frameTop.getBlockY(), frameBottom.getBlockY());
        double ymax = Math.max(frameTop.getBlockY(), frameBottom.getBlockY());
        double zmin = Math.min(frameTop.getBlockZ(), frameBottom.getBlockZ());
        double zmax = Math.max(frameTop.getBlockZ(), frameBottom.getBlockZ());

        return (xmin <= location.getBlockX() && location.getBlockX() <= xmax) &&
                (ymin <= location.getBlockY() && location.getBlockY() <= ymax) &&
                (zmin <= location.getBlockZ() && location.getBlockZ() <= zmax);
    }

    public static boolean isSquare(Location frameTop, Location frameBottom) {
        if (frameTop.getBlockY() == frameBottom.getBlockY()) {
            return Math.abs(frameTop.getBlockX() - frameBottom.getBlockX()) == Math.abs(frameTop.getBlockZ() - frameBottom.getBlockZ());
        }
        if (frameTop.getBlockZ() == frameBottom.getBlockZ()) {
            return Math.abs(frameTop.getBlockX() - frameBottom.getBlockX()) == Math.abs(frameTop.getBlockY() - frameBottom.getBlockY());
        }
        if (frameTop.getBlockX() == frameBottom.getBlockX()) {
            return Math.abs(frameTop.getBlockY() - frameBottom.getBlockY()) == Math.abs(frameTop.getBlockZ() - frameBottom.getBlockZ());
        }
        return false;
    }

}
