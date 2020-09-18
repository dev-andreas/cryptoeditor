package com.andreas.main.stages.removeSaveStage;

import com.andreas.main.app.AppStage;
import com.andreas.main.stages.loginStage.LoginStage;

import javafx.application.Application;
import javafx.stage.Modality;

public class RemoveSaveStage extends AppStage {

    private LoginStage loginStage;

    public RemoveSaveStage(Application app, LoginStage loginStage) {
        super(app, "stages/removeSaveStage/removeSave.fxml");
        setTitle("Remove Save");
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        this.loginStage = loginStage;

        init();
    }
    
    public LoginStage getLoginStage() {
        return loginStage;
    }
}