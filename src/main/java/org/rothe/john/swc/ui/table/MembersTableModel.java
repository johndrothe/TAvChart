package org.rothe.john.swc.ui.table;

import lombok.val;
import org.rothe.john.swc.event.Teams;
import org.rothe.john.swc.model.Member;
import org.rothe.john.swc.model.Team;
import org.rothe.john.swc.model.Time;
import org.rothe.john.swc.model.Zone;

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
            this.members.addAll(team.members());
        }
        fireTableDataChanged();
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
                return member.normal().left();
            }
            case END_TIME -> {
                return member.normal().right();
            }
            case LUNCH_START -> {
                return member.lunch().left();
            }
            case LUNCH_END -> {
                return member.lunch().right();
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
            fireTeamChanged(columnIndex, setValue(aValue, rowIndex, columnIndex));
        } catch (DateTimeParseException e) {
            System.err.printf("Ignoring invalid value '%s' entered as the '%s' for team member '%s'.%n",
                    aValue,
                    Columns.getColumn(columnIndex).getDescription(),
                    members.get(rowIndex).name());
        }
    }

    protected Team setValue(Object aValue, int rowIndex, int columnIndex) {
        return team.withMembers(updatedMembers(aValue, rowIndex, columnIndex));
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
            case START_TIME -> {
                return member.withNormal(member.normal().withLeft(toTime(member.zone(), aValue)));
            }
            case END_TIME -> {
                return member.withNormal(member.normal().withRight(toTime(member.zone(), aValue)));
            }
            case LUNCH_START -> {
                return member.withLunch(member.lunch().withLeft(toTime(member.zone(), aValue)));
            }
            case LUNCH_END -> {
                return member.withLunch(member.lunch().withRight(toTime(member.zone(), aValue)));
            }
            case ZONE -> {
                return member.withZone(toZone(aValue));
            }
        }
        return member;
    }

    private static Zone toZone(Object aValue) {
        if(aValue instanceof Zone zone) {
            return zone;
        }
        return Zone.fromCsv(aValue.toString());
    }

    private static Time toTime(Zone zone, Object aValue) {
        return Time.parse(zone, aValue.toString()).roundToQuarterHour();
    }

    private void fireTeamChanged(int columnIndex, Team newTeam) {
        Teams.fireTeamChanged(this,
                "Edit " + Columns.getColumn(columnIndex).getDescription(),
                newTeam);
    }
}
