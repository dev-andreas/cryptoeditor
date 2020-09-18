package com.andreas.main.stages.newSaveStage;

import com.andreas.main.app.AppStage;
import com.andreas.main.stages.loginStage.LoginStage;

import javafx.application.Application;
import javafx.stage.Modality;

public class NewSaveStage extends AppStage {
    
    private LoginStage loginStage;

    public NewSaveStage(Application app, LoginStage loginStage) {
        super(app, "stages/newSaveStage/newSave.fxml");
        setTitle("New save");
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        this.loginStage = loginStage;

        init();
    }

    public LoginStage getLoginStage() {
        return loginStage;
    }
}