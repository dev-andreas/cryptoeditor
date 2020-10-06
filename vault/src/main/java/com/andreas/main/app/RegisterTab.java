package com.andreas.main.app;

import com.andreas.main.FileUtils;
import com.andreas.main.app.imageTab.ImageTab;
import com.andreas.main.save.Register;
import com.andreas.main.save.Save;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveController;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.scene.control.Tab;

public abstract class RegisterTab extends Tab {

    public static RegisterTab getCorrectTab(SaveScene saveScene, Register register) {
        if (!register.isOpen())
            return null;
        
        if (FileUtils.seemsBinary(register.getContent())) {
            for (int i = 0; i < Register.ACCEPTED_IMAGE_FILE_TYPES.length; i++) {
                if (Register.ACCEPTED_IMAGE_FILE_TYPES[i].equals(register.getFileType().toLowerCase()))
                    return new ImageTab(saveScene, register);
            }
            return new BinaryTab(saveScene, register);
        } else {
            return new TextTab(saveScene, register);
        }
    }

    // CLASS
    
    protected Register register;
    private boolean editable, saved;

    @SuppressWarnings("unused")
    private SaveScene saveScene;
    private SaveController saveController;

    public RegisterTab(SaveScene saveScene, Register register, boolean editable) {
        this.register = register;
        this.editable = editable;
        this.saveScene = saveScene;
        saveController = (SaveController) saveScene.getController();
        saved = true;

        setText(register.isOpen() ? register.getName() + register.getFileType() : "Locked register");
    }

    /**
     * Saves the register of this tab.
     * @param save The corresponding save.
     */
    public void save(Save save) {
        if (!editable)
            return;
        register.setContent(getNewContent());
        save.saveRegister(register);
        saved = true;
        setStyle("-fx-border-width: 0px");
    }

    public void unsave() {
        saved = false;
        saveController.savedState.setText("Unsaved changes.");
        setStyle("-fx-border-width: 1px 0px 0px 0px");
    }

    protected abstract byte[] getNewContent();

    // GETTERS AND SETTERS
    
    /**
     * @return Returns <code>true</code> if this register is editable.
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * @return Returns <code>true</code> if this register is saved.
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * @return This method returns the corresponding register.
     */
    public Register getRegister() {
        return register;
    }
}
