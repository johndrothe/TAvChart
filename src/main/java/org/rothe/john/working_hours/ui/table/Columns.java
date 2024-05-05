package org.rothe.john.working_hours.ui.table;

public enum Columns {

    NAME("Name"),
    ROLE("Role"),
    LOCATION("Location"),
    HOURS_START("Hours Start"),
    HOURS_END("Hours End"),
    LUNCH_START("Lunch Start"),
    LUNCH_END("Lunch End"),
    ZONE("Time Zone");

    private final String description;

    Columns(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getColumnIndex() {
        return this.ordinal();
    }

    public static Columns getColumn(int index) {
        return Columns.values()[index];
    }

    public static int getCount() {
        return Columns.values().length;
    }
}
