package dev.blockeed.replica.entities.hotbars;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class Hotbar {

    private List<HotbarItem> items;

    public Hotbar(List<HotbarItem> items) {
        this.items = items;
    }

    public void setHotbar(Player player) {
        player.getInventory().clear();
        items.forEach(item -> {
            player.getInventory().setItem(item.getPosition() - 1, item.getItemStack());
        });
    }


}
