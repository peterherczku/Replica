package dev.blockeed.replica.entities.hotbars;

import dev.blockeed.replica.enums.HotbarItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor @Getter
public class HotbarItem {

    private HotbarItemType itemType;
    private ItemStack itemStack;
    private int position;

}
