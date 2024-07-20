package org.rothe.john.working_hours.ui.table.paste;

import lombok.val;
import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.model.TimePair;
import org.rothe.john.working_hours.model.Zone;
import org.rothe.john.working_hours.ui.table.MembersTable;

import java.awt.Toolkit;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.awt.datatransfer.DataFlavor.stringFlavor;

public class Paster {

    // TODO: Handle or prevent pasting into non-rectangular selection sets.

    private final MembersTable table;

    private Paster(MembersTable table) {
        this.table = table;
    }

    public static void paste(MembersTable table) {
        new Paster(table).paste();
    }

    public static boolean canPaste(MembersTable table) {
        try {
            return table.getSelectedRowCount() > 0
                    && table.getSelectedColumnCount() > 0
                    && !getTextFromClipboard().isEmpty();
        } catch (IOException | UnsupportedFlavorException e) {
            return false;
        }
    }

    private void paste() {
        try {
            paste(CopiedContent.of(getTextFromClipboard(), table.getColumnCount()));
        } catch (IOException | UnsupportedFlavorException ex) {
            System.err.printf("Error: Failed to paste from clipboard: %s%n", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void paste(CopiedContent content) {
        if (content.isEmpty() || table.getSelectedRowCount() == 0) {
            return;
        }

        switch (ReplaceBehavior.of(selection(), content.shape())) {
            case ENTIRE_TABLE -> replaceEntireTable(content);
            case ROWS -> pasteRows(content);
            case VALUES -> pasteValues(content);
        }
    }

    private void pasteRows(CopiedContent data) {
        // Replace the selected rows with the rows in the data.
        fireTeamChanged(setFragmentValues(table.getTeam(), data, table.getSelectedRow(), 0));
        // TODO: Add/Remove as necessary.
    }

    private void pasteValues(CopiedContent data) {
        // Data > Selection: Start pasting at the first selected cell.
        // TODO: Data <= Selection: Paste one or more complete copies of the data into the selected cells
        fireTeamChanged(setFragmentValues(table.getTeam(), data,
                table.getSelectedRow(), table.getSelectedColumn()));
    }

    private Team setFragmentValues(Team team, CopiedContent data, int targetRow, int targetColumn) {
        PasteCursor cursor = new PasteCursor(team, targetRow, targetColumn);

        for (int row = 0; row < data.getRowCount(); ++row) {
            for (int column = 0; column < data.getColumnCount(); ++column) {
                cursor.setNext(data.getValueAt(row, column));
            }
            cursor.nextRow();
        }
        return cursor.team();
    }

    private void replaceEntireTable(CopiedContent data) {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < data.getRowCount(); ++i) {
            members.add(newMember(Zone.here()));
        }
        Team team = table.getTeam().withMembers(members);
        fireTeamChanged(setFragmentValues(team, data, 0, 0));
    }

    private Member newMember(Zone zone) {
        return new Member("", "", "", zone,
                TimePair.businessNormal(zone),
                TimePair.businessLunch(zone));
    }

    private void fireTeamChanged(Team newTeam) {
        Teams.fireTeamChanged(this, "Paste", newTeam);
    }

    private SelectionShape selection() {
        return SelectionShape.of(table);
    }

    private static String getTextFromClipboard() throws IOException, UnsupportedFlavorException {
        val clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (clipboard.isDataFlavorAvailable(stringFlavor)) {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(stringFlavor);
        }
        return "";
    }
}
