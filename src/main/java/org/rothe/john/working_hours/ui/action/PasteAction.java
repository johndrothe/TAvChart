package org.rothe.john.working_hours.ui.action;

import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.model.TimePair;
import org.rothe.john.working_hours.model.Zone;
import org.rothe.john.working_hours.ui.table.MembersTable;
import org.rothe.john.working_hours.ui.table.MembersTableModel;
import org.rothe.john.working_hours.ui.table.paste.DataShape;
import org.rothe.john.working_hours.ui.table.paste.PasteData;
import org.rothe.john.working_hours.ui.table.paste.Selection;
import org.rothe.john.working_hours.util.Images;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.awt.datatransfer.DataFlavor.stringFlavor;

public class PasteAction extends ToolbarAction {
    private final PasteTableModel model = new PasteTableModel();
    private final MembersTable table;

    public PasteAction(MembersTable table) {
        super("Paste", Images.load("paste.png"));
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (clipboard.isDataFlavorAvailable(stringFlavor)) {
            try {
                paste(clipboard);
            } catch (IOException | UnsupportedFlavorException ex) {
                //TODO: Undo the paste
                System.err.printf("Error: Failed to paste from clipboard: %s%n", ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void paste(Clipboard clipboard) throws IOException, UnsupportedFlavorException {
        PasteData data = PasteData.of(clipboard.getData(stringFlavor));
        if (data.isEmpty()) {
            return;
        }

        switch (Selection.of(table)) {
            case SINGLE_CELL -> pasteIntoSingleValue(data);
            case PARTIAL_ROW -> pasteIntoPartialRow(data);
            case COMPLETE_ROW -> pasteIntoCompleteRow(data);
            case FRAGMENT -> pasteIntoFragment(data);
            case ENTIRE_TABLE -> replaceEntireTable(data);
        }
    }

    private void pasteIntoFragment(PasteData data) {

    }

    private void pasteIntoCompleteRow(PasteData data) {

    }

    private void pasteIntoPartialRow(PasteData data) {
        switch (DataShape.of(data, table.getColumnCount())) {
            case SINGLE_VALUE -> setSingleValue(data, table.getSelectedRow(), table.getSelectedColumn());
            case PARTIAL_ROW, FRAGMENT -> setFragmentValues(data, getMinSelectedRow(), getMinSelectedCol(), getMaxSelectedRow(), getMaxSelectedCol());
            case COMPLETE_ROW -> {
            }
            case MULTIPLE_ROWS -> {
            }
        }

    }

    private int getMinSelectedRow() {
        return IntStream.of(table.getSelectedRows()).min().orElse(0);
    }

    private int getMaxSelectedRow() {
        return IntStream.of(table.getSelectedRows()).max().orElse(0);
    }

    private int getMinSelectedCol() {
        return IntStream.of(table.getSelectedColumns()).min().orElse(0);
    }

    private int getMaxSelectedCol() {
        return IntStream.of(table.getSelectedColumns()).max().orElse(0);
    }

    private void pasteIntoSingleValue(PasteData data) {
        switch (DataShape.of(data, table.getColumnCount())) {
            case SINGLE_VALUE -> setSingleValue(data, table.getSelectedRow(), table.getSelectedColumn());
            case PARTIAL_ROW, FRAGMENT -> setFragmentValues(data, table.getSelectedRow(), table.getSelectedColumn());
            case COMPLETE_ROW -> {
            }
            case MULTIPLE_ROWS -> {
            }
        }
    }

    private void setSingleValue(PasteData data, int targetRow, int targetColumn) {
        model.setTeam(table.getTeam());
        fireTeamChanged(model.setValue(data.getValueAt(0, 0), targetRow, targetColumn));
    }

    // situations where you assume you have at least enough pasted data to fill the selection
    private void setFragmentValues(PasteData data, int targetRow, int targetColumn) {
        setFragmentValues(data, targetRow, targetColumn, table.getRowCount(), table.getColumnCount());
    }

    // situations where you assume you have at least enough pasted data to fill the selection
    private void setFragmentValues(PasteData data, int targetRow, int targetColumn, int maxRow, int maxColumn) {
        Team team = table.getTeam();

        for (int row = 0; row < data.getRowCount() && targetRow + row < maxRow; ++row) {
            for (int col = 0; col < data.getColumnCount() && targetColumn + col < maxColumn; ++col) {
                model.setTeam(team);
                team = model.setValue(data.getValueAt(row, col), targetRow + row, targetColumn + col);
            }
        }
        fireTeamChanged(team);
    }

    private void replaceEntireTable(PasteData data) {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < data.getRowCount(); ++i) {
            members.add(newMember(Zone.here()));
        }
        Team team = table.getTeam().withMembers(members);

        for (int row = 0; row < data.getRowCount(); ++row) {
            for (int col = 0; col < data.getColumnCount(); ++col) {
                model.setTeam(team);
                team = model.setValue(data.getValueAt(row, col), row, col);
            }
        }
        fireTeamChanged(team);
    }

    private Member newMember(Zone zone) {
        return new Member("", "", "", zone,
                TimePair.businessNormal(zone),
                TimePair.businessLunch(zone));
    }

    private void fireTeamChanged(Team newTeam) {
        Teams.fireTeamChanged(this, "Paste", newTeam);
    }

    private static class PasteTableModel extends MembersTableModel {
        @Override
        public Team setValue(Object aValue, int rowIndex, int columnIndex) {
            return super.setValue(aValue, rowIndex, columnIndex);
        }
    }
}
