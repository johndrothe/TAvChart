package work.rothe.tav.ui.table;

import lombok.Getter;

@Getter
public enum Columns {

    NAME("Name"),
    ROLE("Role"),
    LOCATION("Location"),
    START_TIME("Normal Start"),
    END_TIME("Normal End"),
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
