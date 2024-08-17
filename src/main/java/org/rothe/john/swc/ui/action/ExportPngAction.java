package org.rothe.john.swc.ui.action;

import lombok.val;
import org.rothe.john.swc.event.Documents;
import org.rothe.john.swc.model.Document;
import org.rothe.john.swc.ui.canvas.Canvas;
import org.rothe.john.swc.io.PNGWriter;
import org.rothe.john.swc.util.Images;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javax.swing.JFileChooser.APPROVE_OPTION;

public class ExportPngAction extends ToolbarAction {
    private final File HOME = new File(System.getProperty("user.home"));

    private final Canvas canvas;
    private File lastSelected;

    public ExportPngAction(Canvas canvas) {
        super("Export PNG", Images.load("export_image.png"));
        this.canvas = canvas;
    }

    private void write(File file) {
        try {
            ImageIO.write(PNGWriter.export(Documents.getCurrent()), "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        val document = canvas.getDocument();
        if (isNull(document)) {
            return;
        }

        val file = selectFile(defaultFileName(document));
        if (nonNull(file)) {
            write(file);
            lastSelected = file;
        }
    }

    private File selectFile(String defaultFileName) {
        val chooser = newChooser(defaultFileName);
        if (chooser.showDialog(canvas.getRootPane(), "Export") == APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    private JFileChooser newChooser(String defaultFileName) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogTitle("Export to PNG");
        chooser.setDragEnabled(false);
        chooser.setFileHidingEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setCurrentDirectory(HOME);
        chooser.setSelectedFile(getInitialSelection(defaultFileName));
        chooser.setFileFilter(new PngFileFilter());
        return chooser;
    }

    private File getInitialSelection(String defaultFileName) {
        if(nonNull(lastSelected)) {
            return lastSelected;
        } else {
            return new File(HOME, defaultFileName);
        }
    }

    private static String defaultFileName(Document document) {
        if(isNull(document) || document.name().trim().isEmpty()) {
            return "my_document.png";
        }
        return document.name()
                .toLowerCase()
                .replace(" ", "_").trim()
                .replaceAll("[^a-z0-9_-]","@@@@@")
                .replace("@@@@@", "")
                .concat(".png");
    }

    private static class PngFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".png");
        }

        @Override
        public String getDescription() {
            return "Portable Network Graphic (*.png)";
        }
    }
}
