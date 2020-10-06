package com.andreas.main.stages.createBackupStage;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppStage;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.application.Application;
import javafx.stage.Modality;

public class CreateBackupStage extends AppStage {

    private SaveScene saveScene;

    public CreateBackupStage(Application app, SaveScene saveScene) {
        super(app);
        setResizable(false);
        initModality(Modality.WINDOW_MODAL);

        this.saveScene = saveScene;
        setScene(new AppScene(app, "stages/createBackupStage/createBackup.fxml", "Create backup"));
    }

    public SaveScene getSaveScene() {
        return saveScene;
    }
}
