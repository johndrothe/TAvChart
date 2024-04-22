package org.rothe.john.team_schedule.ui.canvas;

import lombok.val;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.ZoneId;
import java.util.List;

import static java.awt.GridBagConstraints.WEST;
import static javax.swing.SwingConstants.HORIZONTAL;
import static org.rothe.john.team_schedule.util.Zones.getLocationDisplayString;
import static org.rothe.john.team_schedule.util.Zones.getZoneAbbrev;
import static org.rothe.john.team_schedule.util.Zones.toTransitionDateStr;

// Note that the daylight savings time offset from UTC is always one higher in locations that use it.
public class TransitionsRenderer extends AbstractRenderer {
    private static final Color COLOR_FILL = new Color(252, 252, 252);
    private static final Color COLOR_LINE = Color.GRAY;
    private static final String TITLE = "Standard and Daylight Savings Time Transitions";

    private final List<ZoneId> zoneIds;

    public TransitionsRenderer(CanvasInfo canvasInfo, List<ZoneId> zoneIds) {
        super(canvasInfo, COLOR_FILL, COLOR_LINE);
        this.zoneIds = zoneIds;

        setLayout(new GridBagLayout());
        initialize();
    }

    private void initialize() {
        add(new JLabel(TITLE), titleConstraints());

        zoneIds.stream()
                .filter(z -> !z.getRules().isFixedOffset())
                .forEach(this::addZoneLabels);
    }

    private void addZoneLabels(ZoneId zoneId) {
        addLabel(getLocationDisplayString(zoneId), zoneConstraints());
        addLabel(getZoneAbbrev(zoneId), abbreviationConstraints());
        addLabel(toTransitionDateStr(zoneId.getRules()), datesConstraints());
    }

    private void addLabel(String text, GridBagConstraints constraints) {
        val label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.PLAIN));
        add(label, constraints);
    }

    private static GridBagConstraints titleConstraints() {
        return new GridBagConstraints(0, -1, 3, 1,
                1.0, 0.0, WEST, HORIZONTAL,
                new Insets(0, 20, 5, 5), 0, 0);
    }

    private static GridBagConstraints zoneConstraints() {
        return new GridBagConstraints(0, -1, 1, 1,
                0.0, 0.0, WEST, HORIZONTAL,
                new Insets(5, 40, 0, 0), 0, 0);
    }

    private static GridBagConstraints abbreviationConstraints() {
        return new GridBagConstraints(1, -1, 1, 1,
                0.0, 0.0, WEST, HORIZONTAL,
                new Insets(5, 20, 0, 0), 0, 0);
    }

    private static GridBagConstraints datesConstraints() {
        return new GridBagConstraints(2, -1, 1, 1,
                0.0, 0.0, WEST, HORIZONTAL,
                new Insets(5, 25, 0, 0), 0, 0);
    }
}
