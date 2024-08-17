package org.rothe.john.swc.event.undo;

import org.rothe.john.swc.event.NewDocumentEvent;
import org.rothe.john.swc.event.DocumentChangedEvent;
import org.rothe.john.swc.event.DocumentListener;

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
