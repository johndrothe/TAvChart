package org.rothe.john.swc.io;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class CsvFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".csv");
    }

    @Override
    public String getDescription() {
        return "Comma Separated Value (*.csv)";
    }
}
