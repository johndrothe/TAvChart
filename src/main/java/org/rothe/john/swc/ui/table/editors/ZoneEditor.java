package org.rothe.john.swc.ui.table.editors;

import lombok.val;
import org.rothe.john.swc.model.Zone;

import javax.swing.*;
import java.awt.*;

public class ZoneEditor extends DefaultCellEditor {
    public ZoneEditor() {
        super(newCombo());
    }

    private static JComboBox<Zone> newCombo() {
        return new JComboBox<>(newComboModel());
    }

    private static DefaultComboBoxModel<Zone> newComboModel() {
        return new DefaultComboBoxModel<>(Zone.getAvailableZones());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        val component = super.getTableCellEditorComponent(table, value, isSelected, row, column);
        component.setFont(table.getFont());
        return component;
    }
}
