package com.andreas.main.stages.renameRegisterStage;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppStage;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.application.Application;
import javafx.stage.Modality;

public class RenameRegisterStage extends AppStage {

    private SaveScene saveScene;

    public RenameRegisterStage(Application app, SaveScene saveScene) {
        super(app);
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        this.saveScene = saveScene;
        setScene(new AppScene(app, "stages/renameRegisterStage/renameRegister.fxml", "Rename register"));
    }

    public SaveScene getSaveScene() {
        return saveScene;
    }
}