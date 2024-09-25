package work.rothe.tav.ui.table.editors;

import work.rothe.tav.model.Zone;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;

import static java.util.Objects.nonNull;

class ProxyRenderer implements ListCellRenderer<Zone> {
    private final DividerPanel panel = new DividerPanel();

    private final ListCellRenderer<? super Zone> delegate;

    public ProxyRenderer(ListCellRenderer<? super Zone> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Zone> list, Zone value,
                                                  int index, boolean isSelected, boolean cellHasFocus)
    {
        Component c = delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        panel.removeAll();

        boolean before = isMarkedDifference(value, getElement(list, index - 1));
        boolean after = isMarkedDifference(value, getElement(list, index + 1));

        if(before || after) {
            panel.setUnder(after);
            panel.add(c, BorderLayout.CENTER);
            return panel;
        }

        return c;
    }

    private boolean isMarkedDifference(Zone z1, Zone z2) {
        return nonNull(z1) && nonNull(z2) && z1.getClass() != z2.getClass();
    }

    private Zone getElement(JList<? extends Zone> list, int index) {
        if(index < 0 || index >= list.getModel().getSize()) {
            return null;
        }
        return list.getModel().getElementAt(index);
    }

    private static class DividerPanel extends JPanel {
        private boolean under = true;
        public DividerPanel() {
            super(new BorderLayout());
            setOpaque(false);
        }

        public void setUnder(boolean under) {
            this.under = under;
        }

        @Override
        protected void paintChildren(Graphics g) {
            super.paintChildren(g);
            if(under) {
                g.fillRect(0, getHeight() - 2, getWidth(), 1);
            } else {
                g.fillRect(0, 1, getWidth(), 1);
            }
        }
    }
}
