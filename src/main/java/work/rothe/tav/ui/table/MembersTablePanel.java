package work.rothe.tav.ui.table;

import lombok.Getter;
import lombok.val;
import work.rothe.tav.event.DocumentChangedEvent;
import work.rothe.tav.event.DocumentListener;
import work.rothe.tav.event.Documents;
import work.rothe.tav.model.Document;
import work.rothe.tav.ui.details.MembersTableToolbar;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import java.awt.BorderLayout;

import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createEtchedBorder;

public class MembersTablePanel extends JPanel implements DocumentListener {
    @Getter
    private final MembersTable table = new MembersTable();
    private final MembersTableToolbar toolbar = new MembersTableToolbar(table);

    public MembersTablePanel() {
        super(new BorderLayout());
        setBorder(border());

        val centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(true);
        add(toolbar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        centerPanel.setBorder(createEmptyBorder(10, 10, 10, 10));
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
    }
    public void register() {
        Documents.addDocumentListener(this);
    }

    public void unregister() {
        Documents.removeDocumentListener(this);
    }

    public void documentChanged(DocumentChangedEvent event) {
        table.setDocument(event.document());
        toolbar.documentChanged(event);
    }

    private static Border border() {
        return createCompoundBorder(createEtchedBorder(), createEmptyBorder(2, 0, 0, 0));
    }
}
