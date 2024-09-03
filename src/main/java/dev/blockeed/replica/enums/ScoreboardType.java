package dev.blockeed.replica.enums;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public enum ScoreboardType {

    LOBBY,
    WAITING,
    INGAME;

    @Setter
    private String title;
    @Setter
    private List<String> lines;

}
