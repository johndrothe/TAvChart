package work.rothe.tav.ui;

import work.rothe.tav.event.undo.UndoListener;
import work.rothe.tav.ui.action.ExportPngAction;
import work.rothe.tav.ui.action.NewDocumentAction;
import work.rothe.tav.ui.action.RedoAction;
import work.rothe.tav.ui.action.UndoAction;
import work.rothe.tav.ui.canvas.Canvas;

import javax.swing.JToolBar;

public class MainToolbar extends JToolBar {
    public MainToolbar(UndoListener listener, Canvas canvas) {
        super();
        setFloatable(false);

        createButtons(listener, canvas);
    }

    private void createButtons(UndoListener listener, Canvas canvas) {
        add(new NewDocumentAction(getRootPane()));

        addSeparator();
        add(new UndoAction(listener));
        add(new RedoAction(listener));
        addSeparator();

        add(new ExportPngAction(canvas));
    }
}
