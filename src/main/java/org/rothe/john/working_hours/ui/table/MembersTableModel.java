package org.rothe.john.working_hours.ui.table;

import lombok.val;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Zone;

import javax.swing.table.AbstractTableModel;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MembersTableModel extends AbstractTableModel {
    private final List<Member> members = new ArrayList<>();

    public void setMembers(List<Member> members) {
        this.members.clear();
        this.members.addAll(members);
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
        val member = members.get(rowIndex);

        switch (Columns.getColumn(columnIndex)) {
            case NAME -> {
                member.withName(aValue.toString());
//                return member.name();
            }
            case ROLE -> {
//                return member.role();
            }
            case LOCATION -> {
//                return member.location();
            }
            case HOURS_START -> {
//                return member.availability().normal().start();
            }
            case HOURS_END -> {
//                return member.availability().normal().end();
            }
            case LUNCH_START -> {
//                return member.availability().lunch().start();
            }
            case LUNCH_END -> {
//                return member.availability().lunch().end();
            }
            case ZONE -> {
//                return member.zone();
            }
        }
    }

}
