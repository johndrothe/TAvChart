package org.rothe.john.swc.ui.table;

import lombok.Getter;
import lombok.val;
import org.rothe.john.swc.event.DocumentChangedEvent;
import org.rothe.john.swc.event.DocumentListener;
import org.rothe.john.swc.event.Documents;
import org.rothe.john.swc.model.Document;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

import static javax.swing.BorderFactory.createEmptyBorder;

public class MembersTablePanel extends JPanel implements DocumentListener {
    @Getter
    private final MembersTable table = new MembersTable();

    private Document document;

    public MembersTablePanel() {
        super(new BorderLayout());
        setBorder(createEmptyBorder(10, 10, 10, 10));
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
