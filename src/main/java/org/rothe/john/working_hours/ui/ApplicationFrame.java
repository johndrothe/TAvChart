package org.rothe.john.working_hours.ui;

import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.ui.canvas.Canvas;
import org.rothe.john.working_hours.ui.membership.MembershipPanel;
import org.rothe.john.working_hours.util.SampleFactory;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;

public class ApplicationFrame extends JFrame {
    private final JPanel centerPanel = new JPanel(new BorderLayout());
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final MembershipPanel membershipPanel = new MembershipPanel();
    private final Canvas canvas = new Canvas();
    private final Toolbar toolBar = new Toolbar(canvas);

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
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.add(tabbedPane, CENTER);

        tabbedPane.add("Team Members", membershipPanel);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(canvas, CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setOpaque(false);
        tabbedPane.add("Working Hours", panel);
        assignTeam(SampleFactory.newTeam());
        tabbedPane.setSelectedComponent(panel);
    }

    private void assignTeam(final Team team) {
        canvas.setTeam(team);
        membershipPanel.setTeam(team);
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
