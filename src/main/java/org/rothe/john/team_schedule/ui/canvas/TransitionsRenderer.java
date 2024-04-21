package org.rothe.john.team_schedule.ui.canvas;

import lombok.val;
import org.rothe.john.team_schedule.util.Zones;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.ZoneId;
import java.time.zone.ZoneRules;
import java.util.List;

import static java.awt.GridBagConstraints.WEST;
import static javax.swing.SwingConstants.HORIZONTAL;

// Note that the daylight savings time offset from UTC is always one higher in locations that use it.
public class TransitionsRenderer extends AbstractRenderer {
    private static final Color COLOR_FILL = new Color(252, 252, 252);
    private static final Color COLOR_LINE = Color.GRAY;

    private final List<ZoneId> zoneIds;

    public TransitionsRenderer(List<ZoneId> zoneIds) {
        super(COLOR_FILL, COLOR_LINE);
        this.zoneIds = zoneIds;

        setLayout(new GridBagLayout());
        initialize();
    }

    private void initialize() {
        add(new JLabel("Standard and Daylight Savings Time Transitions"), titleConstraints());

        for(ZoneId zoneId : zoneIds){
            ZoneRules rules = zoneId.getRules();
            if(rules.isFixedOffset()) {
                continue;
            }

            addLabel(Zones.getLocationDisplayString(zoneId), zoneConstraints());
            addLabel(Zones.getZoneAbbrev(zoneId), abbreviationConstraints());
            addLabel(Zones.toTransitionDateStr(rules), datesConstraints());
        }
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
