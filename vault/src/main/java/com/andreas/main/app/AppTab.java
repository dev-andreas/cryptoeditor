package com.andreas.main.app;

import java.nio.file.Path;
import java.util.function.Consumer;

import com.andreas.main.FileUtils;
import com.andreas.main.save.Register;
import com.andreas.main.save.SaveTreeItem;

import javafx.scene.control.Tab;

public abstract class AppTab extends Tab {
    
    private String name, type;
    protected byte[] data;
    private boolean editable, saved;
    private SaveTreeItem saveTreeItem;

    public AppTab(Register register, boolean editable, SaveTreeItem saveTreeItem) {
        this.editable = editable;
        this.saveTreeItem = saveTreeItem;
        saved = true;

        name = register.getName();
        type = register.getFileType();
        data = register.getContent();

        setText(register.isOpen() ? name + type : "Locked register");
    }

    public AppTab(Path filePath) {
        saved = true;

        String[] fullName = FileUtils.splitPrefixAndSuffix(filePath.getFileName().toString());

        name = fullName[0];
        type = fullName[1];

        data = FileUtils.readBinaryFile(filePath.toString());
        editable = !FileUtils.seemsBinary(data);
    
        setText(name + type);
    }

    /**
     * Saves the register of this tab.
     * @param save The corresponding save.
     */
    public void save(Consumer<byte[]> action) {
        if (!editable)
            return;

        action.accept(getNewContent());
        saved = true;
        setStyle("-fx-border-width: 0px");
    }

    public void unsave() {
        saved = false;
        setStyle("-fx-border-width: 1px 0px 0px 0px");
    }

    protected abstract byte[] getNewContent();

    // GETTERS AND SETTERS
    
    public boolean isEditable() {
        return editable;
    }

    public boolean isSaved() {
        return saved;
    }

    public String getName() {
        return name;
    }

    public String getFileType() {
        return type;
    }

    public SaveTreeItem getSaveTreeItem() {
        return saveTreeItem;
    }
}
