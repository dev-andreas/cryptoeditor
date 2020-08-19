package com.andreas.main.stages.newSaveStage;

import com.andreas.main.App;
import com.andreas.main.app.AppStage;
import com.andreas.main.stages.loginStage.LoginStage;

import javafx.stage.Modality;

public class NewSaveStage extends AppStage {
    
    private LoginStage loginStage;

    public NewSaveStage(App app, LoginStage loginStage) {
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