package com.andreas.main.stages.createKeyStage;

import com.andreas.main.app.AppStage;

import javafx.application.Application;
import javafx.stage.Modality;

public class CreateKeyStage extends AppStage {

    public CreateKeyStage(Application app) {
        super(app, "stages/createKeyStage/createKey.fxml");
        setTitle("Create key");
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        init();
    }
    
}