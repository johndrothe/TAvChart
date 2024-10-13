package work.rothe.tav.ui.action;

import lombok.val;
import work.rothe.tav.event.Documents;
import work.rothe.tav.io.TavFileFilter;
import work.rothe.tav.model.Document;
import work.rothe.tav.ui.table.MembersTable;
import work.rothe.tav.util.Images;
import work.rothe.tav.io.json.Json;

import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;
import static javax.swing.JFileChooser.APPROVE_OPTION;

public class OpenAction extends ToolbarAction {
    private final File HOME = new File(System.getProperty("user.home"));

    private final MembersTable table;

    public OpenAction(MembersTable table) {
        super("Open", Images.load("load"));
        this.table = table;
    }

    public static Document read(Path path) {
        try {
            return Json.fromJson(Files.readString(path, UTF_8), Document.class);
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
        Documents.fireDocumentChanged(this, "Open " + path.getFileName().toString(), document);
    }

    private Path selectFile() {
        val chooser = newChooser();
        if (chooser.showDialog(table.getRootPane(), "Open") == APPROVE_OPTION) {
            return chooser.getSelectedFile().toPath();
        }
        return null;
    }

    private JFileChooser newChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogTitle("Open");
        chooser.setDragEnabled(false);
        chooser.setFileHidingEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setCurrentDirectory(HOME);
        chooser.setFileFilter(new TavFileFilter());
        return chooser;
    }
}
