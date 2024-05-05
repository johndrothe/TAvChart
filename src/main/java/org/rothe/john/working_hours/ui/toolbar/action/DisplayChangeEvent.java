package org.rothe.john.working_hours.ui.toolbar.action;

public class DisplayChangeEvent {
    private final boolean canvasDisplayed;

    public static DisplayChangeEvent of(boolean canvasDisplayed) {
        return new DisplayChangeEvent(canvasDisplayed);
    }

    private DisplayChangeEvent(boolean canvasDisplayed) {
        this.canvasDisplayed = canvasDisplayed;
    }

    public boolean isCanvasDisplayed() {
        return canvasDisplayed;
    }

    public boolean isTableDisplayed() {
        return !canvasDisplayed;
    }
}
