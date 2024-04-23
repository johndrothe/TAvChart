package org.rothe.john.working_hours.ui;

import org.rothe.john.working_hours.ui.canvas.Canvas;
import org.rothe.john.working_hours.util.SampleFactory;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static org.rothe.john.working_hours.util.Borders.empty;

public class ApplicationFrame extends JFrame {
    private final JPanel centerPanel = new JPanel(new BorderLayout());
    private final Toolbar toolBar = new Toolbar();
    private final Canvas canvas = new Canvas();

    public ApplicationFrame() {
        super("Team Scheduler - 0.0.1");
        initialize();
        setSize(1024, 768);
    }

    private void initialize() {
        initToolbar();
        initCenter();
        doLayout();
        initWindowClosing();
    }

    private void initToolbar() {
        getContentPane().add(toolBar, NORTH);
    }

    private void initCenter() {
        getContentPane().add(centerPanel, CENTER);
        centerPanel.setBorder(empty());
        centerPanel.setOpaque(true);
        initCanvas();
    }

    private void initCanvas() {
        centerPanel.add(canvas, CENTER);
        canvas.setTeam(SampleFactory.newTeam());
    }

    private void initWindowClosing() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

}
