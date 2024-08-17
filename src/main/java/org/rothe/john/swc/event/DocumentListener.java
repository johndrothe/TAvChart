package org.rothe.john.swc.event;

import java.util.EventListener;

public interface DocumentListener extends EventListener {
    void documentChanged(DocumentChangedEvent event);
}
