package dev.blockeed.replica.entities.game.states;

import dev.blockeed.replica.entities.game.GameState;
import dev.blockeed.replica.entities.map.Island;
import dev.blockeed.replica.enums.HotbarType;
import dev.blockeed.replica.enums.Titles;
import dev.blockeed.replica.managers.GameManager;
import dev.blockeed.replica.managers.HotbarManager;
import dev.blockeed.replica.managers.PlayerManager;
import dev.blockeed.replica.managers.TitleManager;
import dev.blockeed.replica.utils.Settings;
import dev.blockeed.replica.utils.TeleportUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class LobbyState extends GameState {
    public LobbyState() {
        super(Settings.LOBBY_TIME);
    }

    @Override
    protected void onEnd() {
        GameManager.setState(new WaitingState());
        GameManager.getPlayerIslands().forEach(((player, island) -> player.teleport(island.getSpawnLocation())));
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

        PlayerManager.resetPlayer(player);
    }

    @EventHandler
    public void onPlayerSpawnEvent(PlayerSpawnLocationEvent event) {
        TeleportUtil.teleportOnJoin(event.getPlayer(), GameManager.getMap().getLobbyLocation());
    }

    @Override
    public void run() {
        if (time == 0) {
            onEnd();
            this.cancel();
            return;
        }

        if (time <= 3) {
            GameManager.getOnlinePlayers().forEach(player -> {
                TitleManager.sendTitle(Titles.ARENA_STARTING).setValue("time", time+"").send(player);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            });
        }
        if (time > 0 && GameManager.getOnlineAlivePlayers().size() >= Settings.MIN_PLAYERS) time--;
        else time = Settings.LOBBY_TIME;
    }
}
