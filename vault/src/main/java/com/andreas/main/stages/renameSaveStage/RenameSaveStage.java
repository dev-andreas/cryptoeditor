package com.andreas.main.stages.renameSaveStage;

import com.andreas.main.App;
import com.andreas.main.app.AppStage;
import com.andreas.main.stages.loginStage.LoginStage;

import javafx.stage.Modality;

public class RenameSaveStage extends AppStage {

    private LoginStage loginStage;

    public RenameSaveStage(App app, LoginStage loginStage) {
        super(app, "stages/renameSaveStage/renameSave.fxml");
        setTitle("Rename Save");
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        this.loginStage = loginStage;

        init();
    }
    
    public LoginStage getLoginStage() {
        return loginStage;
    }
}