package work.rothe.tav.ui.rows;

import org.junit.jupiter.api.Test;
import work.rothe.tav.model.Document;
import work.rothe.tav.model.Member;
import work.rothe.tav.model.Zone;
import work.rothe.tav.ui.canvas.rows.MemberRow;
import work.rothe.tav.ui.canvas.rows.RowBuilder;
import work.rothe.tav.ui.canvas.rows.TitleRow;
import work.rothe.tav.ui.canvas.rows.ZoneRow;
import work.rothe.tav.ui.canvas.rows.ZoneTransitionsRow;
import work.rothe.tav.ui.canvas.util.CanvasCalculator;
import work.rothe.tav.util.SampleFactory;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class RowBuilderTest {
    private final CanvasCalculator calculator = mock(CanvasCalculator.class);
    private final Document document = SampleFactory.newDocument();
    private final RowBuilder builder = RowBuilder.of(document).calculator(calculator);

    @Test
    void titleRow() {
        assertCount(1, e -> e.row() instanceof TitleRow);
    }

    @Test
    void zoneRows() {
        assertCount(4, e -> e.row() instanceof ZoneRow);

        document.zones().forEach(z -> assertTrue(zoneIncluded(z)));
    }

    private boolean zoneIncluded(Zone zone) {
        return builder.build().anyMatch(entry -> matches(entry, zone));
    }

    private static boolean matches(RowBuilder.Entry e, Zone zone) {
        return e.row() instanceof ZoneRow row && row.getZone().equals(zone);
    }

    @Test
    void memberRows() {
        assertCount(7, e -> e.row() instanceof MemberRow);

        document.members().forEach(member -> assertTrue(memberIncluded(member)));
    }

    private boolean memberIncluded(Member member) {
        return builder.build().anyMatch(entry -> matches(entry, member));
    }

    private static boolean matches(RowBuilder.Entry e, Member member) {
        return e.row() instanceof MemberRow row && row.getMember().equals(member);
    }

    @Test
    void transitionsRow() {
        assertCount(1, e -> e.row() instanceof ZoneTransitionsRow);
    }

    private void assertCount(long count, Predicate<RowBuilder.Entry> filter) {
        assertEquals(count, builder.build().filter(filter).count());
    }
}
