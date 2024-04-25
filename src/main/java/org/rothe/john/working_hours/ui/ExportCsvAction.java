package org.rothe.john.working_hours.ui;

import lombok.val;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.ui.canvas.Canvas;

import javax.swing.AbstractAction;
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

public class ExportCsvAction extends AbstractAction {
    private final File HOME = new File(System.getProperty("user.home"));

    private final JComponent parent;
    private final Canvas canvas;

    public ExportCsvAction(JComponent parent, Canvas canvas) {
        super("Export");
        this.parent = parent;
        this.canvas = canvas;
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
        val team = canvas.getTeam();
        if (isNull(team)) {
            return;
        }

        val path = selectFile();
        if (nonNull(path)) {
            write(path, canvas.getTeam());
        }
    }

    private Path selectFile() {
        val chooser = newChooser();
        if (chooser.showOpenDialog(parent.getParent()) == APPROVE_OPTION) {
            return chooser.getSelectedFile().toPath();
        }
        return null;
    }

    private JFileChooser newChooser() {
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
        chooser.setSelectedFile(new File(HOME, "my_team.csv"));
        chooser.setFileFilter(new CsvFileFilter());
        return chooser;
    }
}
