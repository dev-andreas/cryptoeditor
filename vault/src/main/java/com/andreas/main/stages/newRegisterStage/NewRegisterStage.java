package com.andreas.main.stages.newRegisterStage;

import com.andreas.main.App;
import com.andreas.main.app.AppStage;
import com.andreas.main.stages.saveStage.SaveStage;

import javafx.stage.Modality;

public class NewRegisterStage extends AppStage {

    private SaveStage saveStage;

    public NewRegisterStage(App app, SaveStage saveStage) {
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