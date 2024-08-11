package org.rothe.john.swc.ui.action;

import lombok.val;
import org.rothe.john.swc.event.Teams;
import org.rothe.john.swc.io.SwhFileFilter;
import org.rothe.john.swc.model.Team;
import org.rothe.john.swc.ui.table.MembersTable;
import org.rothe.john.swc.util.Images;
import org.rothe.john.swc.io.json.Json;

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

public class SaveAction extends ToolbarAction {
    private final File HOME = new File(System.getProperty("user.home"));

    private final MembersTable table;

    public SaveAction(MembersTable table) {
        super("Save", Images.load("save.png"));
        this.table = table;
    }

    public static void write(Path path, Team team) {
        try {
            Files.writeString(path, Json.toJson(team), CREATE, TRUNCATE_EXISTING);
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
        if (chooser.showDialog(table.getRootPane(), "Save") == APPROVE_OPTION) {
            return chooser.getSelectedFile().toPath();
        }
        return null;
    }

    private JFileChooser newChooser(String defaultFileName) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogTitle("Save");
        chooser.setDragEnabled(false);
        chooser.setFileHidingEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setCurrentDirectory(HOME);
        chooser.setSelectedFile(new File(HOME, defaultFileName));
        chooser.setFileFilter(new SwhFileFilter());
        return chooser;
    }

    private static String defaultFileName(Team team) {
        if(isNull(team) || team.getName().trim().isEmpty()) {
            return "my_team.swh";
        }
        return team.getName()
                .toLowerCase()
                .replace(" ", "_").trim()
                .replaceAll("[^a-z0-9_-]","@@@@@")
                .replace("@@@@@", "")
                .concat(".swh");
    }
}
