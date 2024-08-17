package org.rothe.john.swc.event;

import org.rothe.john.swc.model.Document;

public class NewDocumentEvent extends DocumentChangedEvent {

    public NewDocumentEvent(Object source, String event, Document document) {
        super(source, event, null, document);
    }
}
