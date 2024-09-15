package work.rothe.tav.event;

import lombok.RequiredArgsConstructor;
import work.rothe.tav.model.Document;

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
