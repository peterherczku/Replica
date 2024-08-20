package dev.blockeed.replica.entities.game.states;

import dev.blockeed.replica.entities.game.GameState;
import dev.blockeed.replica.enums.Messages;
import dev.blockeed.replica.enums.Titles;
import dev.blockeed.replica.managers.GameManager;
import dev.blockeed.replica.managers.MessageManager;
import dev.blockeed.replica.managers.PlayerManager;
import dev.blockeed.replica.managers.TitleManager;
import org.bukkit.entity.Player;

public class EndingState extends GameState {

    private Player winner;

    public EndingState(Player winner) {
        super(30);
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

    }
}
