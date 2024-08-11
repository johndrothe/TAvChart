package org.rothe.john.swc.ui.action;

import org.rothe.john.swc.ui.table.MembersTable;
import org.rothe.john.swc.util.Images;

import java.awt.event.ActionEvent;
import java.io.File;

public class SaveAsAction extends ToolbarAction {
    private final File HOME = new File(System.getProperty("user.home"));

    private final MembersTable table;

    public SaveAsAction(MembersTable table) {
        super("Save As", Images.load("save.png"));
        this.table = table;
    }
//
//    public static void write(Path path, Team team) {
//        try {
//            Files.writeString(path, team.toCsv(), CREATE, TRUNCATE_EXISTING);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        val team = Teams.getTeam();
//        if (isNull(team)) {
//            return;
//        }
//
//        val path = selectFile(defaultFileName(team));
//        if (nonNull(path)) {
//            write(path, team);
//        }
    }

//    private Path selectFile(String defaultFileName) {
//        val chooser = newChooser(defaultFileName);
//        if (chooser.showDialog(table.getRootPane(), "Export") == APPROVE_OPTION) {
//            return chooser.getSelectedFile().toPath();
//        }
//        return null;
//    }
//
//    private JFileChooser newChooser(String defaultFileName) {
//        JFileChooser chooser = new JFileChooser();
//        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
//        chooser.setAcceptAllFileFilterUsed(false);
//        chooser.setDialogTitle("Export to CSV");
//        chooser.setDragEnabled(false);
//        chooser.setFileHidingEnabled(false);
//        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        chooser.setMultiSelectionEnabled(false);
//        chooser.setCurrentDirectory(HOME);
//        chooser.setSelectedFile(new File(HOME, defaultFileName));
//        chooser.setFileFilter(new CsvFileFilter());
//        return chooser;
//    }
//
//    private static String defaultFileName(Team team) {
//        if(isNull(team) || team.getName().trim().isEmpty()) {
//            return "my_team.csv";
//        }
//        return team.getName()
//                .toLowerCase()
//                .replace(" ", "_").trim()
//                .replaceAll("[^a-z0-9_-]","@@@@@")
//                .replace("@@@@@", "")
//                .concat(".csv");
//    }
}
