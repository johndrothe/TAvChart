package org.rothe.john.working_hours.ui.action;

import lombok.val;
import org.rothe.john.working_hours.model.Team;
import org.rothe.john.working_hours.ui.canvas.Canvas;
import org.rothe.john.working_hours.util.Images;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javax.swing.JFileChooser.APPROVE_OPTION;

public class ExportImageAction extends ToolbarAction {
    private final File HOME = new File(System.getProperty("user.home"));

    private final Canvas canvas;
    private File lastSelected;

    public ExportImageAction(Canvas canvas) {
        super("Export PNG", Images.load("export_image.png"));
        this.canvas = canvas;
    }

    private void write(File file) {
        try {
            ImageIO.write(createImage(), "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage createImage() {
        BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        try {
            canvas.print(g);
        } finally {
            g.dispose();
        }
        return image;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        val team = canvas.getTeam();
        if (isNull(team)) {
            return;
        }

        val file = selectFile(defaultFileName(team));
        if (nonNull(file)) {
            write(file);
            lastSelected = file;
        }
    }

    private File selectFile(String defaultFileName) {
        val chooser = newChooser(defaultFileName);
        if (chooser.showOpenDialog(canvas.getParent()) == APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    private JFileChooser newChooser(String defaultFileName) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogTitle("Export to PNG");
        chooser.setApproveButtonText("Export");
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

    private static String defaultFileName(Team team) {
        if(isNull(team) || team.getName().trim().isEmpty()) {
            return "my_team.csv";
        }
        return team.getName()
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
