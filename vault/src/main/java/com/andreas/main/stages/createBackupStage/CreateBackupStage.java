package com.andreas.main.stages.createBackupStage;

import com.andreas.main.app.AppStage;
import com.andreas.main.stages.saveStage.SaveStage;

import javafx.application.Application;
import javafx.stage.Modality;

public class CreateBackupStage extends AppStage {

    private SaveStage saveStage;

    public CreateBackupStage(Application app, SaveStage saveStage) {
        super(app, "stages/backupStage/backup.fxml");
        setTitle("Create backup");
        setResizable(false);
        initModality(Modality.WINDOW_MODAL);

        this.saveStage = saveStage;

        init();
    }

    public SaveStage getSaveStage() {
        return saveStage;
    }
}