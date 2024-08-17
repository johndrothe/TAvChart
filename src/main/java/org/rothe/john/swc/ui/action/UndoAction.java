package org.rothe.john.swc.ui.action;

import org.rothe.john.swc.event.DocumentChangedEvent;
import org.rothe.john.swc.event.DocumentListener;
import org.rothe.john.swc.event.Documents;
import org.rothe.john.swc.event.undo.UndoListener;
import org.rothe.john.swc.util.Images;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UndoAction extends ToolbarAction implements DocumentListener {
    private final UndoListener listener;

    public UndoAction(UndoListener listener) {
        super("Undo", Images.load("undo.png"));
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
