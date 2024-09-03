package dev.blockeed.replica.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public enum Messages {

    PREFIX,
    HELP,
    HAVENT_SELECTED_AN_EDGE,
    YOUR_SELECTION_IS_NOT_IN_ONE_PLANE,
    SUCCESSFULLY_SAVED_IMAGE,
    SPECTATOR_MODE,
    YOUR_SELECTION_IS_NOT_A_SQUARE,
    PLAYER_COMPLETED_THE_IMAGE,
    YOU_HAVE_BEEN_ELIMINATED,
    PLAYER_HAS_BEEN_ELIMINATED,
    PLAYER_WON_THE_ARENA,

    YOU_HAVE_ALREADY_STARTED_CREATING_AN_ARENA,
    ARENA_ID_CANNOT_CONTAIN_SPACE,
    SUCCESSFULLY_STARTED_CREATING_ARENA,
    YOU_HAVENT_STARTED_CREATING_AN_ARENA,
    SUCCESSFULLY_SET_LOBBY,
    SUCCESSFULY_SET_SPECTATOR,
    SUCCESSFULLY_SET_MAP_NAME,
    YOU_HAVE_ALREADY_STARTED_CREATING_AN_ISLAND,
    SUCCESSFULLY_CREATED_ISLAND_AND_SET_SPAWN,
    YOU_HAVENT_STARTED_CREATING_AN_ISLAND,
    SUCCESSFULY_SET_FRAME,
    SUCCESSFULY_SET_BUILDING_FRAME,
    SUCCESSFULY_SET_ISLAND_BOUNDARIES,
    YOU_HAVENT_SET_UP_THE_ISLAND_YET,
    SUCCESSFULLY_SAVED_ISLAND,
    FIRST_FINISH_SETTING_UP_THE_ISLAND,
    YOU_HAVE_TO_ADD_AT_LEAST_TWO_ISLANDS,
    YOU_HAVENT_SET_UP_THE_ARENA_YET,
    SUCCESSFULLY_SAVED_ARENA,

    SUCCESSFUL_TOP_SELECTION,
    SUCCESSFUL_BOTTOM_SELECTION,

    ROUND_HAS_ENDED,
    ROUND_HAS_STARTED,

    ;

    @Setter
    private String text;
    @Setter
    private List<String> lines;
    @Setter
    private MessageType type;

    public MessageType getType() {
        if (type == null) return MessageType.SINGLE_LINE;
        return type;
    }

    public String getText() {
        if (text == null) return this.name();
        return text;
    }

    public List<String> getLines() {
        if (lines == null) return Collections.singletonList(this.name());
        if (lines.isEmpty()) return Collections.singletonList(this.name());
        return lines;
    }

}
