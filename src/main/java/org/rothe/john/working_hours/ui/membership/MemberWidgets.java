package org.rothe.john.working_hours.ui.membership;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.rothe.john.working_hours.model.Member;
import org.rothe.john.working_hours.model.Zone;
import org.rothe.john.working_hours.ui.util.GBCBuilder;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.time.LocalTime;
import java.time.ZoneOffset;

@RequiredArgsConstructor
public class MemberWidgets {
    private final MembershipPanel parent;
    @Getter
    private final Member member;

    public static void addHeaders(JPanel panel) {
        panel.add(label(""), constraints(0));
        panel.add(label("Role"), constraints(1));
        panel.add(label("Location"), constraints(2));

        panel.add(label("Working Hours"), constraints().gridx(3).gridwidth(2).build());
        panel.add(label("Lunch Hour"), constraints().gridx(5).gridwidth(2).build());
        panel.add(label("Time Zone"), constraints(7));
    }

    private static JLabel label(String text) {
        val label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    public void addToPanel(JPanel panel) {
        panel.add(new JTextField(member.name()), constraints(0));
        panel.add(new JTextField(member.role()), constraints(1));
        panel.add(new JTextField(member.location()), constraints(2));

        panel.add(timeField(member.zone(), member.availability().normal().start()), constraints(3));
        panel.add(timeField(member.zone(), member.availability().normal().end()), constraints(4));
        panel.add(timeField(member.zone(), member.availability().lunch().start()), constraints(5));
        panel.add(timeField(member.zone(), member.availability().lunch().end()), constraints(6));

        val combo = new JComboBox<>(zoneComboModel());
        combo.setSelectedItem(member.zone().getRawZoneId());
        combo.setFont(combo.getFont().deriveFont(Font.PLAIN));
        panel.add(combo, constraints(7));
    }

    private JTextField timeField(Zone zone, LocalTime time) {
        val field = new JTextField(time.toString());
        field.setHorizontalAlignment(JTextField.RIGHT);

//        field.setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat("HH:mm"))));
//        field.setValue(time.atDate(LocalDate.now()).atZone(zone.getRawZoneId()));

        return field;
    }

    private static DefaultComboBoxModel<String> zoneComboModel() {
        return new DefaultComboBoxModel<>(getAvailableZones());
    }

    private static String[] getAvailableZones() {
        return ZoneOffset.getAvailableZoneIds().stream()
                .sorted()
                .toArray(String[]::new);
    }

    private static GridBagConstraints constraints(int gridx) {
        return constraints().gridx(gridx).build();
    }

    private static GBCBuilder constraints() {
        return new GBCBuilder().weightx(1.0).fillBoth().insets(5);
    }
}
