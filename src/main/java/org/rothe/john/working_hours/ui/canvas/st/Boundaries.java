package org.rothe.john.working_hours.ui.canvas.st;

public record Boundaries(int left, int right){
    public int width() {
        return right - left;
    }
}
