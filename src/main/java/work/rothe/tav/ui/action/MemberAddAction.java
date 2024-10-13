package work.rothe.tav.ui.action;

import lombok.val;
import work.rothe.tav.event.Documents;
import work.rothe.tav.model.Document;
import work.rothe.tav.model.Member;
import work.rothe.tav.model.TimePair;
import work.rothe.tav.model.Zone;
import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.util.Images;

import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class MemberAddAction extends ToolbarAction {
    private final MembersTable table;

    public MemberAddAction(MembersTable table) {
        super("Add Member", Images.load("plus-square"));
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(nonNull(table.getCellEditor())) {
            table.getCellEditor().cancelCellEditing();
        }
        addMember(Documents.getCurrent());
    }

    private void addMember(Document document) {
        fireDocumentChanged(document.withMembers(newMembers(document.members())));
        SwingUtilities.invokeLater(() -> editNewMember(document.members().size()));
    }

    private List<Member> newMembers(List<Member> members) {
        return Stream.concat(members.stream(), Stream.of(newMember())).toList();
    }

    private Member newMember() {
        val selected = table.getSelectedMember();
        return newMember(
                selected.map(Member::role).orElse("Role"),
                selected.map(Member::location).orElse("Location"),
                selected.map(Member::zone).orElse(Zone.utc()));
    }

    private Member newMember(String role, String location, Zone zone) {
        return new Member("New Member", role, location, zone,
                TimePair.businessNormal(zone),
                TimePair.businessLunch(zone));
    }

    private void editNewMember(int index) {
        table.clearSelection();
        table.addRowSelectionInterval(index, index);
        table.addColumnSelectionInterval(0, 0);
        table.editCellAt(index, 0);

        editNewMemberName(table.getEditorComponent());
    }

    private void editNewMemberName(Component component) {
        if(isNull(component)) {
            return;
        }
        component.requestFocusInWindow();
        if(component instanceof JTextComponent t) {
            t.selectAll();
        }
    }

    private void fireDocumentChanged(Document newDocument) {
        Documents.fireDocumentChanged(this, String.valueOf(getValue(NAME)), newDocument);
    }
}
