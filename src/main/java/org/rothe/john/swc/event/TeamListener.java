package org.rothe.john.swc.event;

import java.util.EventListener;

public interface TeamListener extends EventListener {
    void teamChanged(TeamChangedEvent event);
}
