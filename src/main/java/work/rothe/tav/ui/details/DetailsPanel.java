package work.rothe.tav.ui.details;

import work.rothe.tav.event.DocumentChangedEvent;
import work.rothe.tav.event.DocumentListener;
import work.rothe.tav.event.Documents;
import work.rothe.tav.model.Document;
import work.rothe.tav.ui.table.MembersTablePanel;

import javax.swing.JPanel;
import java.awt.BorderLayout;

import static javax.swing.BorderFactory.createEmptyBorder;

public class DetailsPanel extends JPanel implements DocumentListener {
    private Document document;

    public DetailsPanel(MembersTablePanel membersTablePanel) {
        super(new BorderLayout());
        setBorder(createEmptyBorder(10, 10, 10, 10));
        setOpaque(false);

//        val northPanel = new JPanel(new BorderLayout());
//        northPanel.setOpaque(true);
//        add(northPanel, BorderLayout.NORTH);

        add(membersTablePanel, BorderLayout.CENTER);
    }

    public void register() {
        Documents.addDocumentListener(this);
    }

    public void unregister() {
        Documents.removeDocumentListener(this);
    }

    public void documentChanged(DocumentChangedEvent event) {
        this.document = event.document();
    }

    private void resizeTable() {

    }
}
