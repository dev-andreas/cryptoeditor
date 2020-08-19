package com.andreas.main.stages.renameRegisterStage;

import com.andreas.main.App;
import com.andreas.main.app.AppStage;
import com.andreas.main.stages.saveStage.SaveStage;

import javafx.stage.Modality;

public class RenameRegisterStage extends AppStage {

    private SaveStage saveStage;

    public RenameRegisterStage(App app, SaveStage saveStage) {
        super(app, "stages/renameRegisterStage/renameRegister.fxml");
        setTitle("Rename register");
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        this.saveStage = saveStage;

        init();
    }

    public SaveStage getSaveStage() {
        return saveStage;
    }
}