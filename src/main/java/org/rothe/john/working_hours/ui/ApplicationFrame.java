package org.rothe.john.working_hours.ui;

import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.event.undo.UndoListener;
import org.rothe.john.working_hours.ui.canvas.Canvas;
import org.rothe.john.working_hours.ui.table.MembersTablePanel;
import org.rothe.john.working_hours.ui.action.DisplayChangeEvent;
import org.rothe.john.working_hours.util.GBCBuilder;
import org.rothe.john.working_hours.util.SampleFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.BorderLayout.CENTER;

public class ApplicationFrame extends JFrame {
    private final UndoListener listener = new UndoListener();
    private final JPanel centerPanel = new JPanel(new BorderLayout());
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final MembersTablePanel tablePanel = new MembersTablePanel();
    private final Canvas canvas = new Canvas();
    private final Toolbar toolBar = new Toolbar(listener);
    private final MenuBar menuBar = new MenuBar(canvas, tablePanel.getTable(), listener);

    public ApplicationFrame() {
        super("Team Scheduler - 0.1.0");
        addWindowListener(new ExitOnCloseListener());
        initialize();
        setSize(1024, 768);
    }

    private void initialize() {
        Teams.addTeamListener(listener);

        initNorth();

        initCenter();
        doLayout();

        canvas.register();
        tablePanel.register();

        tabChanged(null);

        Teams.fireTeamChanged(this, "New", SampleFactory.newTeam());
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

        tabbedPane.add("Team Members", tablePanel);
        tabbedPane.add("Working Hours", newCanvasPanel());
        tabbedPane.addChangeListener(this::tabChanged);
        tabbedPane.setSelectedIndex(1);
    }

    private JPanel newCanvasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(canvas, CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setOpaque(false);
        return panel;
    }

    private void exitApplication() {
        setVisible(false);

        canvas.unregister();
        tablePanel.unregister();
        System.exit(0);
    }

    private class ExitOnCloseListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            exitApplication();
        }
    }

    private void tabChanged(ChangeEvent event) {
        toolBar.displayChanged(DisplayChangeEvent.of(tablePanel.getTable(), canvas,
                tabbedPane.getSelectedIndex() == 1));
    }
}
