package dev.blockeed.replica.events.handler;

import dev.blockeed.replica.entities.hotbars.HotbarItem;
import dev.blockeed.replica.enums.Messages;
import dev.blockeed.replica.managers.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class HotbarHandler implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (GameManager.getCurrentState() == null) return;
        if (event.getItem() == null) return;
        if (event.getItem().getItemMeta() == null) return;
        Player player = event.getPlayer();
        String displayName = event.getItem().getItemMeta().getDisplayName();
        List<HotbarItem> items = new ArrayList<>();
        HotbarManager.getHotbars().forEach(hotbar -> {
            items.addAll(hotbar.getItems());
        });

        HotbarItem hotbarItem = items.stream().filter(item -> item.getItemStack().getItemMeta().getDisplayName().equals(displayName)).findAny().orElse(null);
        if (hotbarItem == null) return;

        switch (hotbarItem.getItemType()) {
            case LEAVE:
                //BungeeNetworkManager.sendServer(player, SettingsManager.LOBBY_SERVER_NAME);
                break;

        }

    }

}
