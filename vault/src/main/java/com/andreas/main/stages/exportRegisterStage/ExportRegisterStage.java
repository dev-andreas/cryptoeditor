package com.andreas.main.stages.exportRegisterStage;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppStage;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.application.Application;
import javafx.stage.Modality;

public class ExportRegisterStage extends AppStage {

    private SaveScene saveScene;

    public ExportRegisterStage(Application app, SaveScene saveScene) {
        super(app);
        setResizable(false);
        initModality(Modality.WINDOW_MODAL);
        
        this.saveScene = saveScene;
        setScene(new AppScene(app, "stages/exportRegisterStage/exportRegister.fxml", "Export"));
    }
    

    public SaveScene getSaveScene() {
        return saveScene;
    }
}