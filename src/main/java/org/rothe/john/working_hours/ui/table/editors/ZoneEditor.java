package org.rothe.john.working_hours.ui.table.editors;

import org.rothe.john.working_hours.model.Zone;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

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
}
