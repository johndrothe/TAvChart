package work.rothe.tav.event.undo;

import work.rothe.tav.event.NewDocumentEvent;
import work.rothe.tav.event.DocumentChangedEvent;
import work.rothe.tav.event.DocumentListener;

import javax.swing.undo.UndoManager;

import static java.util.Objects.isNull;

public class UndoListener extends UndoManager implements DocumentListener {
    public UndoListener() {
        setLimit(20);
    }

    @Override
    public void documentChanged(DocumentChangedEvent e) {
        if (e.source() == this) {
            return;
        }

        if (shouldDiscardEdits(e)) {
            discardAllEdits();
            return;
        }

        addEdit(new DocumentEdit(this, e.event(), e.oldDocument(), e.document()));
    }

    private static boolean shouldDiscardEdits(DocumentChangedEvent e) {
        return e instanceof NewDocumentEvent || isNull(e.oldDocument()) || isNull(e.document());
    }
}
