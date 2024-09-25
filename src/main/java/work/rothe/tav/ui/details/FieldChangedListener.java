package work.rothe.tav.ui.details;

import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.function.Consumer;

record FieldChangedListener(JTextField field,
                            Consumer<String> fieldChanged)
        implements ActionListener, FocusListener {

    FieldChangedListener(JTextField field, Consumer<String> fieldChanged) {
        this.field = field;
        this.fieldChanged = fieldChanged;
        field.addFocusListener(this);
        field.addActionListener(this);
    }

    public static void register(JTextField field, Consumer<String> fieldChanged) {
        new FieldChangedListener(field, fieldChanged);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        field.transferFocus();
    }

    @Override
    public void focusGained(FocusEvent e) {
        // no-op
    }

    @Override
    public void focusLost(FocusEvent e) {
        changeValue();
    }

    private void changeValue() {
        fieldChanged.accept(field.getText());
    }
}
