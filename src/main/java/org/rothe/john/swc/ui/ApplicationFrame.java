package org.rothe.john.swc.ui;

import org.rothe.john.swc.event.Documents;
import org.rothe.john.swc.event.undo.UndoListener;
import org.rothe.john.swc.ui.action.DisplayChangeEvent;
import org.rothe.john.swc.ui.canvas.Canvas;
import org.rothe.john.swc.ui.table.MembersTablePanel;
import org.rothe.john.swc.util.GBCBuilder;
import org.rothe.john.swc.util.Settings;
import org.rothe.john.swc.util.ZoomHandler;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.BorderLayout.CENTER;
import static org.rothe.john.swc.ui.canvas.Canvas.ROW_HEIGHT_MINIMUM;

public class ApplicationFrame extends JFrame {
    public static final String VERSION = "1.1.2";
    private final ExitOnCloseListener exitListener = new ExitOnCloseListener();
    private final UndoListener listener = new UndoListener();
    private final JPanel centerPanel = new JPanel(new BorderLayout());
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final MembersTablePanel tablePanel = new MembersTablePanel();
    private final Canvas canvas = new Canvas();
    private final Toolbar toolBar;
    private final MenuBar menuBar;
    private final Settings settings;

    public ApplicationFrame(Settings settings) {
        super("Working Hours - " + VERSION);
        this.settings = settings;
        this.toolBar = new Toolbar(listener);
        this.menuBar = new MenuBar(zoomHandler(), canvas, tablePanel.getTable(), listener);

        initialize();
    }

    private void initialize() {
        loadPreInitSettings();
        addWindowListener(exitListener);
        Documents.addDocumentListener(listener);

        initNorth();

        initCenter();
        doLayout();

        canvas.register();
        tablePanel.register();

        tabChanged(null);

        loadPostInitSettings();
    }

    private void initNorth() {
        JPanel northPanel = new JPanel(new GridBagLayout());
        getContentPane().add(northPanel, BorderLayout.NORTH);

        northPanel.add(menuBar, new GBCBuilder().gridy(0).weightx(1.0).fillHorizontal().build());
        northPanel.add(toolBar, new GBCBuilder().gridy(1).weightx(1.0).fillHorizontal().build());
    }

    private void initCenter() {
        getContentPane().add(centerPanel, CENTER);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.add(tabbedPane, CENTER);

        tabbedPane.add("Members", tablePanel);
        tabbedPane.add("Working Hours", newCanvasPanel());
        tabbedPane.addChangeListener(this::tabChanged);
        tabbedPane.setSelectedIndex(1);
    }

    private JPanel newCanvasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scroll = newCanvasScroll();
        panel.add(scroll, CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setOpaque(false);
        return panel;
    }

    private JScrollPane newCanvasScroll() {
        JScrollPane scroll = new JScrollPane(canvas);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(ROW_HEIGHT_MINIMUM);
        return scroll;
    }

    private void exitApplication() {
        saveSettings();
        setVisible(false);

        canvas.unregister();
        tablePanel.unregister();
        System.exit(0);
    }

    private void saveSettings() {
        settings.setMainWindowSize(new Dimension(getWidth(), getHeight()));
    }

    private void tabChanged(ChangeEvent event) {
        toolBar.displayChanged(DisplayChangeEvent.of(tablePanel.getTable(), canvas,
                tabbedPane.getSelectedIndex() == 1));
    }

    private void loadPreInitSettings() {
    }

    private void loadPostInitSettings() {
        setSize(Settings.minimum(settings.getMainWindowSize(), getScreenSize()));
    }

    private static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public void setScaleAndExit(int uiScale) {
        settings.setUiScale(uiScale);
        exitApplication();
    }

    private ZoomHandler zoomHandler() {
        return new ZoomHandler(this::setScaleAndExit, settings::getUiScale);
    }

    private class ExitOnCloseListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            exitApplication();
        }
    }
}
