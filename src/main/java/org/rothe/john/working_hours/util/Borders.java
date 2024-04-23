package org.rothe.john.working_hours.util;

import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import static javax.swing.BorderFactory.createBevelBorder;
import static javax.swing.BorderFactory.createEmptyBorder;

public final class Borders {

    public static Border empty() {
        return createEmptyBorder(10, 10, 10, 10);
    }

    public static Border raised() {
        return createBevelBorder(BevelBorder.RAISED);
    }

    public static Border empty(int size) {
        return createEmptyBorder(size, size, size, size);
    }
}
