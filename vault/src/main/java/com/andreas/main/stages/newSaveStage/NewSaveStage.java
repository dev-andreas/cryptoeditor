package com.andreas.main.stages.newSaveStage;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppStage;
import com.andreas.main.stages.mainStage.scenes.loginScene.LoginScene;

import javafx.application.Application;
import javafx.stage.Modality;

public class NewSaveStage extends AppStage {
    
    private LoginScene loginScene;

    public NewSaveStage(Application app, LoginScene loginScene) {
        super(app);
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        this.loginScene = loginScene;
        setScene(new AppScene(app, "stages/newSaveStage/newSave.fxml", "New save"));
    }

    public LoginScene getLoginScene() {
        return loginScene;
    }
}