package org.rothe.john.swc.ui.table.paste.operations;

import org.rothe.john.swc.model.Team;
import org.rothe.john.swc.ui.table.MembersTableModel;

class TableValueWriter {
    private final PasteTableModel model = new PasteTableModel();
    private Team team;
    private final int baseRow;
    private final int baseColumn;

    private int currentRow;
    private int currentColumn;

    public TableValueWriter(Team team, int baseRow, int baseColumn) {
        this.team = team;
        this.currentRow = this.baseRow = baseRow;
        this.currentColumn = this.baseColumn = baseColumn;
    }

    public void setNext(String value) {
        model.setTeam(team);
        try {
            team = model.setValue(value, currentRow, currentColumn++);
        } catch (Exception e) {
            // Quietly allow (but ignore) inappropriate values
            e.printStackTrace();
        }
    }

    public void nextRow() {
        ++currentRow;
        currentColumn = baseColumn;
    }

    public Team team() {
        return team;
    }

    private static class PasteTableModel extends MembersTableModel {
        @Override
        public Team setValue(Object aValue, int rowIndex, int columnIndex) {
            return super.setValue(aValue, rowIndex, columnIndex);
        }
    }
}
