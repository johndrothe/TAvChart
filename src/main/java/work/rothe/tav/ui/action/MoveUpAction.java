package work.rothe.tav.ui.action;

import work.rothe.tav.model.Member;
import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.util.Images;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoveUpAction extends AbstractReorderAction {
    public MoveUpAction(MembersTable table) {
        super(table, "Move Up", Images.load("move-up.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveUp(table().getSelectedRows());
    }

    private void moveUp(int[] rows) {
        Arrays.sort(rows);
        if (rows.length == 0 || rows[0] == 0) {
            return;
        }

        moveUp(rows, new ArrayList<>(document().members()));
    }

    private void moveUp(int[] rows, List<Member> members) {
        for (int row : rows) {
            Member m = members.remove(row);
            members.add(row - 1, m);
        }
        fireDocumentChanged("Move Up", document().withMembers(members));
        selectRowsLater(adjust(rows, -1));
    }
}
