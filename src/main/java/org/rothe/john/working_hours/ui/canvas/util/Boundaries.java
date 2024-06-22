package org.rothe.john.working_hours.ui.canvas.util;

public record Boundaries(int left, int right) {
    public Boundaries(int left, int right) {
        this.left = left;
        this.right = right;
        if (left >= right) {
            throw new IllegalArgumentException(String.format("Boundary left(%d) >= right (%d)",
                    left, right));
        }
    }

    public int width() {
        return right - left;
    }
}
