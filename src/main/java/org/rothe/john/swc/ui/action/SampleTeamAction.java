package org.rothe.john.swc.ui.action;

import org.rothe.john.swc.event.Teams;
import org.rothe.john.swc.model.Member;
import org.rothe.john.swc.model.Team;
import org.rothe.john.swc.util.Images;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Supplier;

public class SampleTeamAction extends ToolbarAction {
    private final String name;
    private final Supplier<List<Member>> memberSupplier;

    public SampleTeamAction(String name, Supplier<List<Member>> memberSupplier) {
        super(name, Images.load("new.png"));
        this.name = name;
        this.memberSupplier = memberSupplier;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Teams.fireNewTeam(this, name, new Team(name, memberSupplier.get()));
    }
}
