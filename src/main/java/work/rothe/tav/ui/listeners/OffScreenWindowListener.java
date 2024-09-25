package work.rothe.tav.ui.listeners;

import javax.swing.JFrame;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OffScreenWindowListener extends WindowAdapter {
    @Override
    public void windowOpened(WindowEvent e) {
        adjustWindow(e.getWindow());
        e.getWindow().removeWindowListener(this);
    }

    private static void adjustWindow(Window window) {
        if ((window instanceof Frame frame) && isMaximized(frame)) {
            return;
        }
        window.setBounds(fitWindowToScreen(window));
    }

    private static boolean isMaximized(Frame frame) {
        return (frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0;
    }

    private static Rectangle fitWindowToScreen(Window window) {
        return fitWindowToScreen(window.getBounds(), getScreenBounds(window));
    }

    private static Rectangle fitWindowToScreen(Rectangle window, Rectangle screen) {
        int width = Math.min(window.width, screen.width);
        int height = Math.min(window.height, screen.height);
        int x = adjustCoordinate(window.x, width, screen.x, screen.width);
        int y = adjustCoordinate(window.y, height, screen.y, screen.height);

        return new Rectangle(x, y, width, height);
    }

    private static int adjustCoordinate(int windowCoordinate,
                                        int windowSize,
                                        int screenCoordinate,
                                        int screenSize) {
        return Math.min(
                Math.max(windowCoordinate, screenCoordinate),
                (screenCoordinate + screenSize) - windowSize
        );
    }

    private static Rectangle getScreenBounds(Window window) {
        return window.getGraphicsConfiguration().getBounds();
    }
}
