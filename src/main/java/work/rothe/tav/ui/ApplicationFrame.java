package work.rothe.tav.ui;

import work.rothe.tav.event.Documents;
import work.rothe.tav.event.undo.UndoListener;
import work.rothe.tav.ui.action.DisplayChangeEvent;
import work.rothe.tav.ui.canvas.Canvas;
import work.rothe.tav.ui.details.DetailsPanel;
import work.rothe.tav.ui.listeners.ApplicationTitleListener;
import work.rothe.tav.ui.listeners.OffScreenWindowListener;
import work.rothe.tav.ui.table.MembersTablePanel;
import work.rothe.tav.util.GBCBuilder;
import work.rothe.tav.util.Settings;
import work.rothe.tav.util.ZoomHandler;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.BorderLayout.CENTER;

public class ApplicationFrame extends JFrame {
    private final ExitOnCloseListener exitListener = new ExitOnCloseListener();
    private final UndoListener listener = new UndoListener();
    private final JPanel centerPanel = new JPanel(new BorderLayout());
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final MembersTablePanel tablePanel = new MembersTablePanel();
    private final DetailsPanel detailsPanel = new DetailsPanel(tablePanel);
    private final Canvas canvas;
    private final Toolbar toolBar;
    private final MenuBar menuBar;
    private final Settings settings;

    public ApplicationFrame(Settings settings) {
        super("TAvChart");
        this.settings = settings;
        this.canvas = new Canvas(settings);
        this.toolBar = new Toolbar(listener);
        this.menuBar = new MenuBar(zoomHandler(), canvas, tablePanel.getTable(), listener);

        initialize();
    }

    private void initialize() {
        loadPreInitSettings();
        addWindowListener(exitListener);
        addWindowListener(new OffScreenWindowListener());
        Documents.addDocumentListener(listener);
        Documents.addDocumentListener(new ApplicationTitleListener(this));

        initNorth();

        initCenter();
        doLayout();

        detailsPanel.register();
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

        tabbedPane.add("Details", detailsPanel);
        tabbedPane.add("Availability", newCanvasPanel());
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
        scroll.getVerticalScrollBar().setUnitIncrement(canvas.getRowHeightMinimum());
        return scroll;
    }

    private void exitApplication() {
        saveSettings();
        setVisible(false);

        detailsPanel.unregister();
        canvas.unregister();
        tablePanel.unregister();
        System.exit(0);
    }

    private void saveSettings() {
        settings.setMainWindowSize(new Dimension(getWidth(), getHeight()));
        settings.setMainWindowLocation(getLocation());
        settings.save();
    }

    private void tabChanged(ChangeEvent event) {
        toolBar.displayChanged(DisplayChangeEvent.of(tablePanel.getTable(), canvas,
                tabbedPane.getSelectedIndex() == 1));
    }

    private void loadPreInitSettings() {
    }

    private void loadPostInitSettings() {
        setSize(settings.getMainWindowSize());
        setLocation(settings.getMainWindowLocation());
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
