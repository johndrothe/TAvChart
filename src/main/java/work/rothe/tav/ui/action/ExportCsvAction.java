package work.rothe.tav.ui.action;

import lombok.val;
import work.rothe.tav.event.Documents;
import work.rothe.tav.io.CsvFileFilter;
import work.rothe.tav.model.Document;
import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.util.Images;

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

public class ExportCsvAction extends ToolbarAction {
    private final File HOME = new File(System.getProperty("user.home"));

    private final MembersTable table;

    public ExportCsvAction(MembersTable table) {
        super("Export CSV", Images.load("csv-export"));
        this.table = table;
    }

    public static void write(Path path, Document document) {
        try {
            Files.writeString(path, document.toCsv(), CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        val document = Documents.getCurrent();
        if (isNull(document)) {
            return;
        }

        val path = selectFile(defaultFileName(document));
        if (nonNull(path)) {
            write(path, document);
        }
    }

    private Path selectFile(String defaultFileName) {
        val chooser = newChooser(defaultFileName);
        if (chooser.showDialog(table.getRootPane(), "Export") == APPROVE_OPTION) {
            return chooser.getSelectedFile().toPath();
        }
        return null;
    }

    private JFileChooser newChooser(String defaultFileName) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogTitle("Export to CSV");
        chooser.setDragEnabled(false);
        chooser.setFileHidingEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setCurrentDirectory(HOME);
        chooser.setSelectedFile(new File(HOME, defaultFileName));
        chooser.setFileFilter(new CsvFileFilter());
        return chooser;
    }

    private static String defaultFileName(Document document) {
        if(isNull(document) || document.name().trim().isEmpty()) {
            return "my_document.csv";
        }
        return document.name()
                .toLowerCase()
                .replace(" ", "_").trim()
                .replaceAll("[^a-z0-9_-]","@@@@@")
                .replace("@@@@@", "")
                .concat(".csv");
    }
}
