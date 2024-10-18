package work.rothe.tav.ui;

import work.rothe.tav.event.Documents;
import work.rothe.tav.event.undo.UndoListener;
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
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.BorderLayout.CENTER;
import static work.rothe.tav.util.Constants.APP_NAME;

public class ApplicationFrame extends JFrame {
    private final ExitOnCloseListener exitListener = new ExitOnCloseListener();
    private final UndoListener listener = new UndoListener();
    private final JPanel centerPanel = new JPanel(new BorderLayout());
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final MembersTablePanel tablePanel = new MembersTablePanel();
    private final DetailsPanel detailsPanel = new DetailsPanel(tablePanel);
    private final Canvas canvas;
    private final MainToolbar toolBar;
    private final MainMenuBar menuBar;
    private final Settings settings;

    public ApplicationFrame(Settings settings) {
        super(APP_NAME);
        this.settings = settings;
        this.canvas = new Canvas(settings);
        this.toolBar = new MainToolbar(listener, canvas);
        this.menuBar = new MainMenuBar(zoomHandler(), canvas, tablePanel.getTable());

        initialize();
    }

    private void initialize() {
        initializeListeners();
        initializeContentPane();
        registerChildren();
        loadPostInitSettings();
    }

    private void initializeContentPane() {
        initNorth();
        initCenter();
        doLayout();
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
        scroll.getHorizontalScrollBar().setUnitIncrement(canvas.getRowHeightMinimum());
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(canvas.getRowHeightMinimum());
        return scroll;
    }

    private void exitApplication() {
        saveSettings();
        setVisible(false);

        unregisterChildren();

        System.exit(0);
    }

    private void saveSettings() {
        settings.setMainWindowSize(new Dimension(getWidth(), getHeight()));
        settings.setMainWindowLocation(getLocation());
        settings.setMainWindowMaximized(isMaximized());
        settings.save();
    }

    private boolean isMaximized() {
        return (getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0;
    }

    private void loadPostInitSettings() {
        setSize(settings.getMainWindowSize());
        setLocation(settings.getMainWindowLocation());
        if(settings.isMainWindowMaximized()) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }

    private void initializeListeners() {
        addWindowListener(exitListener);
        addWindowListener(new OffScreenWindowListener());
        Documents.addDocumentListener(listener);
        Documents.addDocumentListener(new ApplicationTitleListener(this));
    }

    public void setScaleAndExit(int uiScale) {
        settings.setUiScale(uiScale);
        exitApplication();
    }

    private ZoomHandler zoomHandler() {
        return new ZoomHandler(this::setScaleAndExit, settings::getUiScale);
    }

    private void registerChildren() {
        detailsPanel.register();
        canvas.register();
        tablePanel.register();
    }

    private void unregisterChildren() {
        detailsPanel.unregister();
        canvas.unregister();
        tablePanel.unregister();
    }

    private class ExitOnCloseListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            exitApplication();
        }
    }
}
