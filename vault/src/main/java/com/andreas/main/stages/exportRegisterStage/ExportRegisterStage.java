package com.andreas.main.stages.exportRegisterStage;

import com.andreas.main.app.AppStage;
import com.andreas.main.stages.saveStage.SaveStage;

import javafx.application.Application;
import javafx.stage.Modality;

public class ExportRegisterStage extends AppStage {

    private SaveStage saveStage;

    public ExportRegisterStage(Application app, SaveStage saveStage) {
        super(app, "stages/exportRegisterStage/exportRegister.fxml");
        setTitle("Export");
        setResizable(false);
        initModality(Modality.WINDOW_MODAL);
        
        this.saveStage = saveStage;

        init();
    }
    

    public SaveStage getSaveStage() {
        return saveStage;
    }
}