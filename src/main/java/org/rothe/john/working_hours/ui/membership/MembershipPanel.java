package org.rothe.john.working_hours.ui.membership;

import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.ui.util.GBCBuilder;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.List;

import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLineBorder;

public class MembershipPanel extends JPanel {
    private final JPanel centerPanel = new JPanel(new GridBagLayout());
    private List<MemberWidgets> widgets;

    private Team team;

    public MembershipPanel() {
        super(new BorderLayout());
        setBorder(createEmptyBorder(10, 10, 10, 10));
        setOpaque(false);
        centerPanel.setOpaque(true);
//        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(border());
        add(centerPanel, BorderLayout.CENTER);
    }

    public void setTeam(Team team) {
        this.team = team;
        this.widgets = toMemberWidgets(team.getMembers());

        centerPanel.removeAll();
        MemberWidgets.addHeaders(centerPanel);
        widgets.forEach(m -> m.addToPanel(centerPanel));
        centerPanel.add(Box.createGlue(), spacerConstraints());
    }

    private List<MemberWidgets> toMemberWidgets(Collection<Member> members) {
        return members.stream()
                .sorted(Member.offsetNameCompartor())
                .map(m -> new MemberWidgets(this, m))
                .toList();
    }

    private static GridBagConstraints spacerConstraints() {
        return new GBCBuilder().gridx(0).gridwidth(10).gridy(-1).weightx(0.0).weighty(1.0).fillVertical().build();
    }
    private Border border() {
        return createCompoundBorder(
                createLineBorder(Color.BLACK, 1),
                createEmptyBorder(10, 10, 10, 10));
    }
}
