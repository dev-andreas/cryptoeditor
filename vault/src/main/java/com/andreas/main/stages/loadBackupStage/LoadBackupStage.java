package com.andreas.main.stages.loadBackupStage;

import com.andreas.main.app.AppStage;
import com.andreas.main.stages.saveStage.SaveStage;

import javafx.application.Application;
import javafx.stage.Modality;

public class LoadBackupStage extends AppStage {

    private SaveStage saveStage;

    public LoadBackupStage(Application app, SaveStage saveStage) {
        super(app, "stages/loadBackupStage/loadBackup.fxml");
        setTitle("Load backup");
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        this.saveStage = saveStage;

        init();
    }

    public SaveStage getSaveStage() {
        return saveStage;
    }
}
