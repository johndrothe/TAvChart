package work.rothe.tav.ui.listeners;

import lombok.val;

import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OffScreenWindowListener extends WindowAdapter {
    @Override
    public void windowOpened(WindowEvent e) {
        adjustWindow(e);

        e.getWindow().removeWindowListener(this);
    }

    private void adjustWindow(WindowEvent e) {
        val window = e.getWindow();

        window.setBounds(adjustBounds(getScreenBounds(e.getWindow()), window.getBounds()));
    }

    private Rectangle adjustBounds(Rectangle screen, Rectangle window) {
        int width = Math.min(window.width, screen.width);
        int height = Math.min(window.height, screen.height);
        int x = adjustCoordinate(window.x, width, screen.x, screen.width);
        int y = adjustCoordinate(window.y, height, screen.y, screen.height);

        return new Rectangle(x, y, width, height);
    }

    private int adjustCoordinate(int windowCoordinate, int windowSize, int screenCoordinate, int screenSize) {
        return Math.min(
                Math.max(windowCoordinate, screenCoordinate),
                (screenCoordinate + screenSize) - windowSize
        );
    }

    private static Rectangle getScreenBounds(Window window) {
        return window.getGraphicsConfiguration().getBounds();
    }
}
