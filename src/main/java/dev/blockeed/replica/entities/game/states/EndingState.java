package dev.blockeed.replica.entities.game.states;

import dev.blockeed.replica.entities.game.GameState;
import dev.blockeed.replica.enums.Messages;
import dev.blockeed.replica.enums.Titles;
import dev.blockeed.replica.managers.GameManager;
import dev.blockeed.replica.managers.MessageManager;
import dev.blockeed.replica.managers.PlayerManager;
import dev.blockeed.replica.managers.TitleManager;
import dev.blockeed.replica.utils.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EndingState extends GameState {

    private Player winner;

    public EndingState(Player winner) {
        super(Settings.ENDING_TIME);
        this.winner = winner;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        GameManager.getOnlinePlayers().forEach(player -> {
            MessageManager.sendMessage(Messages.PLAYER_WON_THE_ARENA).setValue("player", winner.getName()).send(player);
            TitleManager.sendTitle(Titles.PLAYER_WON_THE_ARENA).setValue("player", winner.getName()).send(player);
            if (!PlayerManager.isSpectator(player)) PlayerManager.setSpectator(player);
        });
    }

    @Override
    protected void onEnd() {
        GameManager.restart();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

}
