package org.rothe.john.swc.ui.table;

import lombok.Getter;
import lombok.val;
import org.rothe.john.swc.event.TeamChangedEvent;
import org.rothe.john.swc.event.TeamListener;
import org.rothe.john.swc.event.Teams;
import org.rothe.john.swc.model.Team;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

import static javax.swing.BorderFactory.createEmptyBorder;

public class MembersTablePanel extends JPanel implements TeamListener {
    @Getter
    private final MembersTable table = new MembersTable();

    private Team team;

    public MembersTablePanel() {
        super(new BorderLayout());
        setBorder(createEmptyBorder(10, 10, 10, 10));
        setOpaque(false);

        val centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(true);
        add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
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

    private void resizeTable() {

    }
}
