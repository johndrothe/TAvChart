package work.rothe.tav.ui.details;

import lombok.val;
import work.rothe.tav.event.DocumentChangedEvent;
import work.rothe.tav.event.DocumentListener;
import work.rothe.tav.event.Documents;
import work.rothe.tav.model.Document;
import work.rothe.tav.ui.table.MembersTablePanel;
import work.rothe.tav.util.GBCBuilder;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import static java.util.Objects.nonNull;
import static javax.swing.BorderFactory.createEmptyBorder;
import static work.rothe.tav.event.Documents.fireDocumentChanged;

public class DetailsPanel extends JPanel implements DocumentListener {
    private final JTextField nameField = new JTextField(30);
    private final JTextField widthField = sizeField();
    private final JTextField heightField = sizeField();

    private Document document;

    public DetailsPanel(MembersTablePanel membersTablePanel) {
        super(new BorderLayout());
        setBorder(createEmptyBorder(10, 10, 10, 10));
        setOpaque(false);

        add(documentDetailsPanel(), BorderLayout.NORTH);
        add(membersTablePanel, BorderLayout.CENTER);
    }

    public void register() {
        Documents.addDocumentListener(this);
    }

    public void unregister() {
        Documents.removeDocumentListener(this);
    }

    public void documentChanged(DocumentChangedEvent event) {
        this.document = event.document();
        if (nonNull(document)) {
            nameField.setText(document.name());
            widthField.setText(Integer.toString(document.canvasSize().width));
            heightField.setText(Integer.toString(document.canvasSize().height));
        }
    }

    private JPanel documentDetailsPanel() {
        val panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        panel.setBorder(createEmptyBorder(0, 0, 10, 0));

        panel.add(new JLabel("Team Name"), labelConstraints(0));
        panel.add(nameField, fieldConstraints(1, 0, 5));
        FieldChangedListener.register(nameField, this::nameChanged);


        panel.add(new JLabel("Page Size"), labelConstraints(1));
        panel.add(widthField, fieldConstraints(1, 1, 1));
        FieldChangedListener.register(widthField, this::widthChanged);

        panel.add(new JLabel("x"), dimensionConstraints());
        panel.add(heightField, fieldConstraints(3, 1, 1));
        FieldChangedListener.register(heightField, this::heightChanged);

        panel.add(new JLabel("(pixels)"), unitsConstraints());

        panel.add(Box.createGlue(), new GBCBuilder().gridy(0).weightx(1.0).build());

        return panel;
    }

    private void widthChanged(String text) {
        int value = Integer.parseInt(text);
        Dimension size = document.canvasSize();
        if (value != size.width) {
            fireDocumentChanged(this, "Width Edit",
                    document.withCanvasSize(new Dimension(value, size.height)));
        }
    }
    private void heightChanged(String text) {
        int value = Integer.parseInt(text);
        Dimension size = document.canvasSize();
        if (value != size.height) {
            fireDocumentChanged(this, "Height Edit",
                    document.withCanvasSize(new Dimension(size.width, value)));
        }
    }

    private void nameChanged(String text) {
        if (!document.name().equals(text)) {
            fireDocumentChanged(this, "Name Edit", document.withName(text));
        }
    }

    private static GridBagConstraints dimensionConstraints() {
        return new GBCBuilder().gridx(2).gridy(1)
                .insets(5).fillVertical().build();
    }

    private static GridBagConstraints unitsConstraints() {
        return new GBCBuilder().gridx(4).gridy(1)
                .insets(5).fillVertical().build();
    }

    private static GridBagConstraints fieldConstraints(int gridx, int gridy, int gridwidth) {
        return new GBCBuilder()
                .gridx(gridx).gridwidth(gridwidth).gridy(gridy)
                .anchorWest().fillVertical()
                .insets(0, 0, 5, 0).build();
    }

    private static GridBagConstraints labelConstraints(int gridY) {
        return new GBCBuilder().gridx(0).gridy(gridY)
                .anchorEast().fillVertical()
                .insets(0, 0, 5, 10).build();
    }

    private static JTextField sizeField() {
        JTextField field = new JTextField(6);
        field.setHorizontalAlignment(JTextField.RIGHT);
        return field;
    }
}
