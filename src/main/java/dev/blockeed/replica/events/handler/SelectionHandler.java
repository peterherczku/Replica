package dev.blockeed.replica.events.handler;

import dev.blockeed.replica.enums.Messages;
import dev.blockeed.replica.managers.ImageManager;
import dev.blockeed.replica.managers.MessageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SelectionHandler implements Listener {

    @EventHandler
    public void onBlockBreak(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        System.out.println("a1");
        if (!player.hasPermission("replica.admin")) return;
        System.out.println("a2");
        if (player.getItemInHand() == null) return;
        System.out.println("a3");
        if (player.getItemInHand().getType() != Material.DIAMOND_AXE) return;
        System.out.println("a4");
        if (player.getTargetBlock(5) == null) return;
        System.out.println("a5");
        if (event.getAction().isLeftClick()) {
            event.setCancelled(true);
            ImageManager.setTopSelection(player, player.getTargetBlock(5).getLocation());
            MessageManager.sendMessage(Messages.SUCCESSFUL_TOP_SELECTION).send(player);
        }
        if (event.getAction().isRightClick()) {
            event.setCancelled(true);
            ImageManager.setBottomSelection(player, player.getTargetBlock(5).getLocation());
            MessageManager.sendMessage(Messages.SUCCESSFUL_BOTTOM_SELECTION).send(player);
        }
    }

}
