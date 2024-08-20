package dev.blockeed.replica.entities.images;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;
import java.util.UUID;

@Getter @AllArgsConstructor
public class Image {

    private UUID uuid;
    private List<Material> blocks;
    private int size;

}
