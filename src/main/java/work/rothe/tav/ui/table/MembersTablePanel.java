package work.rothe.tav.ui.table;

import lombok.Getter;
import lombok.val;
import work.rothe.tav.event.DocumentChangedEvent;
import work.rothe.tav.event.DocumentListener;
import work.rothe.tav.event.Documents;
import work.rothe.tav.model.Document;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

public class MembersTablePanel extends JPanel implements DocumentListener {
    @Getter
    private final MembersTable table = new MembersTable();

    private Document document;

    public MembersTablePanel() {
        super(new BorderLayout());
        setOpaque(false);

        val centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(true);
        add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void register() {
        Documents.addDocumentListener(this);
    }

    public void unregister() {
        Documents.removeDocumentListener(this);
    }

    public void documentChanged(DocumentChangedEvent event) {
        this.document = event.document();
        table.setDocument(document);
    }

    private void resizeTable() {

    }
}
