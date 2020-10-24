package com.andreas.main.stages.mainStage;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppStage;
import com.andreas.main.stages.mainStage.scenes.loginScene.LoginScene;

import javafx.application.Application;
import javafx.stage.Modality;

public class MainStage extends AppStage {

    public MainStage(Application app) {
        super(app);
        setTitle("cryptoeditor");
        initModality(Modality.NONE);
        AppScene scene = new LoginScene(app);
        setScene(scene);
    }
}
