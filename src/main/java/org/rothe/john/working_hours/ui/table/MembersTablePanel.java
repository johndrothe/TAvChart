package org.rothe.john.working_hours.ui.table;

import org.rothe.john.working_hours.event.TeamChangedEvent;
import org.rothe.john.working_hours.event.TeamListener;
import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Team;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

import static javax.swing.BorderFactory.createEmptyBorder;

public class MembersTablePanel extends JPanel implements TeamListener {
    private final JPanel centerPanel = new JPanel(new BorderLayout());
    private final MembersTable table = new MembersTable();
    private final JScrollPane scrollPane = new JScrollPane(table);

    private Team team;

    public MembersTablePanel() {
        super(new BorderLayout());
        setBorder(createEmptyBorder(10, 10, 10, 10));
        setOpaque(false);

        add(centerPanel, BorderLayout.CENTER);
        centerPanel.setOpaque(true);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
    }

    public void register() {
        Teams.addTeamListener(this);
    }

    public void unregister() {
        Teams.removeTeamListener(this);
    }

    public void teamChanged(TeamChangedEvent event) {
        this.team = event.team();
        table.setTeam(team);
    }
}
