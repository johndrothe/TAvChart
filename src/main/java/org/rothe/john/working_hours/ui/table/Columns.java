package org.rothe.john.working_hours.ui.table;

import lombok.Getter;

@Getter
public enum Columns {

    NAME("Name"),
    ROLE("Role"),
    LOCATION("Location"),
    START_TIME("Start Start"),
    END_TIME("End Time"),
    LUNCH_START("Lunch Start"),
    LUNCH_END("Lunch End"),
    ZONE("Time Zone");

    private final String description;

    Columns(String description) {
        this.description = description;
    }

    public static Columns getColumn(int index) {
        return Columns.values()[index];
    }

    public static int getCount() {
        return Columns.values().length;
    }
}
