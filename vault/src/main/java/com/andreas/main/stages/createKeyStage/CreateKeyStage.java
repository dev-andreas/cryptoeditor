package com.andreas.main.stages.createKeyStage;

import com.andreas.main.App;
import com.andreas.main.app.AppStage;

import javafx.stage.Modality;

public class CreateKeyStage extends AppStage {

    public CreateKeyStage(App app) {
        super(app, "stages/createKeyStage/createKey.fxml");
        setTitle("Create key");
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        init();
    }
    
}