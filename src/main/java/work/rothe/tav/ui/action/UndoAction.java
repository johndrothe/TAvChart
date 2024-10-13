package work.rothe.tav.ui.action;

import work.rothe.tav.event.DocumentChangedEvent;
import work.rothe.tav.event.DocumentListener;
import work.rothe.tav.event.Documents;
import work.rothe.tav.event.undo.UndoListener;
import work.rothe.tav.util.Images;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UndoAction extends ToolbarAction implements DocumentListener {
    private final UndoListener listener;

    public UndoAction(UndoListener listener) {
        super("Undo", Images.load("undo"));
        this.listener = listener;

        Documents.addDocumentListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        listener.undo();
    }

    @Override
    public void documentChanged(DocumentChangedEvent event) {
        SwingUtilities.invokeLater(this::updateDisplay);
    }

    private void updateDisplay() {
        putValue(SHORT_DESCRIPTION, listener.getUndoPresentationName());
        setEnabled(listener.canUndo());
    }
}
