package work.rothe.tav.event;

import java.util.EventListener;

public interface DocumentListener extends EventListener {
    void documentChanged(DocumentChangedEvent event);
}
