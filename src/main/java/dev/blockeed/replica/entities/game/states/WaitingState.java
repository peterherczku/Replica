package dev.blockeed.replica.entities.game.states;

import dev.blockeed.replica.entities.game.GameState;
import dev.blockeed.replica.entities.images.Image;
import dev.blockeed.replica.enums.Titles;
import dev.blockeed.replica.managers.GameManager;
import dev.blockeed.replica.managers.ImageManager;
import dev.blockeed.replica.managers.ScoreboardManager;
import dev.blockeed.replica.managers.TitleManager;
import dev.blockeed.replica.utils.FrameUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class WaitingState extends GameState {
    public WaitingState() {
        super(10);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        GameManager.getIslands().forEach(island -> {
            FrameUtil.getLocationsInCuboid(island.getFrameTop(), island.getFrameBottom()).forEach(location -> location.getBlock().setType(Material.AIR));
            FrameUtil.getLocationsInCuboid(island.getBuildingFrameTop(), island.getBuildingFrameBottom()).forEach(location -> location.getBlock().setType(Material.AIR));
        });
    }

    @Override
    protected void onEnd() {
        Image image = ImageManager.getRandomImage(GameManager.getPreviousImages().stream().map(Image::getUuid).toList());
        GameManager.addPreviousImage(image);
        GameManager.setState(new BuildingState(image, GameManager.getBuildingStage()));
        GameManager.setBuildingStage(GameManager.getBuildingStage() + 1);
    }

    @Override
    public void run() {
        super.run();
        if (getTime() < 5) {
            GameManager.getOnlinePlayers().forEach(player -> {
                TitleManager.sendTitle(Titles.ROLLING_NEXT_IMAGE).setValue("time", getTime() + "").send(player);
            });
        }
    }
}
