package work.rothe.tav.ui.listeners;

import lombok.RequiredArgsConstructor;
import lombok.val;
import work.rothe.tav.event.DocumentChangedEvent;
import work.rothe.tav.event.DocumentListener;
import work.rothe.tav.event.Documents;
import work.rothe.tav.util.ManifestUtil;

import java.awt.Frame;

import static java.util.Objects.nonNull;
import static work.rothe.tav.util.Constants.APP_NAME;

@RequiredArgsConstructor
public class ApplicationTitleListener implements DocumentListener {
    private final Frame frame;

    @Override
    public void documentChanged(DocumentChangedEvent e) {
        frame.setTitle(title());
    }

    private static String title() {
        return "%s - %s %s".formatted(documentName(), APP_NAME, ManifestUtil.getVersion());
    }

    private static String documentName() {
        val document = Documents.getCurrent();
        if (nonNull(document)) {
            return document.name();
        }
        return "Empty";
    }
}
