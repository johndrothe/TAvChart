package work.rothe.tav.ui.action;

import work.rothe.tav.event.Documents;
import work.rothe.tav.model.Document;
import work.rothe.tav.util.Images;

import javax.swing.JComponent;
import java.awt.event.ActionEvent;
import java.util.List;

import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;

public class NewDocumentAction extends ToolbarAction {
    private final JComponent parent;

    public NewDocumentAction(JComponent parent) {
        super("New Document", Images.load("new"));
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (confirm()) {
            Documents.fireNewDocument(this, "New Document", new Document("New Document", 0, List.of()));
        }
    }

    private boolean confirm() {
        return showConfirmDialog(parent,
                "Create New Document",
                "Create a new empty document?",
                YES_NO_OPTION) == YES_OPTION;
    }
}
