package com.andreas.main.stages.importRegisterStage;

import com.andreas.main.app.AppStage;
import com.andreas.main.stages.saveStage.SaveStage;

import javafx.application.Application;
import javafx.stage.Modality;

public class ImportRegisterStage extends AppStage {

    private SaveStage saveStage;

    public ImportRegisterStage(Application app, SaveStage saveStage) {
        super(app, "stages/importRegisterStage/importRegister.fxml");
        setTitle("Import");
        setResizable(false);
        initModality(Modality.WINDOW_MODAL);

        this.saveStage = saveStage;

        init();
    }

    public SaveStage getSaveStage() {
        return saveStage;
    }
}