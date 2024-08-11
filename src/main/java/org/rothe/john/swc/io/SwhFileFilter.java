package org.rothe.john.swc.io;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class SwhFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".swh");
    }

    @Override
    public String getDescription() {
        return "Swing Working Hours Save File (*.swh)";
    }
}
