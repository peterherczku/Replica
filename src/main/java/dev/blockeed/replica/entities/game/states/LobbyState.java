package dev.blockeed.replica.entities.game.states;

import dev.blockeed.replica.entities.game.GameState;
import dev.blockeed.replica.entities.map.Island;
import dev.blockeed.replica.enums.HotbarType;
import dev.blockeed.replica.managers.GameManager;
import dev.blockeed.replica.managers.HotbarManager;
import dev.blockeed.replica.managers.PlayerManager;
import dev.blockeed.replica.utils.TeleportUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyState extends GameState {
    public LobbyState() {
        super(15);
    }

    @Override
    protected void onEnd() {
        GameManager.setState(new WaitingState());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        Island island = GameManager.getFreeIsland();
        if (island == null) {
            PlayerManager.setSpectator(player);
            return;
        }

        GameManager.addPlayer(player, island);
        TeleportUtil.teleportOnJoin(player, island.getSpawnLocation());
        //HotbarManager.getHotbar(HotbarType.LOBBY).setHotbar(player);
    }

}
