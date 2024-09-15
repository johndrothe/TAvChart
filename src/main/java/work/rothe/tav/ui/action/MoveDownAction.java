package work.rothe.tav.ui.action;

import work.rothe.tav.model.Member;
import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.util.Images;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoveDownAction extends AbstractReorderAction {
    public MoveDownAction(MembersTable table) {
        super(table, "Move Up", Images.load("move-down.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveDown(table().getSelectedRows());
    }

    public void moveDown(int[] rows) {
        Arrays.sort(rows);
        if (rows.length == 0 || rows[rows.length - 1] == table().getRowCount() - 1) {
            return;
        }
        moveDown(rows, new ArrayList<>(document().members()));
    }

    private void moveDown(int[] rows, List<Member> members) {
        for (int i = rows.length-1; i >= 0; --i) {
            int row = rows[i];
            Member m = members.remove(row);
            members.add(row + 1, m);
        }
        fireDocumentChanged("Move Down", document().withMembers(members));
        selectRowsLater(adjust(rows, 1));
    }
}
