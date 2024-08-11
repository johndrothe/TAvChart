package org.rothe.john.swc.event;

import org.rothe.john.swc.model.Team;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import java.util.concurrent.atomic.AtomicReference;

public class Teams {
    private static final EventListenerList listenerList = new EventListenerList();
    private static final AtomicReference<Team> currentTeam = new AtomicReference<>();

    public static Team getTeam() {
        return currentTeam.get();
    }

    /**
     * Adds a listener to the list that's notified each time a change
     * to the team occurs.
     *
     * @param l the TeamListener
     */
    public static void addTeamListener(TeamListener l) {
        listenerList.add(TeamListener.class, l);
    }

    /**
     * Removes a listener from the list that's notified each time a
     * change to the team occurs.
     *
     * @param l the TeamListener
     */
    public static void removeTeamListener(TeamListener l) {
        listenerList.remove(TeamListener.class, l);
    }

    /**
     * Returns an array of all the team changed listeners registered on this model.
     *
     * @return all of this model's <code>TeamListener</code>s
     * or an empty
     * array if no team changed listeners are currently registered
     * @see #addTeamListener
     * @see #removeTeamListener
     * @since 1.4
     */
    public static TeamListener[] getTeamListeners() {
        return listenerList.getListeners(TeamListener.class);
    }

    public static void fireTeamChanged(Object source, String change, Team newTeam) {
        fireTeamChanged(new TeamChangedEvent(source, change, currentTeam.get(), newTeam));
    }

    public static void fireNewTeam(Object source, String change, Team newTeam) {
        fireTeamChanged(new NewTeamEvent(source, change, newTeam));
    }

    /**
     * Forwards the given notification event to all <code>TeamListeners</code>
     * that registered themselves as listeners.
     *
     * @param event the event to be forwarded
     * @see #addTeamListener
     * @see TeamChangedEvent
     * @see EventListenerList
     */
    public static void fireTeamChanged(TeamChangedEvent event) {
        currentTeam.set(event.team());
        if(SwingUtilities.isEventDispatchThread()) {
            fireTeamChangedImpl(event);
        } else {
            SwingUtilities.invokeLater(() -> fireTeamChangedImpl(event));
        }
    }

    private static void fireTeamChangedImpl(TeamChangedEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TeamListener.class) {
                ((TeamListener) listeners[i + 1]).teamChanged(e);
            }
        }
    }

    public static TeamListener[] getListeners() {
        return listenerList.getListeners(TeamListener.class);
    }
}
