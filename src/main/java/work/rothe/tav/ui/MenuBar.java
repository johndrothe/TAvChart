package work.rothe.tav.ui;

import com.github.swingdpi.DpiUtils;
import lombok.val;
import work.rothe.tav.event.undo.UndoListener;
import work.rothe.tav.ui.action.*;
import work.rothe.tav.ui.canvas.Canvas;
import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.ui.table.paste.Paster;
import work.rothe.tav.util.SampleFactory;
import work.rothe.tav.util.ZoomHandler;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.KeyEvent.VK_C;
import static java.awt.event.KeyEvent.VK_V;
import static java.awt.event.KeyEvent.VK_Y;
import static java.awt.event.KeyEvent.VK_Z;

public class MenuBar extends JMenuBar {
    private final ZoomHandler zoomHandler;
    private final Canvas canvas;
    private final MembersTable table;
    private final UndoListener listener;
    private final JMenuItem copy;
    private final JMenuItem paste;

    public MenuBar(ZoomHandler zoomHandler,
                   Canvas canvas,
                   MembersTable table,
                   UndoListener listener) {
        this.zoomHandler = zoomHandler;
        this.canvas = canvas;
        this.table = table;
        this.listener = listener;
        this.copy = newItem(new CopyAction(table), 'C', VK_C);
        this.paste = newItem(new PasteAction(table), 'P', VK_V);

        addMenus();

        table.getSelectionModel().addListSelectionListener(new TableSelectionListener(this::updateCopyPaste));
    }

    private void addMenus() {
        addFileMenu();
        addEditMenu();
        addViewMenu();
        addMembersMenu();
        add(createSampleMenu());
    }

    private void addViewMenu() {
        val menu = newMenu("Zoom", 'Z');

        for(int scale : DpiUtils.STANDARD_SCALINGS) {
            menu.add(newZoomItem(scale));
        }
    }

    private JRadioButtonMenuItem newZoomItem(int scale) {
        var item = new JRadioButtonMenuItem(new ZoomAction(canvas.getRootPane(), zoomHandler, scale));
        item.setSelected(zoomHandler.get() == scale);
        item.setEnabled(!item.isSelected());
        return item;
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

        menu.add(newItem(new NewDocumentAction(getRootPane()), 'N'));

        menu.addSeparator();
        menu.add(new OpenAction(table));
        menu.add(new SaveAction(table));

        menu.addSeparator();
        menu.add(new SaveAsAction(table));

        menu.addSeparator();
        menu.add(new ImportCsvAction(table)).setMnemonic('I');
        menu.add(new ExportCsvAction(table)).setMnemonic('E');
        menu.addSeparator();
        menu.add(new ExportPngAction(canvas)).setMnemonic('m');

        menu.addSeparator();
        menu.add(newItem(new ExitAction(getRootPane()), 'X'));
    }

    private JMenu createSampleMenu() {
        val menu = newMenu("Sample Teams", 'T');

        menu.add(new SampleDocumentAction("Centering Debug Team", SampleFactory::centeringDebugMembers));
        menu.add(new SampleDocumentAction("Debug Team", SampleFactory::debugMembers));
        menu.add(new SampleDocumentAction("Debug Shift Team", SampleFactory::debugShiftMembers));
        menu.add(new SampleDocumentAction("Demo Team", SampleFactory::demoMembers));

        return menu;
    }

    private void addEditMenu() {
        val menu = newMenu("Edit", 'E');

        menu.add(copy);
        menu.add(paste);
        menu.addMenuListener(new RunnableMenuListener(this::updateCopyPaste));

        menu.addSeparator();

        menu.add(newItem(new UndoAction(listener), 'U', VK_Z));
        menu.add(newItem(new RedoAction(listener), 'R', VK_Y));
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

    private void updateCopyPaste() {
        copy.setEnabled(table.isShowing() && table.getSelectedRowCount() > 0);
        paste.setEnabled(table.isShowing() && Paster.canPaste(table));
    }

    private record RunnableMenuListener(Runnable runnable) implements MenuListener {
        @Override
        public void menuSelected(MenuEvent e) {
            runnable.run();
        }

        @Override
        public void menuDeselected(MenuEvent e) {
        }

        @Override
        public void menuCanceled(MenuEvent e) {
        }
    }

    private record TableSelectionListener(Runnable runnable) implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            runnable.run();
        }
    }
}
