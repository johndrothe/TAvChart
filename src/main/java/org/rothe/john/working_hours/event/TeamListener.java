package org.rothe.john.working_hours.event;

import java.util.EventListener;

public interface TeamListener extends EventListener {
    void teamChanged(TeamChangedEvent event);
}
