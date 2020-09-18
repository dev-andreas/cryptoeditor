package com.andreas.main.app;

import com.andreas.main.FileUtils;
import com.andreas.main.save.Register;
import com.andreas.main.save.Save;
import com.andreas.main.stages.saveStage.SaveController;
import com.andreas.main.stages.saveStage.SaveStage;

import javafx.scene.control.Tab;

public abstract class RegisterTab extends Tab {

    public static RegisterTab getCorrectTab(SaveStage saveStage, Register register) {
        if (!register.isOpen())
            return null;
        
        if (FileUtils.seemsBinary(register.getContent())) {
            return new BinaryTab(saveStage, register);
        } else {
            return new TextTab(saveStage, register);
        }
    }

    // CLASS
    
    protected Register register;
    private boolean editable, saved;

    private SaveStage saveStage;
    private SaveController saveController;

    public RegisterTab(SaveStage saveStage, Register register, boolean editable) {
        this.register = register;
        this.editable = editable;
        this.saveStage = saveStage;
        saveController = (SaveController)this.saveStage.getController();
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
