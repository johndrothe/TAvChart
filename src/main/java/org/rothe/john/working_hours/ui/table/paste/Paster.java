package org.rothe.john.working_hours.ui.table.paste;

import lombok.val;
import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.model.TimePair;
import org.rothe.john.working_hours.model.Zone;
import org.rothe.john.working_hours.ui.table.MembersTable;
import org.rothe.john.working_hours.ui.table.util.MemberRemover;
import org.rothe.john.working_hours.util.Pair;

import java.awt.Toolkit;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.awt.datatransfer.DataFlavor.stringFlavor;
import static java.util.stream.Stream.concat;

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
                    && isContiguousSelection(table)
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

    private void pasteRows(CopiedContent content) {
        System.err.println("Pasting " + content.getRowCount() + " rows into " + table.getSelectedRowCount() + " rows");

        // Replace the selected rows with the rows in the data.
        Team team = addOrRemoveRows(table.getTeam(), content);
        // Paste in the values in the copied content.
        team = applyValues(team, content, table.getSelectedRow(), 0);
        fireTeamChanged(team);
    }

    private void pasteValues(CopiedContent data) {
        // Data > Selection: Start pasting at the first selected cell.
        // TODO: Data <= Selection: Paste one or more complete copies of the data into the selected cells
        fireTeamChanged(applyValues(table.getTeam(), data,
                table.getSelectedRow(), table.getSelectedColumn()));
    }

    private Team addOrRemoveRows(Team team, CopiedContent content) {
        return addRequiredRows(removeExcessRows(team, content), content);
    }

    private Team addRequiredRows(Team team, CopiedContent content) {
        return team.withMembers(addRequiredMembers(team, content).toList());
    }

    private Stream<Member> addRequiredMembers(Team team, CopiedContent content) {
        return concat(concat(getMembersBeforeNew(team), newMembers(content)), getMembersAfterNew(team));
    }

    private Stream<Member> getMembersBeforeNew(Team team) {
        return team.getMembers().stream().limit(table.getLastSelectedRow());
    }

    private Stream<Member> getMembersAfterNew(Team team) {
        return team.getMembers().stream().skip(table.getLastSelectedRow());
    }

    private Stream<Member> newMembers(CopiedContent content) {
        return IntStream.range(0, (content.getRowCount() - table.getSelectedRowCount()))
                .peek(i -> System.err.println("New member number " + i))
                .mapToObj(i -> newMember())
                .peek(m -> System.err.println("New member: " + m));
    }

    private Team removeExcessRows(Team team, CopiedContent data) {
        return MemberRemover.remove(team, getRowsToRemove(data));
    }

    private int[] getRowsToRemove(CopiedContent data) {
        return IntStream.of(table.getSelectedRows())
                .sorted()
                .skip(data.getRowCount())
                .toArray();
    }

    private Team applyValues(Team team, CopiedContent content, int startRow, int startColumn) {
        PasteCursor cursor = new PasteCursor(team, startRow, startColumn);

        for (int row = 0; row < content.getRowCount(); ++row) {
            for (int column = 0; column < content.getColumnCount(); ++column) {
                cursor.setNext(content.getValueAt(row, column));
            }
            cursor.nextRow();
        }
        return cursor.team();
    }

    private void replaceEntireTable(CopiedContent content) {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < content.getRowCount(); ++i) {
            members.add(newMember());
        }
        Team team = table.getTeam().withMembers(members);
        fireTeamChanged(applyValues(team, content, 0, 0));
    }

    private Member newMember() {
        return new Member("", "", "", Zone.here(),
                TimePair.businessNormal(Zone.here()),
                TimePair.businessLunch(Zone.here()));
    }

    private void fireTeamChanged(Team newTeam) {
        Teams.fireTeamChanged(this, "Paste", newTeam);
    }

    private SelectionShape selection() {
        return SelectionShape.of(table);
    }

    private static boolean isContiguousSelection(MembersTable table) {
        return Pair.stream(table.getSelectedRowList())
                .noneMatch(p -> Math.abs(p.left() - p.right()) != 1);
    }

    private static String getTextFromClipboard()
            throws IOException, UnsupportedFlavorException {
        val clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (clipboard.isDataFlavorAvailable(stringFlavor)) {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(stringFlavor);
        }
        return "";
    }
}
