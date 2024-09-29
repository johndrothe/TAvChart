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

import static java.util.Objects.isNull;
import static javax.swing.JFileChooser.APPROVE_OPTION;

public class ImportCsvAction extends ToolbarAction {
    private final File HOME = new File(System.getProperty("user.home"));

    private final MembersTable table;

    public ImportCsvAction(MembersTable table) {
        super("Import CSV", Images.load("csv-import.png"));
        this.table = table;
    }

    public static Document read(Path path) {
        try {
            return Document.fromCsv(path.getFileName().toString(), Files.readString(path));
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

        Document document = read(path);
        Documents.fireDocumentChanged(this, "Import " + path.getFileName().toString(), document);
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
        chooser.setSelectedFile(new File(HOME, "my_document.csv"));
        chooser.setFileFilter(new CsvFileFilter());
        return chooser;
    }
}
