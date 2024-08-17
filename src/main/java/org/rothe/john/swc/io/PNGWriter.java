package org.rothe.john.swc.io;

import org.rothe.john.swc.event.NewDocumentEvent;
import org.rothe.john.swc.model.Document;
import org.rothe.john.swc.ui.canvas.Canvas;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PNGWriter {
    private final Canvas canvas = new Canvas();
    private PNGWriter() {
        super();
        canvas.setVisible(true);
    }

    public static BufferedImage export(Document document) {
        var exporter = new PNGWriter();
        exporter.setDocument(document);
        exporter.doLayout();
        return exporter.exportImage();
    }

    private BufferedImage exportImage() {
        BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        try {
            canvas.print(g);
        } finally {
            g.dispose();
        }
        return image;
    }

    private void setDocument(Document document) {
        canvas.setPreferredSize(document.canvasSize());
        canvas.setSize(document.canvasSize());
        canvas.documentChanged(new NewDocumentEvent(this, "Export", document));
    }

    private void doLayout() {
        canvas.doLayout();
        for(Component child : canvas.getComponents()) {
            child.doLayout();
        }
    }
}
