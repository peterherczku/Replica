package dev.blockeed.replica.enums;

import lombok.Setter;

public enum Titles {

    WAITING_COUNTDOWN,
    SUCCESSFULLY_COMPLETED_THE_IMAGE,

    ROUND_HAS_ENDED,
    ROLLING_NEXT_IMAGE,
    ROUND_HAS_STARTED,

    PLAYER_WON_THE_ARENA;

    @Setter
    private String title;
    @Setter
    private String subtitle;

    public String getTitle() {
        if (title == null) return this.name();
        return title;
    }

    public String getSubtitle() {
        if (subtitle == null) return "";
        return subtitle;
    }

}
