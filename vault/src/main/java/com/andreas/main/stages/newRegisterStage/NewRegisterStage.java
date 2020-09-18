package com.andreas.main.stages.newRegisterStage;

import com.andreas.main.app.AppStage;
import com.andreas.main.stages.saveStage.SaveStage;

import javafx.application.Application;
import javafx.stage.Modality;

public class NewRegisterStage extends AppStage {

    private SaveStage saveStage;

    public NewRegisterStage(Application app, SaveStage saveStage) {
        super(app, "stages/newRegisterStage/newRegister.fxml");
        setTitle("New register");
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        this.saveStage = saveStage;
        
        init();
    }
    
    public SaveStage getSaveStage() {
        return saveStage;
    } 
}