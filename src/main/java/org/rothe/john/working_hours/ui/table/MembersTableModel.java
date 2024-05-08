package org.rothe.john.working_hours.ui.table;

import lombok.val;
import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Availability;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.model.Time;
import org.rothe.john.working_hours.model.Zone;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class MembersTableModel extends AbstractTableModel {
    private final List<Member> members = new ArrayList<>();
    private Team team = null;

    public void setTeam(Team team) {
        this.team = team;
        this.members.clear();

        if (nonNull(team)) {
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
            case START_TIME, END_TIME, LUNCH_START, LUNCH_END -> {
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
            case START_TIME -> {
                return member.availability().normalStart();
            }
            case END_TIME -> {
                return member.availability().normalEnd();
            }
            case LUNCH_START -> {
                return member.availability().lunchStart();
            }
            case LUNCH_END -> {
                return member.availability().lunchEnd();
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

        try {
            val newTeam = team.withMembers(updatedMembers(aValue, rowIndex, columnIndex));
            SwingUtilities.invokeLater(new TeamChanged(columnIndex, newTeam));
        } catch (DateTimeParseException e) {
            System.err.printf("Ignoring invalid value '%s' entered as the '%s' for team member '%s'.%n",
                    aValue,
                    Columns.getColumn(columnIndex).getDescription(),
                    members.get(rowIndex).name());
        }
    }

    private List<Member> updatedMembers(Object aValue, int rowIndex, int columnIndex) {
        val newMembers = new ArrayList<>(members);
        newMembers.set(rowIndex, updatedMember(aValue, members.get(rowIndex), columnIndex));
        return newMembers;
    }

    private Member updatedMember(Object aValue, Member member, int columnIndex) {
        val column = Columns.getColumn(columnIndex);
        switch (column) {
            case NAME -> {
                return member.withName(aValue.toString());
            }
            case ROLE -> {
                return member.withRole(aValue.toString());
            }
            case LOCATION -> {
                return member.withLocation(aValue.toString());
            }
            case START_TIME, END_TIME, LUNCH_START, LUNCH_END -> {
                return member.withAvailability(updatedAvailability(aValue, member, column));
            }
            case ZONE -> {
                return member.withZone((Zone) aValue);
            }
        }
        return member;
    }

    private Availability updatedAvailability(Object aValue, Member member, Columns column) {
        return updatedAvailability(toTime(aValue), member.availability(), column);
    }

    private Availability updatedAvailability(Time value, Availability availability, Columns column) {
        switch (column) {
            case START_TIME -> {
                return availability.withNormalStart(value);
            }
            case END_TIME -> {
                return availability.withNormalEnd(value);
            }
            case LUNCH_START -> {
                return availability.withLunchStart(value);
            }
            case LUNCH_END -> {
                return availability.withLunchEnd(value);
            }
            default -> throw new IllegalArgumentException("Invalid availability column: " + column);
        }
    }

    private static Time toTime(Object aValue) {
        return roundToQuarterHour(Time.parse(aValue.toString()));
    }

    private static Time roundToQuarterHour(Time time) {
        return Time.at(time.getHour(), round15(time.getMinute()));
    }

    private static int round15(int minutes) {
        val mod = minutes % 15;
        if (mod > 7) {
            return minutes - mod + 15;
        }
        return minutes - mod;
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
