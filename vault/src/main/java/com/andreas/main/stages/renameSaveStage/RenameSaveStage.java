package com.andreas.main.stages.renameSaveStage;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppStage;
import com.andreas.main.stages.mainStage.scenes.loginScene.LoginScene;

import javafx.application.Application;
import javafx.stage.Modality;

public class RenameSaveStage extends AppStage {

    private LoginScene loginScene;

    public RenameSaveStage(Application app, LoginScene loginScene) {
        super(app);
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        this.loginScene = loginScene;
        setScene(new AppScene(app, "stages/renameSaveStage/renameSave.fxml", "Rename save"));
    }
    
    public LoginScene getLoginScene() {
        return loginScene;
    }
}