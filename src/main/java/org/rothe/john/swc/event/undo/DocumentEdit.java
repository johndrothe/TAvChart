package org.rothe.john.swc.event.undo;

import org.rothe.john.swc.event.DocumentChangedEvent;
import org.rothe.john.swc.event.Documents;
import org.rothe.john.swc.model.Document;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

record DocumentEdit(UndoListener listener, String change, Document oldDocument, Document newDocument)
        implements UndoableEdit {

    @Override
    public void undo() throws CannotUndoException {
        Documents.fireDocumentChanged(new DocumentChangedEvent(listener, getUndoPresentationName(), newDocument, oldDocument));
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void redo() throws CannotRedoException {
        Documents.fireDocumentChanged(new DocumentChangedEvent(listener, getRedoPresentationName(), oldDocument, newDocument));
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public void die() {

    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean replaceEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean isSignificant() {
        return true;
    }

    @Override
    public String getPresentationName() {
        return change;
    }

    @Override
    public String getUndoPresentationName() {
        return "Undo " + change;
    }

    @Override
    public String getRedoPresentationName() {
        return "Redo " + change;
    }
}
