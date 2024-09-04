package dev.blockeed.replica.events.handler;

import dev.blockeed.replica.enums.Messages;
import dev.blockeed.replica.managers.ImageManager;
import dev.blockeed.replica.managers.MessageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class SelectionHandler implements Listener {

    @EventHandler
    public void onBlockBreak(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("replica.admin")) return;
        if (player.getItemInHand() == null) return;
        if (player.getItemInHand().getType() != Material.DIAMOND_AXE) return;
        if (event.getClickedBlock() == null) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction().isLeftClick()) {
            event.setCancelled(true);
            ImageManager.setTopSelection(player, event.getClickedBlock().getLocation());
            MessageManager.sendMessage(Messages.SUCCESSFUL_TOP_SELECTION).send(player);
        }
        if (event.getAction().isRightClick()) {
            event.setCancelled(true);
            ImageManager.setBottomSelection(player, event.getClickedBlock().getLocation());
            MessageManager.sendMessage(Messages.SUCCESSFUL_BOTTOM_SELECTION).send(player);
        }
    }

}
