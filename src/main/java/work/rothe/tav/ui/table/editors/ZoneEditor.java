package work.rothe.tav.ui.table.editors;

import lombok.val;
import work.rothe.tav.model.Zone;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import java.awt.Component;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.stream.Stream;

public class ZoneEditor extends DefaultCellEditor {
    private static DecoratedZone decorated(String id, String name) {
        return new DecoratedZone(ZoneId.of(id), name);
    }

    public ZoneEditor() {
        super(newCombo());
    }

    private static JComboBox<Zone> newCombo() {
        return new JComboBox<>(newComboModel());
    }

    private static DefaultComboBoxModel<Zone> newComboModel() {
        return new DefaultComboBoxModel<>(getAvailableZones());
    }

    private static Zone[] getAvailableZones() {
        return Stream.concat(getPreferredLocations(), Arrays.stream(Zone.getAvailableZones()))
                .toArray(Zone[]::new);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        val component = super.getTableCellEditorComponent(table, value, isSelected, row, column);
        component.setFont(table.getFont());
        return component;
    }

    private static Stream<DecoratedZone> getPreferredLocations() {
        return Stream.of(
                decorated("America/Costa_Rica", "Costa Rica"),
                decorated("America/Guayaquil", "Ecuador"),
                decorated("Asia/Calcutta", "India Standard Time"),
                decorated("America/Anchorage", "US Alaska"),
                decorated("America/Phoenix", "US Arizona"),
                decorated("America/Chicago", "US Central"),
                decorated("America/New_York", "US Eastern"),
                decorated("America/Adak", "US Hawaii"),
                decorated("America/Denver", "US Mountain"),
                decorated("America/Los_Angeles", "US Pacific"),
                decorated("America/Puerto_Rico", "US Puerto Rico"),
                decorated("America/Panama", "Panama"),
                decorated("Poland", "Poland")
        );
    }
}
