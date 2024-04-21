package org.rothe.john.team_schedule.ui;

import org.rothe.john.team_schedule.model.Member;
import org.rothe.john.team_schedule.model.Team;
import org.rothe.john.team_schedule.ui.canvas.Canvas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.ZoneId;
import java.util.List;

import static org.rothe.john.team_schedule.util.Borders.empty;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;

public class ApplicationFrame extends JFrame {
    private final JPanel centerPanel = new JPanel(new BorderLayout());
    private final JPanel southPanel = new JPanel(new BorderLayout());
    private final JToolBar toolBar = new Toolbar();
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
        canvas.setTeam(newSampleTeam());
    }

    private static Team newSampleTeam() {
        return new Team("Winners",
                List.of(
                        new Member("Gertrude Bauer", "PO", "Berlin", ZoneId.of("Z")),
                        new Member("Trevor Jones", "PDM", "Baltimore", ZoneId.of("America/New_York")),
                        new Member("Bob Hope", "Developer", "Manhattan", ZoneId.of("America/New_York")),
                        new Member("Miles Davis", "Developer", "Jackson", ZoneId.of("America/Chicago")),
                        new Member("Davis Lynn", "Developer", "Chicago", ZoneId.of("America/Chicago")),
                        new Member("Tomasz Chlebek", "Developer", "Warsaw", ZoneId.of("Europe/Warsaw")),
                        new Member("Danuta Adamski", "Developer", "Prague", ZoneId.of("Europe/Warsaw"))
//                        new Member("Jill Lastname", "Developer", "Los Angeles", ZoneId.of("America/Los_Angeles")),
//                        new Member("Jane Smith", "Developer", "San Francisco", ZoneId.of("America/Los_Angeles"))
                ));
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
