package work.rothe.tav.ui;

import com.github.swingdpi.DpiUtils;
import lombok.val;
import work.rothe.tav.ui.action.*;
import work.rothe.tav.ui.canvas.Canvas;
import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.util.SampleFactory;
import work.rothe.tav.util.ZoomHandler;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class MainMenuBar extends JMenuBar {
    private final ZoomHandler zoomHandler;
    private final Canvas canvas;
    private final MembersTable table;

    public MainMenuBar(ZoomHandler zoomHandler,
                       Canvas canvas,
                       MembersTable table) {
        this.zoomHandler = zoomHandler;
        this.canvas = canvas;
        this.table = table;

        addMenus();
    }

    private void addMenus() {
        addFileMenu();
        addViewMenu();
        add(createSampleMenu());
    }

    private void addViewMenu() {
        val menu = newMenu("Zoom", 'Z');
        menu.addMenuListener(new ZoomMenuListener(menu));
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

    private JMenuItem newItem(Action a, char mnemonic) {
        val item = new JMenuItem(a);
        item.setMnemonic(mnemonic);
        return item;
    }

    private JMenu newMenu(String title, char mnemonic) {
        val menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        return add(menu);
    }

    private class ZoomMenuListener implements MenuListener {
        private final JMenu menu;

        ZoomMenuListener(JMenu menu) {
            this.menu = menu;
        }

        @Override
        public void menuSelected(MenuEvent e) {
            for (int scale : DpiUtils.STANDARD_SCALINGS) {
                menu.add(newZoomItem(scale));
            }
        }

        @Override
        public void menuDeselected(MenuEvent e) {
            menu.removeAll();
        }

        @Override
        public void menuCanceled(MenuEvent e) {
            menu.removeAll();
        }

        private JRadioButtonMenuItem newZoomItem(int scale) {
            var item = new JRadioButtonMenuItem(new ZoomAction(canvas, zoomHandler, scale));
            item.setSelected(zoomHandler.get() == scale);
            item.setEnabled(!item.isSelected());
            return item;
        }
    }
}
