package org.rothe.john.swc.ui.table.paste.operations;

import org.rothe.john.swc.event.Teams;
import org.rothe.john.swc.model.Team;
import org.rothe.john.swc.ui.table.MembersTable;
import org.rothe.john.swc.ui.table.paste.CopiedContent;

public class ValueOperation extends AbstractPasteOperation {
    private ValueOperation(CopiedContent content, MembersTable table) {
        super(content, table);
    }

    public static void paste(CopiedContent content, MembersTable table) {
        new ValueOperation(content, table).paste();
    }

    private void paste() {
        if(isSingleCellSelected() || isDataLargerThanSelection()) {
            // Data > Selection: Start pasting at the first selected cell.
            Teams.fireTeamChanged(this, "Paste", pasteOneCopy());
        } else {
            // Data <= Selection: Paste one or more complete copies of the data into the selected cells
            Teams.fireTeamChanged(this, "Paste", pasteMultipleCopies());
        }
    }

    private Team pasteMultipleCopies() {
        Team team = table.getTeam();
        for(int vertical = 0; vertical < getVerticalCopies(); ++vertical) {
            for(int horizontal = 0; horizontal < getHorizontalCopies(); ++horizontal) {

                int targetRow = table.getSelectedRow() + (horizontal * content.getRowCount());
                int targetColumn = table.getSelectedColumn() + (vertical * content.getColumnCount());
                team = applyValues(team, content, targetRow, targetColumn);
            }
        }
        return team;
    }

    private Team pasteOneCopy() {
        System.err.println("ValueOperation.pasteOneCopy");
        return applyValues(table.getTeam(), content, table.getSelectedRow(), table.getSelectedColumn());
    }

    private boolean isSingleCellSelected() {
        return table.getSelectedRowCount() == 1 && table.getSelectedColumnCount() == 1;
    }

    private boolean isDataLargerThanSelection() {
        return content.getRowCount() >= table.getSelectedRowCount()
                && content.getColumnCount() >= table.getSelectedColumnCount();
    }

    private int getHorizontalCopies() {
        return table.getSelectedRowCount() / content.getRowCount();
    }

    private int getVerticalCopies() {
        return table.getSelectedColumnCount() / content.getColumnCount();
    }
}
