package org.rothe.john.swc.ui.action;

import org.rothe.john.swc.event.Documents;
import org.rothe.john.swc.model.Document;
import org.rothe.john.swc.model.Member;
import org.rothe.john.swc.util.Images;

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
