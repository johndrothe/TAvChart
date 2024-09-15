package work.rothe.tav.ui.action;

import work.rothe.tav.event.Documents;
import work.rothe.tav.model.Document;
import work.rothe.tav.model.Member;
import work.rothe.tav.util.Images;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Supplier;

public class SampleDocumentAction extends ToolbarAction {
    private final String name;
    private final Supplier<List<Member>> memberSupplier;

    public SampleDocumentAction(String name, Supplier<List<Member>> memberSupplier) {
        super(name, Images.load("new.png"));
        this.name = name;
        this.memberSupplier = memberSupplier;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Documents.fireNewDocument(this, name, new Document(name, 0, memberSupplier.get()));
    }
}
