package work.rothe.tav.ui.action;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import work.rothe.tav.ui.canvas.Canvas;
import work.rothe.tav.ui.table.MembersTable;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DisplayChangeEvent {
    @Getter
    private final boolean canvasDisplayed;
    private final Canvas canvas;
    private final MembersTable table;

    public static DisplayChangeEvent of(MembersTable table, Canvas canvas, boolean canvasDisplayed) {
        return new DisplayChangeEvent(canvasDisplayed, canvas, table);
    }

    public boolean isTableDisplayed() {
        return !isCanvasDisplayed();
    }

    public Optional<Canvas> canvas() {
        return Optional.ofNullable(canvas);
    }

    public Optional<MembersTable> table() {
        return Optional.ofNullable(table);
    }
}
