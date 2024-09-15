package work.rothe.tav.ui;

import lombok.val;
import work.rothe.tav.event.DocumentChangedEvent;
import work.rothe.tav.event.DocumentListener;
import work.rothe.tav.event.Documents;
import work.rothe.tav.event.undo.UndoListener;
import work.rothe.tav.ui.action.DisplayChangeEvent;
import work.rothe.tav.ui.canvas.Canvas;
import work.rothe.tav.ui.table.MembersTablePanel;
import work.rothe.tav.util.GBCBuilder;
import work.rothe.tav.util.ManifestUtil;
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
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.BorderLayout.CENTER;
import static java.util.Objects.nonNull;

public class ApplicationFrame extends JFrame {
    private final ExitOnCloseListener exitListener = new ExitOnCloseListener();
    private final UndoListener listener = new UndoListener();
    private final JPanel centerPanel = new JPanel(new BorderLayout());
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final MembersTablePanel tablePanel = new MembersTablePanel();
    private final Canvas canvas;
    private final Toolbar toolBar;
    private final MenuBar menuBar;
    private final Settings settings;

    public ApplicationFrame(Settings settings) {
        super(title());
        this.settings = settings;
        this.canvas = new Canvas(settings);
        this.toolBar = new Toolbar(listener);
        this.menuBar = new MenuBar(zoomHandler(), canvas, tablePanel.getTable(), listener);

        initialize();
    }

    private void initialize() {
        loadPreInitSettings();
        addWindowListener(exitListener);
        Documents.addDocumentListener(listener);
        Documents.addDocumentListener(new TitleListener());

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
        scroll.getVerticalScrollBar().setUnitIncrement(canvas.getRowHeightMinimum());
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

    public class TitleListener implements DocumentListener {
        @Override
        public void documentChanged(DocumentChangedEvent e) {
            setTitle(title());
        }
    }

    private static String title() {
        return "%s - TAvChart %s"
                .formatted(documentName(), ManifestUtil.getVersion());
    }

    private static String documentName() {
        val document = Documents.getCurrent();
        if (nonNull(document)) {
            return document.name();
        }
        return "Empty";
    }
}
