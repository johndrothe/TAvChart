package work.rothe.tav.io;

import javax.swing.filechooser.FileFilter;
import java.io.File;

import static work.rothe.tav.util.Constants.APP_NAME;
import static work.rothe.tav.util.Constants.FILE_EXTENSION;

public class TavFileFilter extends FileFilter {
    private final String extension = "." + FILE_EXTENSION;

    @Override
    public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(extension);
    }

    @Override
    public String getDescription() {
        return "%s File (*%s)".formatted(APP_NAME, extension);
    }
}
