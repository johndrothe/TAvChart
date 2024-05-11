package org.rothe.john.working_hours.ui.canvas.st;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Boundaries{
    private final int left;
    private final int right;

    public Boundaries(int left, int right) {
        this.left = left;
        this.right = right;
        if(left >= right){
            throw new IllegalArgumentException(String.format("Boundary left(%d) >= right (%d)",
                    left, right));
        }
    }

    public int left() {
        return left;
    }

    public int right() {
        return right;
    }

    public int width() {
        return right - left;
    }
}
