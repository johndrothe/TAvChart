package work.rothe.tav.event;

import work.rothe.tav.model.Document;

public class NewDocumentEvent extends DocumentChangedEvent {

    public NewDocumentEvent(Object source, String event, Document document) {
        super(source, event, null, document);
    }
}
