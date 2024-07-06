package org.rothe.john.working_hours.ui;

import lombok.val;
import org.rothe.john.working_hours.event.undo.UndoListener;
import org.rothe.john.working_hours.ui.action.*;
import org.rothe.john.working_hours.ui.canvas.Canvas;
import org.rothe.john.working_hours.ui.table.MembersTable;

import javax.swing.*;

import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.KeyEvent.*;

public class MenuBar extends JMenuBar {
    private final Canvas canvas;
    private final MembersTable table;
    private final UndoListener listener;

    public MenuBar(Canvas canvas, MembersTable table, UndoListener listener) {
        this.canvas = canvas;
        this.table = table;
        this.listener = listener;

        addMenus();
    }

    private void addMenus() {
        addFileMenu();
        addEditMenu();
        addMembersMenu();
    }

    private void addMembersMenu() {
        val menu = newMenu("Members", 'M');

        menu.add(new MemberAddAction(table));
        menu.add(new MemberRemoveAction(table));

        menu.addSeparator();
        menu.add(new MoveUpAction(table));
        menu.add(new MoveDownAction(table));
    }

    private void addFileMenu() {
        val menu = newMenu("File", 'F');

        menu.add(newItem(new NewTeamAction(getRootPane()), 'N'));
        menu.addSeparator();
        menu.add(new OpenAction(table)).setEnabled(false);
        menu.add(new SaveAction(table)).setEnabled(false);
        menu.addSeparator();
        menu.add(new SaveAsAction(table)).setEnabled(false);
        menu.addSeparator();

        menu.add(newItem(new ExitAction(getRootPane()), 'X'));
    }

    private void addEditMenu() {
        val menu = newMenu("Edit", 'E');

        menu.add(newItem(new CopyAction(table), 'C', VK_C));
        menu.add(newItem(new PasteAction(table), 'P', VK_V));

        menu.addSeparator();

        menu.add(newItem(new UndoAction(listener), 'U', VK_Z));
        menu.add(newItem(new RedoAction(listener), 'R', VK_Y));

        menu.addSeparator();
        menu.add(createImportMenu());
        menu.add(createExportMenu());
    }

    private JMenu createImportMenu() {
        var menu = new JMenu("Import");
        menu.setMnemonic('I');

        menu.add(new ImportCsvAction(table)).setMnemonic('C');
        return menu;
    }
    private JMenu createExportMenu() {
        var menu = new JMenu("Export");
        menu.setMnemonic('X');

        menu.add(new ExportCsvAction(table)).setMnemonic('C');
        menu.addSeparator();
        menu.add(new ExportImageAction(canvas)).setMnemonic('I');
        return menu;
    }

    private JMenuItem newItem(Action a, char mnemonic) {
        val item = new JMenuItem(a);
        item.setMnemonic(mnemonic);
        return item;
    }

    private JMenuItem newItem(Action a, char mnemonic, int accelerator) {
        val item = newItem(a, mnemonic);
        item.setAccelerator(KeyStroke.getKeyStroke(accelerator, CTRL_DOWN_MASK));
        return item;
    }

    private JMenu newMenu(String title, char mnemonic) {
        val menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        return add(menu);
    }
}
