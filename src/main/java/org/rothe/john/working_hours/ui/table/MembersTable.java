package org.rothe.john.working_hours.ui.table;

import lombok.Getter;
import lombok.val;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.model.Zone;
import org.rothe.john.working_hours.ui.action.PasteAction;
import org.rothe.john.working_hours.ui.table.editors.LocalTimeEditor;
import org.rothe.john.working_hours.ui.table.editors.ZoneEditor;

import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.KeyEvent.VK_V;
import static java.util.Objects.nonNull;

@Getter
public class MembersTable extends JTable {
    private Team team;

    public MembersTable() {
        super(new MembersTableModel());
        updateTableConfiguration();
        addPasteAction();
        initializeEditors();
    }

    public void setTeam(Team team) {
        this.team = team;
        getModel().setTeam(team);

        if(nonNull(team)) {
            SwingUtilities.invokeLater(()-> ColumnSizing.adjust(MembersTable.this));
        }
        repaint();
    }

    @Override
    public MembersTableModel getModel() {
        return (MembersTableModel) super.getModel();
    }

    public int getLastSelectedRow() {
        if(getSelectedRowCount() == 0) {
            return -1;
        }
        int[] selected = getSelectedRows();
        return selected[selected.length - 1];
    }

    public List<Integer> getSelectedRowList() {
        return IntStream.of(getSelectedRows()).boxed().toList();
    }

    public Optional<Member> getSelectedMember() {
        val index = getSelectedRow();
        if (index >= 0 && index <= team.getMembers().size()) {
            return Optional.of(team.getMembers().get(index));
        }
        return Optional.empty();
    }

    private void updateTableConfiguration() {
        setCellSelectionEnabled(true);
        setRowHeight(26);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getTableHeader().setReorderingAllowed(false);
    }

    private void addPasteAction() {
        getInputMap().put(KeyStroke.getKeyStroke(VK_V, CTRL_DOWN_MASK), "paste");
        getActionMap().put("paste", new PasteAction(this));
    }

    private void initializeEditors() {
        setDefaultEditor(LocalTime.class, new LocalTimeEditor());
        setDefaultEditor(Zone.class, new ZoneEditor());
    }
}
