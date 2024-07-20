package org.rothe.john.working_hours.ui.table.paste;

import org.rothe.john.working_hours.model.Team;

class PasteCursor {
    private final PasteTableModel model = new PasteTableModel();
    private Team team;
    private final int baseRow;
    private final int baseColumn;

    private int currentRow;
    private int currentColumn;

    public PasteCursor(Team team, int baseRow, int baseColumn) {
        this.team = team;
        this.currentRow = this.baseRow = baseRow;
        this.currentColumn = this.baseColumn = baseColumn;
    }

    public void setNext(String value) {
        model.setTeam(team);
        team = model.setValue(value, currentRow, currentColumn++);
    }

    public void nextRow() {
        ++currentRow;
        currentColumn = baseColumn;
    }

    public Team team() {
        return team;
    }
}
