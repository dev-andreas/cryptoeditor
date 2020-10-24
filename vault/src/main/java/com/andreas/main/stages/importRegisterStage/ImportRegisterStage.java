package com.andreas.main.stages.importRegisterStage;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppStage;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.application.Application;
import javafx.stage.Modality;

public class ImportRegisterStage extends AppStage {

    private SaveScene saveScene;

    public ImportRegisterStage(Application app, SaveScene saveScene) {
        super(app);
        setResizable(false);
        initModality(Modality.WINDOW_MODAL);

        this.saveScene = saveScene;
        setScene(new AppScene(app, "stages/importRegisterStage/importRegister.fxml", "Import"));
    }

    public SaveScene getSaveScene() {
        return saveScene;
    }
}