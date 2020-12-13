package com.andreas.main.save;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.crypto.SecretKey;

import com.andreas.main.FileUtils;
import com.andreas.main.stages.StageUtils;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class SaveTreeItem extends TreeItem<String> {
	private String nameCipher, typeCipher;
    private String name, type;

    private Save save;

    public SaveTreeItem(Save save) {
        this.save = save;
        name = "";
        type = "";
        nameCipher = "";
        typeCipher = "";

        setExpanded(true);
    }

    /**
     * Loads 
     * @param key
     */
    public void load(SecretKey key) {

        updateValue();

        forEachChild(e -> {
            e.load(key);
        });
    }

    /**
     * This method unloads the name, type and ciphers of this item.
     */
    public void unload() {
        name = "";
        type = "";
        nameCipher = "";
        typeCipher = "";
    }

    /**
     * Updates the value of this item.
     */
    public void updateValue() {
        setValue(name + (type.equals(Register.DIRECTORY) ? "" : type));
    }

    /**
     * @deprecated Use {@link #forEachChild(Consumer)}.
     */
    @Override
    public ObservableList<TreeItem<String>> getChildren() {
        return super.getChildren();
    }

    /**
     * This method iterates over every child.
     * @param action The action to execute.
     */
    public void forEachChild(Consumer<SaveTreeItem> action) {
        Objects.requireNonNull(action);
        for (TreeItem<String> t : super.getChildren())
            action.accept((SaveTreeItem)t);
    }

    /**
     * This method adds a child to this item.
     * @param item The item to add.
     */
    public void addChild(SaveTreeItem item) {
        super.getChildren().add(item);
    }

    /**
     * This method gets a child of this item by an index.
     * @param index The child index.
     */
    public SaveTreeItem getChild(int index) {
        return (SaveTreeItem)super.getChildren().get(index);
    }

    /**
     * This method removes a child from this item by an index.
     * @param index The child index.
     */
    public void removeChild(int index) {
        super.getChildren().remove(index);
    }

    /**
     * This method removes a child from this item.
     * @param item The child to remove.
     */
    public void removeChild(SaveTreeItem item) {
        super.getChildren().remove(item);
    }

     /**
     * This method calculates the path of the corresponding register.
     * @return Returns the path.
     */
    public String calculatePath() {
        String path = StageUtils.SAVES_PATH + save.getName();

        List<String> paths = new ArrayList<>();

        SaveTreeItem parent = (SaveTreeItem)getParent();

        byte[] cipher = save.encrypt((getName() + getType()).getBytes(StandardCharsets.UTF_8));
        paths.add(FileUtils.bytesToHex(cipher));
        while (parent.getParent() != null) {
            byte[] parentCipher = save.encrypt((parent.getName() + parent.getType()).getBytes(StandardCharsets.UTF_8));
            paths.add(FileUtils.bytesToHex(parentCipher) + "/");
            parent = (SaveTreeItem)parent.getParent();
        }

        paths.add("/registers/");

        for (int i = paths.size() - 1; i >= 0; i--) {
            path += paths.get(i);
        }

        return path;
    }

    // GETTERS AND SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        nameCipher = FileUtils.bytesToHex(save.encrypt(name.getBytes(StandardCharsets.UTF_8)));
        updateValue();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        typeCipher = FileUtils.bytesToHex(save.encrypt(type.getBytes(StandardCharsets.UTF_8)));
        updateValue();
    }

    public String getNameCipher() {
        return nameCipher;
    }

    public void setNameCipher(String nameCipher) {
        this.nameCipher = nameCipher;
        name = new String(save.encrypt(FileUtils.hexToBytes(nameCipher)), StandardCharsets.UTF_8);
        updateValue();
    }

    public String getTypeCipher() {
        return typeCipher;
    }

    public void setTypeCipher(String typeCipher) {
        this.typeCipher = typeCipher;
        type = new String(save.encrypt(FileUtils.hexToBytes(typeCipher)), StandardCharsets.UTF_8);
        updateValue();
    }
}