package work.rothe.tav.ui.action;

import work.rothe.tav.event.DocumentChangedEvent;
import work.rothe.tav.event.DocumentListener;
import work.rothe.tav.event.Documents;
import work.rothe.tav.event.undo.UndoListener;
import work.rothe.tav.util.Images;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RedoAction extends ToolbarAction implements DocumentListener {
    private final UndoListener listener;

    public RedoAction(UndoListener listener) {
        super("Redo", Images.load("redo"));
        this.listener = listener;

        Documents.addDocumentListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        listener.redo();
    }

    @Override
    public void documentChanged(DocumentChangedEvent event) {
        SwingUtilities.invokeLater(this::updateDisplay);
    }

    private void updateDisplay() {
        putValue(SHORT_DESCRIPTION, listener.getRedoPresentationName());
        setEnabled(listener.canRedo());
    }
}
