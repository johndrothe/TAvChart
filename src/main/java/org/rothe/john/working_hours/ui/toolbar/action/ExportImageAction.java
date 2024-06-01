package org.rothe.john.working_hours.ui.toolbar.action;

import lombok.val;
import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.ui.CsvFileFilter;
import org.rothe.john.working_hours.util.Images;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javax.swing.JFileChooser.APPROVE_OPTION;

public class ExportImageAction extends ToolbarAction {
    private final File HOME = new File(System.getProperty("user.home"));

    private final JComponent parent;

    public ExportImageAction(JComponent parent) {
        super("Export", Images.load("export_image.png"));
        this.parent = parent;
    }

    public static void write(Path path, Team team) {
        try {
            Files.writeString(path, team.toCsv(), CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        val team = Teams.getTeam();
        if (isNull(team)) {
            return;
        }

        val path = selectFile(defaultFileName(team));
        if (nonNull(path)) {
            write(path, team);
        }
    }

    private Path selectFile(String defaultFileName) {
        val chooser = newChooser(defaultFileName);
        if (chooser.showOpenDialog(parent.getParent()) == APPROVE_OPTION) {
            return chooser.getSelectedFile().toPath();
        }
        return null;
    }

    private JFileChooser newChooser(String defaultFileName) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogTitle("Export to CSV");
        chooser.setApproveButtonText("Export");
        chooser.setDragEnabled(false);
        chooser.setFileHidingEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setCurrentDirectory(HOME);
        chooser.setSelectedFile(new File(HOME, defaultFileName));
        chooser.setFileFilter(new CsvFileFilter());
        return chooser;
    }

    private static String defaultFileName(Team team) {
        if(isNull(team) || team.getName().trim().isEmpty()) {
            return "my_team.csv";
        }
        return team.getName()
                .toLowerCase()
                .replace(" ", "_").trim()
                .replaceAll("[^a-z0-9_-]","@@@@@")
                .replace("@@@@@", "")
                .concat(".csv");
    }
}
