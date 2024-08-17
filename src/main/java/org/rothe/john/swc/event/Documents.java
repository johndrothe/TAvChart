package org.rothe.john.swc.event;

import org.rothe.john.swc.model.Document;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import java.util.concurrent.atomic.AtomicReference;

public class Documents {
    private static final EventListenerList listenerList = new EventListenerList();
    private static final AtomicReference<Document> current = new AtomicReference<>();

    public static Document getCurrent() {
        return current.get();
    }

    /**
     * Adds a listener to the list that's notified each time a change
     * to the document occurs.
     *
     * @param l the DocumentListener
     */
    public static void addDocumentListener(DocumentListener l) {
        listenerList.add(DocumentListener.class, l);
    }

    /**
     * Removes a listener from the list that's notified each time a
     * change to the document occurs.
     *
     * @param l the DocumentListener
     */
    public static void removeDocumentListener(DocumentListener l) {
        listenerList.remove(DocumentListener.class, l);
    }

    /**
     * Returns an array of all the document changed listeners registered on this model.
     *
     * @return all of this model's <code>DocumentListener</code>s
     * or an empty
     * array if no document changed listeners are currently registered
     * @see #addDocumentListener
     * @see #removeDocumentListener
     * @since 1.4
     */
    public static DocumentListener[] getDocumentListeners() {
        return listenerList.getListeners(DocumentListener.class);
    }

    public static void fireDocumentChanged(Object source, String change, Document newDocument) {
        fireDocumentChanged(new DocumentChangedEvent(source, change, current.get(), newDocument));
    }

    public static void fireNewDocument(Object source, String change, Document newDocument) {
        fireDocumentChanged(new NewDocumentEvent(source, change, newDocument));
    }

    /**
     * Forwards the given notification event to all <code>DocumentListeners</code>
     * that registered themselves as listeners.
     *
     * @param event the event to be forwarded
     * @see #addDocumentListener
     * @see DocumentChangedEvent
     * @see EventListenerList
     */
    public static void fireDocumentChanged(DocumentChangedEvent event) {
        current.set(event.document());
        if(SwingUtilities.isEventDispatchThread()) {
            fireDocumentChangedImpl(event);
        } else {
            SwingUtilities.invokeLater(() -> fireDocumentChangedImpl(event));
        }
    }

    private static void fireDocumentChangedImpl(DocumentChangedEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == DocumentListener.class) {
                ((DocumentListener) listeners[i + 1]).documentChanged(e);
            }
        }
    }

    public static DocumentListener[] getListeners() {
        return listenerList.getListeners(DocumentListener.class);
    }
}
