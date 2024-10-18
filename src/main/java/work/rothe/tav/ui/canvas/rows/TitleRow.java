package work.rothe.tav.ui.canvas.rows;

import work.rothe.tav.model.Document;
import work.rothe.tav.ui.canvas.util.CanvasCalculator;

import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class TitleRow extends CanvasRow {
    public TitleRow(CanvasCalculator calculator, Document document) {
        super(calculator, Color.BLACK, Color.BLACK);
        add(newNameLabel(document), BorderLayout.CENTER);
    }

    private static JLabel newNameLabel(Document document) {
        JLabel label = new JLabel(document.name());
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setFont(toTitleFont(label.getFont()));
        return label;
    }

    private static Font toTitleFont(Font font) {
        return font.deriveFont(Font.BOLD, font.getSize2D() * 1.2f);
    }
}
