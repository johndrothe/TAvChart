package org.rothe.john.working_hours.ui.table;

import org.rothe.john.working_hours.event.TeamChangedEvent;
import org.rothe.john.working_hours.event.TeamListener;
import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Team;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import java.util.Optional;

import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLineBorder;

public class MembersTablePanel extends JPanel implements TeamListener {
    private final Toolbar toolBar = new Toolbar();
    private final JPanel centerPanel = new JPanel(new BorderLayout());
    private final MembersTable table = new MembersTable();
    private final JScrollPane scrollPane = new JScrollPane(table);

    private Team team;

    public MembersTablePanel() {
        super(new BorderLayout());
        setBorder(panelBorder());
        setOpaque(false);

        add(toolBar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        centerPanel.setOpaque(true);
        centerPanel.setBorder(centerBorder());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private Border centerBorder() {
        return createEmptyBorder(10, 10, 10, 10);
    }

    private Border panelBorder() {
        return createCompoundBorder(
                createEmptyBorder(10, 10, 10, 10),
                createLineBorder(Color.BLACK, 1));
    }

    public void register() {
        Teams.addTeamListener(this);
    }

    public void unregister() {
        Teams.removeTeamListener(this);
    }

    public void teamChanged(TeamChangedEvent event) {
        this.team = event.team();
        table.setMembers(Optional.ofNullable(team).map(Team::getMembers).orElse(List.of()));
    }

}
