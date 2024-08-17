package org.rothe.john.swc.event;

import lombok.RequiredArgsConstructor;
import org.rothe.john.swc.model.Document;

@RequiredArgsConstructor
public class DocumentChangedEvent {
    private final Object source;
    private final String event;
    private final Document oldDocument;
    private final Document document;

    public Object source() {
        return source;
    }

    public String event() {
        return event;
    }

    public Document oldDocument() {
        return oldDocument;
    }

    public Document document() {
        return document;
    }
}
