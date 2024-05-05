package org.rothe.john.working_hours.ui.toolbar;

import org.rothe.john.working_hours.ui.toolbar.action.*;

import javax.swing.JButton;
import javax.swing.JToolBar;
import java.util.ArrayList;
import java.util.List;

public class Toolbar extends JToolBar {
    private final List<ToolbarAction> actions = new ArrayList<>();

    public Toolbar() {
        super();
        setFloatable(false);
        initializeButtons();
    }

    public void displayChanged(final DisplayChangeEvent event) {
        actions.forEach(a -> a.displayChanged(event));
    }

    public JButton add(ToolbarAction a) {
        actions.add(a);
        return super.add(a);
    }

    private void initializeButtons() {
        add(new ImportCsvAction(this));
        add(new ExportCsvAction(this));
        addSeparator();
        add(new CopyAction(this));
        add(new PasteAction(this));
        addSeparator();
        add(new UndoAction(this));
        add(new RedoAction(this));
        addSeparator();
        add(new MoveUpAction(this));
        add(new MoveDownAction(this));
    }
}
