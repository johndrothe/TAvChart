package work.rothe.tav.ui;

import lombok.val;
import work.rothe.tav.util.GBCBuilder;
import work.rothe.tav.util.ManifestUtil;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;

import static work.rothe.tav.util.Constants.APP_FULL_NAME;
import static work.rothe.tav.util.Constants.APP_NAME;
import static work.rothe.tav.util.ManifestUtil.getBuildDate;
import static work.rothe.tav.util.ManifestUtil.getVersion;

public class AboutDialog extends JDialog {
    public AboutDialog(Window parent) {
        super(parent);
        initialize();
    }

    private void initialize() {
        setTitle("About " + APP_NAME);
        setModalityType(ModalityType.APPLICATION_MODAL);

        addContents();
    }

    @Override
    public void setVisible(boolean b) {
        pack();
        setLocationRelativeTo(getParent());
        super.setVisible(b);
    }

    private void addContents() {
        getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(newAppFullNameHeader(), headingConstraints());
        panel.add(newLabel(version()), labelConstraints());
        panel.add(newLabel("Availability and collaboration zone charts for distributed teams."), labelConstraints());

        JPanel south = new JPanel(new BorderLayout());
        getContentPane().add(south, BorderLayout.SOUTH);
        south.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        south.add(new JButton(new WebsiteAction()), BorderLayout.WEST);
        south.add(new JButton(new CloseAction()), BorderLayout.EAST);
    }

    private String version() {
        return "%s version %s  -  %s".formatted(APP_NAME, getVersion(), getBuildDate());
    }

    private static JLabel newAppFullNameHeader() {
        val label = new JLabel(APP_FULL_NAME);
        label.setFont(label.getFont()
                .deriveFont(label.getFont().getSize2D() * 2f)
                .deriveFont(Font.BOLD));

        return label;
    }

    private static JLabel newLabel(String content) {
        val label = new JLabel(content);
        label.setFont(label.getFont().deriveFont(Font.PLAIN));
        return label;
    }

    private static GridBagConstraints labelConstraints() {
        return new GBCBuilder().gridx(0).fillHorizontal()
                .insets(0, 10, 20, 10)
                .build();
    }

    private static GridBagConstraints headingConstraints() {
        return new GBCBuilder().gridx(0).fillHorizontal().weightx(1.0)
                .anchor(GridBagConstraints.CENTER)
                .insets(0, 0, 20, 0)
                .build();
    }

    private class CloseAction extends AbstractAction {
        CloseAction() {
            super("Close");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
        }
    }

    private static class WebsiteAction extends AbstractAction {
        WebsiteAction() {
            super("Website");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isBrowseSupported()) {
                SwingUtilities.invokeLater(this::browse);
            } else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                SwingUtilities.invokeLater(this::xdgOpen);
            }
        }

        /**
         * Opens the TAvChart website using xdg-open by the Cross-Desktop Group at
         * <a href="https://www.freedesktop.org">freedesktop.org</a>.
         * <p></p>
         * xdg-open opens a file or URL in the user's preferred application.
         * If a URL is provided the URL will be opened in the user's preferred web browser.
         * <p></p>
         * Refer to the <a href="https://gitlab.freedesktop.org/xdg/xdg-utils">xdg-utils repository</a>
         * in GitLab for more information.
         */
        private void xdgOpen() {
            try {
                Runtime.getRuntime().exec(new String[]{"xdg-open", ManifestUtil.getWebsite()});
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void browse() {
            Desktop desktop = java.awt.Desktop.getDesktop();
            try {
                desktop.browse(URI.create(ManifestUtil.getWebsite()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        private boolean isBrowseSupported() {
            return super.isEnabled()
                    && Desktop.isDesktopSupported()
                    && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE);
        }
    }
}
