package com.andreas.main.stages.loadBackupStage;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppStage;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.application.Application;
import javafx.stage.Modality;

public class LoadBackupStage extends AppStage {

    private SaveScene saveScene;

    public LoadBackupStage(Application app, SaveScene saveScene) {
        super(app);
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        this.saveScene = saveScene;

        setScene(new AppScene(app, "stages/loadBackupStage/loadBackup.fxml", "Load backup"));
    }

    public SaveScene getSaveScene() {
        return saveScene;
    }
}
