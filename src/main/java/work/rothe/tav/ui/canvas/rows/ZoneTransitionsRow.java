package work.rothe.tav.ui.canvas.rows;

import lombok.val;
import work.rothe.tav.model.Zone;
import work.rothe.tav.ui.canvas.util.CanvasCalculator;
import work.rothe.tav.util.GBCBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Note that the daylight savings time offset from UTC is always one higher in locations that use it.
public class ZoneTransitionsRow extends CanvasRow {
    private static final Color COLOR_FILL = new Color(252, 252, 252);
    private static final Color COLOR_LINE = Color.GRAY;
    private static final String TITLE = "Standard and Daylight Savings Time Transitions";

    private final List<Zone> zones;

    public ZoneTransitionsRow(CanvasCalculator calculator, List<Zone> zones) {
        super(calculator, COLOR_FILL, COLOR_LINE);
        this.zones = zones;

        setLayout(new GridBagLayout());
        initialize();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintRowShape(g);
    }

    private void paintRowShape(Graphics g) {
        g.setColor(getFillColor());
        fillRowArea(g, getRowMinX(), getRowWidth(), getHeight());

        g.setColor(getLineColor());
        drawRowBorder(g, getRowMinX(), getRowWidth(), getHeight());
    }

    private void initialize() {
        add(new JLabel(TITLE), titleConstraints());

        zones.stream()
                .filter(Zone::hasTransitions)
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
        return defaultConstraints().gridx(0).gridwidth(3).insets(0, 20, 5, 5).build();
    }

    private static GridBagConstraints zoneConstraints() {
        return defaultConstraints().gridx(0).insets(5, 40, 0, 0).build();
    }

    private static GridBagConstraints abbreviationConstraints() {
        return defaultConstraints().gridx(1).insets(5, 20, 0, 0).build();
    }

    private static GridBagConstraints datesConstraints() {
        return defaultConstraints().gridx(2).insets(5, 25, 0, 0).build();
    }

    private static GBCBuilder defaultConstraints() {
        return new GBCBuilder().weightx(1.0).anchorWest().fillHorizontal();
    }
}
