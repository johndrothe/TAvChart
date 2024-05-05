package org.rothe.john.working_hours.ui.table;

import lombok.val;
import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.model.Zone;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class MembersTableModel extends AbstractTableModel {
    private final List<Member> members = new ArrayList<>();
    private Team team = null;

    public void setTeam(Team team) {
        this.team = team;
        this.members.clear();

        if(nonNull(team)) {
            this.members.addAll(team.getMembers());
        }
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return members.size();
    }

    @Override
    public int getColumnCount() {
        return Columns.getCount();
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= getColumnCount()) {
            return null;
        }
        return Columns.getColumn(columnIndex).getDescription();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= getColumnCount()) {
            return Object.class;
        }
        switch (Columns.getColumn(columnIndex)) {
            case NAME, ROLE, LOCATION -> {
                return String.class;
            }
            case HOURS_START, HOURS_END, LUNCH_START, LUNCH_END -> {
                return LocalTime.class;
            }
            case ZONE -> {
                return Zone.class;
            }
            default -> {
                return Object.class;
            }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex < 0 || columnIndex >= getColumnCount()) {
            return Object.class;
        }
        if (rowIndex < 0 || rowIndex >= members.size()) {
            return null;
        }
        val member = members.get(rowIndex);

        switch (Columns.getColumn(columnIndex)) {
            case NAME -> {
                return member.name();
            }
            case ROLE -> {
                return member.role();
            }
            case LOCATION -> {
                return member.location();
            }
            case HOURS_START -> {
                return member.availability().normal().start();
            }
            case HOURS_END -> {
                return member.availability().normal().end();
            }
            case LUNCH_START -> {
                return member.availability().lunch().start();
            }
            case LUNCH_END -> {
                return member.availability().lunch().end();
            }
            case ZONE -> {
                return member.zone();
            }
            default -> throw new IllegalArgumentException("Invalid column: " + columnIndex);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex < 0 || columnIndex >= getColumnCount()) {
            return;
        }
        if (rowIndex < 0 || rowIndex >= members.size()) {
            return;
        }

        val newTeam = team.withMembers(updatedMembers(aValue, rowIndex, columnIndex));
        SwingUtilities.invokeLater(new TeamChanged(columnIndex, newTeam));
    }

    private List<Member> updatedMembers(Object aValue, int rowIndex, int columnIndex) {
        val newMembers = new ArrayList<>(members);
        newMembers.set(rowIndex, updatedMember(aValue, members.get(rowIndex), columnIndex));
        return newMembers;
    }

    private Member updatedMember(Object aValue, Member member, int columnIndex) {
        switch (Columns.getColumn(columnIndex)) {
            case NAME -> {
                return member.withName(aValue.toString());
            }
            case ROLE -> {
                return member.withRole(aValue.toString());
            }
            case LOCATION -> {
                return member.withLocation(aValue.toString());
            }
            case HOURS_START -> {
                val avail = member.availability();
                return member.withAvailability(avail.withNormal(avail.normal().withStart(LocalTime.parse(aValue.toString()))));
            }
            case HOURS_END -> {
                val avail = member.availability();
                return member.withAvailability(avail.withNormal(avail.normal().withEnd(LocalTime.parse(aValue.toString()))));
            }
            case LUNCH_START -> {
                val avail = member.availability();
                return member.withAvailability(avail.withLunch(avail.lunch().withStart(LocalTime.parse(aValue.toString()))));
            }
            case LUNCH_END -> {
                val avail = member.availability();
                return member.withAvailability(avail.withLunch(avail.lunch().withEnd(LocalTime.parse(aValue.toString()))));
            }
            case ZONE -> {
                return member.withZone((Zone) aValue);
            }
        }
        return member;
    }

    private static class TeamChanged implements Runnable {
        private final Team team;
        private final String change;

        public TeamChanged(int columnIndex, Team newTeam) {
            this.change = Columns.getColumn(columnIndex).getDescription();
            this.team = newTeam;
        }

        @Override
        public void run() {
            Teams.fireTeamChanged(this, change, team);
        }
    }
}
