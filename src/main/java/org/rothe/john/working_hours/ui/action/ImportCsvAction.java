package org.rothe.john.working_hours.ui.action;

import lombok.val;
import org.rothe.john.working_hours.event.Teams;
import org.rothe.john.working_hours.io.CsvFileFilter;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.ui.table.MembersTable;
import org.rothe.john.working_hours.util.Images;

import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.isNull;
import static javax.swing.JFileChooser.APPROVE_OPTION;

public class ImportCsvAction extends ToolbarAction {
    private final File HOME = new File(System.getProperty("user.home"));

    private final MembersTable table;

    public ImportCsvAction(MembersTable table) {
        super("Import CSV", Images.load("load.png"));
        this.table = table;
    }

    public static Team read(Path path) {
        try {
            return Team.fromCsv(path.getFileName().toString(), Files.readString(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        val path = selectFile();
        if (isNull(path)) {
            return;
        }

        Team team = read(path);
        Teams.fireTeamChanged(this, "Import " + path.getFileName().toString(), team);
    }

    private Path selectFile() {
        val chooser = newChooser();
        if (chooser.showDialog(table.getRootPane(), "Import") == APPROVE_OPTION) {
            return chooser.getSelectedFile().toPath();
        }
        return null;
    }

    private JFileChooser newChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogTitle("Import from CSV");
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
