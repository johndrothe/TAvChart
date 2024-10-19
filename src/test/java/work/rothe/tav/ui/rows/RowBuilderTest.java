package work.rothe.tav.ui.rows;

import org.junit.jupiter.api.Test;
import work.rothe.tav.model.Document;
import work.rothe.tav.model.Member;
import work.rothe.tav.model.Zone;
import work.rothe.tav.ui.canvas.rows.CanvasRow;
import work.rothe.tav.ui.canvas.rows.MemberRow;
import work.rothe.tav.ui.canvas.rows.RowBuilder;
import work.rothe.tav.ui.canvas.rows.TitleRow;
import work.rothe.tav.ui.canvas.rows.ZoneRow;
import work.rothe.tav.ui.canvas.rows.ZoneTransitionsRow;
import work.rothe.tav.ui.canvas.util.CanvasCalculator;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RowBuilderTest {
    private final Document document = new Document("Winners", 0, members());
    private final CanvasCalculator calculator = new CanvasCalculator(null, null, 100.0);
    private final RowBuilder builder = RowBuilder.of(document).calculator(calculator);

    @Test
    void rowCount() {
        assertEquals(13, builder.build().count());
    }

    @Test
    void titleRow() {
        assertCount(1, e -> e.row() instanceof TitleRow);
    }

    @Test
    void zoneRows() {
        assertCount(4, e -> e.row() instanceof ZoneRow);

        document.zones().forEach(z -> assertTrue(zoneIncluded(z)));
    }

    @Test
    void memberRows() {
        assertCount(7, e -> e.row() instanceof MemberRow);

        document.members().forEach(member -> assertTrue(memberIncluded(member)));
    }

    @Test
    void transitionsRow() {
        assertCount(1, e -> e.row() instanceof ZoneTransitionsRow);
    }

    private void assertCount(long count, Predicate<RowBuilder.Entry> filter) {
        assertEquals(count, builder.build().filter(filter).count());
    }

    @Test
    void titleRowConstraints() {
        assertConstraints(TitleRow.class, c -> c.gridx == 0);
        assertConstraints(TitleRow.class, c -> c.gridwidth == 27);
        assertConstraints(TitleRow.class, c -> c.weightx == 1.0);
        assertConstraints(TitleRow.class, c -> c.fill == GridBagConstraints.BOTH);
        assertConstraints(TitleRow.class, c -> c.insets.equals(new Insets(0, 5, 5, 5)));
        assertConstraints(TitleRow.class, c -> c.ipady == 4);
    }

    @Test
    void zoneRowConstraints() {
        assertConstraints(ZoneRow.class, c -> c.gridx == 0);
        assertConstraints(ZoneRow.class, c -> c.gridwidth == 27);
        assertConstraints(ZoneRow.class, c -> c.weightx == 1.0);
        assertConstraints(ZoneRow.class, c -> c.fill == GridBagConstraints.BOTH);
        assertConstraints(ZoneRow.class, c -> c.insets.equals(new Insets(0, 5, 2, 5)));
        assertConstraints(ZoneRow.class, c -> c.ipady == 4);
    }

    @Test
    void memberRowConstraints() {
        assertConstraints(MemberRow.class, c -> c.gridx == 0);
        assertConstraints(MemberRow.class, c -> c.gridwidth == 27);
        assertConstraints(MemberRow.class, c -> c.weightx == 1.0);
        assertConstraints(MemberRow.class, c -> c.fill == GridBagConstraints.BOTH);
        assertConstraints(MemberRow.class, c -> c.insets.equals(new Insets(0, 5, 2, 5)));
        assertConstraints(MemberRow.class, c -> c.ipady == 4);
    }

    @Test
    void transitionRowConstraints() {
        assertConstraints(ZoneTransitionsRow.class, c -> c.gridx == 0);
        assertConstraints(ZoneTransitionsRow.class, c -> c.gridwidth == 27);
        assertConstraints(ZoneTransitionsRow.class, c -> c.weightx == 1.0);
        assertConstraints(ZoneTransitionsRow.class, c -> c.fill == GridBagConstraints.NONE);
        assertConstraints(ZoneTransitionsRow.class, c -> c.insets.equals(new Insets(30, 5, 2, 5)));
        assertConstraints(ZoneTransitionsRow.class, c -> c.ipady == 10);
        assertConstraints(ZoneTransitionsRow.class, c -> c.ipadx == 50);
    }

    private boolean zoneIncluded(Zone zone) {
        return builder.build().anyMatch(entry -> matches(entry, zone));
    }

    private static boolean matches(RowBuilder.Entry e, Zone zone) {
        return e.row() instanceof ZoneRow row && row.getZone().equals(zone);
    }

    private boolean memberIncluded(Member member) {
        return builder.build().anyMatch(entry -> matches(entry, member));
    }

    private static boolean matches(RowBuilder.Entry e, Member member) {
        return e.row() instanceof MemberRow row && row.getMember().equals(member);
    }

    private void assertConstraints(Class<? extends CanvasRow> rowClass,
                                   Predicate<GridBagConstraints> match) {
        assertTrue(builder.build()
                .filter(e -> rowClass.isInstance(e.row()))
                .map(RowBuilder.Entry::constraints)
                .allMatch(match));
    }

    private static List<Member> members() {
        return List.of(
                toMember("Great Dev", "Developer", "Jackson", toZone("America/Chicago")),
                toMember("Excellent Dev", "Developer", "Chicago", toZone("America/Chicago")),
                toMember("Product Manager", "PDM", "Baltimore", toZone("America/New_York")),
                toMember("Jill Lastname", "Director", "Berlin", toZone("UTC")),
                toMember("Product Owner", "PO", "Berlin", toZone("UTC")),
                toMember("Fantastic Dev", "Developer", "Warsaw", toZone("Poland")),
                toMember("Fabulous Dev", "Developer", "Prague", toZone("Poland"))
        );
    }

    private static Zone toZone(String zoneId) {
        return new Zone(ZoneId.of(zoneId));
    }

    private static Member toMember(String name, String role, String location, Zone zone) {
        return new Member(name, role, location, zone);
    }
}
