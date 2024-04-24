package org.rothe.john.working_hours.ui.canvas.rows;

import lombok.val;
import org.rothe.john.working_hours.ui.canvas.CanvasInfo;
import org.rothe.john.working_hours.model.Zone;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import static java.awt.GridBagConstraints.WEST;
import static javax.swing.SwingConstants.HORIZONTAL;

// Note that the daylight savings time offset from UTC is always one higher in locations that use it.
public class ZoneTransitionsRow extends CanvasRow {
    private static final Color COLOR_FILL = new Color(252, 252, 252);
    private static final Color COLOR_LINE = Color.GRAY;
    private static final String TITLE = "Standard and Daylight Savings Time Transitions";

    private final List<Zone> zones;

    public ZoneTransitionsRow(CanvasInfo canvasInfo, List<Zone> zones) {
        super(canvasInfo, COLOR_FILL, COLOR_LINE);
        this.zones = zones;

        setLayout(new GridBagLayout());
        initialize();
    }

    private void initialize() {
        add(new JLabel(TITLE), titleConstraints());

        zones.stream()
                .filter(z-> !z.isFixedOffset())
                .forEach(this::addLabels);
    }

    private void addLabels(Zone zone) {
        addLabel(zone.getId(), zoneConstraints());
        addLabel(zone.getAbbreviation(), abbreviationConstraints());
        addLabel(zone.toTransitionDateStr(zone.getRules()), datesConstraints());
    }

    private void addLabel(String text, GridBagConstraints constraints) {
        val label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.PLAIN));
        add(label, constraints);
    }

    private static GridBagConstraints titleConstraints() {
        return newConstraints(0, 3, new Insets(0, 20, 5, 5));
    }

    private static GridBagConstraints zoneConstraints() {
        return newConstraints(0, 1, new Insets(5, 40, 0, 0));
    }

    private static GridBagConstraints abbreviationConstraints() {
        return newConstraints(1, 1, new Insets(5, 20, 0, 0));
    }

    private static GridBagConstraints datesConstraints() {
        return newConstraints(2, 1, new Insets(5, 25, 0, 0));
    }

    private static GridBagConstraints newConstraints(int gridX, int gridWidth, Insets insets) {
        return new GridBagConstraints(gridX, -1, gridWidth, 1,
                1.0, 0.0, WEST, HORIZONTAL, insets, 0, 0);
    }
}
